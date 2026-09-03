package com.algaworks.algashop.ordering.infrastructure.persistence.provider;

import static org.assertj.core.api.Assertions.assertThat;

import com.algaworks.algashop.ordering.domain.entity.ShoppingCartTestDataBuilder;
import com.algaworks.algashop.ordering.domain.model.entity.ShoppingCart;
import com.algaworks.algashop.ordering.domain.model.entity.ShoppingCartItem;
import com.algaworks.algashop.ordering.domain.model.valueobject.Money;
import com.algaworks.algashop.ordering.domain.model.valueobject.ProductName;
import com.algaworks.algashop.ordering.domain.model.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.ProductId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.ShoppingCartId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.ShoppingCartItemId;
import com.algaworks.algashop.ordering.infrastructure.persistence.assembler.ShoppingCartPersistenceEntityAssembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.config.SpringDataAuditingConfig;
import com.algaworks.algashop.ordering.infrastructure.persistence.disassembler.ShoppingCartPersistenceEntityDisassembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.ShoppingCartPersistenceEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.repository.ShoppingCartPersistenceEntityRepository;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@Tag("integrationTest")
@DataJpaTest
@Import({
    ShoppingCartPersistenceProvider.class,
    ShoppingCartPersistenceEntityAssembler.class,
    ShoppingCartPersistenceEntityDisassembler.class,
    SpringDataAuditingConfig.class
})
class ShoppingCartPersistenceProviderIntegrationTest {

  @Autowired
  private ShoppingCartPersistenceProvider provider;

  @Autowired
  private ShoppingCartPersistenceEntityRepository repository;

  @Test
  @DisplayName("Should persist a shopping cart with items")
  void shouldPersistShoppingCartWithItems() {
    ShoppingCart shoppingCart = shoppingCartWithItems();

    provider.add(shoppingCart);

    Optional<ShoppingCartPersistenceEntity> result = repository.findById(shoppingCart.id().value());

    assertThat(result).isPresent();
    assertThat(result.get().getItems()).hasSize(1);
    assertThat(result.get().getItems().iterator().next().getProductName()).isEqualTo("Notebook");
    assertThat(result.get().getTotalAmount()).isEqualByComparingTo(new BigDecimal("2000.00"));
    assertThat(result.get().getTotalItems()).isEqualTo(2);
  }

  @Test
  @DisplayName("Should populate auditing fields automatically")
  void shouldPopulateAuditingFieldsAutomatically() {
    ShoppingCart shoppingCart = shoppingCartWithItems();

    provider.add(shoppingCart);

    ShoppingCartPersistenceEntity result = repository.findById(shoppingCart.id().value()).orElseThrow();

    assertThat(result.getCreatedAt()).isNotNull();
    assertThat(result.getLastModifiedAt()).isNotNull();
    assertThat(result.getCreatedByUserId()).isNotNull();
    assertThat(result.getLastModifiedByUserId()).isNotNull();
  }

  @Test
  @DisplayName("Should count records in repository")
  void shouldCountRecordsInRepository() {
    provider.add(shoppingCartWithItems());
    provider.add(shoppingCartWithDifferentCustomer());

    assertThat(provider.count()).isEqualTo(2);
    assertThat(repository.count()).isEqualTo(2);
  }

  @Test
  @DisplayName("Should find shopping cart by customer id")
  void shouldFindShoppingCartByCustomerId() {
    ShoppingCart shoppingCart = shoppingCartWithItems();
    provider.add(shoppingCart);

    Optional<ShoppingCart> result = provider.ofCustomer(shoppingCart.customerId());

    assertThat(result).isPresent();
    assertThat(result.get().id()).isEqualTo(shoppingCart.id());
    assertThat(result.get().customerId()).isEqualTo(shoppingCart.customerId());
    assertThat(result.get().items()).hasSize(1);
  }

  private ShoppingCart shoppingCartWithItems() {
    var customerId = new CustomerId(UUID.fromString("22222222-2222-2222-2222-222222222222"));
    return ShoppingCartTestDataBuilder.aShoppingCart()
        .shoppingCartId(new ShoppingCartId(UUID.fromString("11111111-1111-1111-1111-111111111111")))
        .customerId(customerId)
        .totalAmount(new Money(new BigDecimal("2000.00")))
        .totalItems(new Quantity(new BigDecimal("2")))
        .items(Set.of(shoppingCartItem(new ShoppingCartId(UUID.fromString("11111111-1111-1111-1111-111111111111")))))
        .build();
  }

  private ShoppingCart shoppingCartWithDifferentCustomer() {
    var customerId = new CustomerId(UUID.fromString("33333333-3333-3333-3333-333333333333"));
    return ShoppingCartTestDataBuilder.aShoppingCart()
        .shoppingCartId(new ShoppingCartId(UUID.fromString("44444444-4444-4444-4444-444444444444")))
        .customerId(customerId)
        .totalAmount(new Money(new BigDecimal("500.00")))
        .totalItems(new Quantity(new BigDecimal("1")))
        .items(Set.of(shoppingCartItem(new ShoppingCartId(UUID.fromString("44444444-4444-4444-4444-444444444444")))))
        .build();
  }

  private ShoppingCartItem shoppingCartItem(ShoppingCartId shoppingCartId) {
    return ShoppingCartItem.existingShoppingCartItemBuilder()
        .id(new ShoppingCartItemId(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")))
        .shoppingCartId(shoppingCartId)
        .product(new ProductId(UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb")))
        .productName(new ProductName("Notebook"))
        .price(new Money(new BigDecimal("1000.00")))
        .quantity(new Quantity(new BigDecimal("2")))
        .totalAmount(new Money(new BigDecimal("2000.00")))
        .available(true)
        .build();
  }
}
