package com.algaworks.algashop.ordering.infrastructure.persistence.shoppingcart;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.algaworks.algashop.ordering.IntegrationTest;
import com.algaworks.algashop.ordering.application.shoppingcart.query.ShoppingCartItemOutput;
import com.algaworks.algashop.ordering.application.shoppingcart.query.ShoppingCartQueryService;
import com.algaworks.algashop.ordering.domain.entity.ShoppingCartTestDataBuilder;
import com.algaworks.algashop.ordering.domain.model.commons.Money;
import com.algaworks.algashop.ordering.domain.model.commons.Quantity;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerId;
import com.algaworks.algashop.ordering.domain.model.product.ProductId;
import com.algaworks.algashop.ordering.domain.model.product.ProductName;
import com.algaworks.algashop.ordering.domain.model.shoppingcart.ShoppingCart;
import com.algaworks.algashop.ordering.domain.model.shoppingcart.ShoppingCartId;
import com.algaworks.algashop.ordering.domain.model.shoppingcart.ShoppingCartItem;
import com.algaworks.algashop.ordering.domain.model.shoppingcart.ShoppingCartItemId;
import com.algaworks.algashop.ordering.domain.model.shoppingcart.ShoppingCartNotFoundException;
import com.algaworks.algashop.ordering.infrastructure.persistence.customer.CustomerPersistenceEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.customer.CustomerPersistenceEntityRepository;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
class ShoppingCartQueryServiceIT {

  @Autowired
  private ShoppingCartQueryService queryService;

  @Autowired
  private ShoppingCartPersistenceProvider persistenceProvider;

  @Autowired
  private CustomerPersistenceEntityRepository customerRepository;

  @Autowired
  private ShoppingCartPersistenceEntityRepository repository;

  @Test
  void shouldFindShoppingCartById() {
    var cart = persistShoppingCart();

    var output = queryService.findById(cart.getId());

    assertShoppingCart(output, cart);
  }

  @Test
  void shouldThrowShoppingCartNotFoundExceptionWhenFindingByNonExistentId() {
    var nonExistentId = UUID.randomUUID();

    assertThatThrownBy(() -> queryService.findById(nonExistentId))
        .isInstanceOf(ShoppingCartNotFoundException.class);
  }

  @Test
  void shouldFindShoppingCartByCustomerId() {
    var cart = persistShoppingCart();

    var output = queryService.findByCustomerId(cart.getCustomer().getId());

    assertShoppingCart(output, cart);
  }

  @Test
  void shouldThrowShoppingCartNotFoundExceptionWhenFindingByNonExistentCustomerId() {
    var nonExistentCustomerId = UUID.randomUUID();

    assertThatThrownBy(() -> queryService.findByCustomerId(nonExistentCustomerId))
        .isInstanceOf(ShoppingCartNotFoundException.class);
  }

  private void assertShoppingCart(
      com.algaworks.algashop.ordering.application.shoppingcart.query.ShoppingCartOutput output,
      ShoppingCartPersistenceEntity cart) {
    assertThat(output.getId()).isEqualTo(cart.getId());
    assertThat(output.getCustomerId()).isEqualTo(cart.getCustomer().getId());
    assertThat(output.getTotalItems()).isEqualTo(3);
    assertThat(output.getTotalAmount()).isEqualByComparingTo("1250.00");
    assertThat(output.getItems()).hasSize(2);
    assertThat(output.getItems())
        .extracting(ShoppingCartItemOutput::getName)
        .containsExactlyInAnyOrder("Notebook", "Mouse");
    var notebook = output.getItems().stream()
        .filter(i -> i.getName().equals("Notebook")).findFirst().orElseThrow();
    assertThat(notebook.getProductId()).isNotNull();
    assertThat(notebook.getPrice()).isEqualByComparingTo("500.00");
    assertThat(notebook.getQuantity()).isEqualTo(2);
    assertThat(notebook.getTotalAmount()).isEqualByComparingTo("1000.00");
    assertThat(notebook.getAvailable()).isTrue();
  }

  private ShoppingCartPersistenceEntity persistShoppingCart() {
    var customerId = UUID.randomUUID();
    persistCustomer(customerId);

    var cart = buildShoppingCart(customerId);

    persistenceProvider.add(cart);

    return repository.findById(cart.id().value()).orElseThrow();
  }

  private void persistCustomer(UUID customerId) {
    var customer = new CustomerPersistenceEntity();
    customer.setId(customerId);
    customer.setFirstName("João");
    customer.setLastName("Silva");
    customer.setEmail("joao.silva@example.com");
    customer.setDocument("12345678901");
    customer.setPhone("11999999999");
    customerRepository.saveAndFlush(customer);
  }

  private ShoppingCart buildShoppingCart(UUID customerId) {
    var shoppingCartId = new ShoppingCartId(UUID.randomUUID());

    var notebook = ShoppingCartItem.existingShoppingCartItemBuilder()
        .id(new ShoppingCartItemId(UUID.randomUUID()))
        .shoppingCartId(shoppingCartId)
        .product(new ProductId(UUID.randomUUID()))
        .productName(new ProductName("Notebook"))
        .price(new Money(new BigDecimal("500.00")))
        .quantity(new Quantity(new BigDecimal("2")))
        .totalAmount(new Money(new BigDecimal("1000.00")))
        .available(true)
        .build();

    var mouse = ShoppingCartItem.existingShoppingCartItemBuilder()
        .id(new ShoppingCartItemId(UUID.randomUUID()))
        .shoppingCartId(shoppingCartId)
        .product(new ProductId(UUID.randomUUID()))
        .productName(new ProductName("Mouse"))
        .price(new Money(new BigDecimal("250.00")))
        .quantity(new Quantity(new BigDecimal("1")))
        .totalAmount(new Money(new BigDecimal("250.00")))
        .available(true)
        .build();

    return ShoppingCartTestDataBuilder.aShoppingCart()
        .shoppingCartId(shoppingCartId)
        .customerId(new CustomerId(customerId))
        .totalAmount(new Money(new BigDecimal("1250.00")))
        .totalItems(new Quantity(new BigDecimal("3")))
        .items(java.util.Set.of(notebook, mouse))
        .build();
  }
}
