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
import com.algaworks.algashop.ordering.infrastructure.persistence.repository.ShoppingCartPersistenceEntityRepository;
import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;

@Tag("integrationTest")
@DataJpaTest
@Import({
    ShoppingCartUpdateProvider.class,
    ShoppingCartPersistenceProvider.class,
    ShoppingCartPersistenceEntityAssembler.class,
    ShoppingCartPersistenceEntityDisassembler.class,
    SpringDataAuditingConfig.class
})
@DirtiesContext
class ShoppingCartUpdateProviderTest {

  private final ShoppingCartUpdateProvider shoppingCartUpdateProvider;
  private final ShoppingCartPersistenceProvider provider;
  private final ShoppingCartPersistenceEntityRepository repository;
  private final TestEntityManager testEntityManager;

  @Autowired
  ShoppingCartUpdateProviderTest(ShoppingCartUpdateProvider shoppingCartUpdateProvider,
      ShoppingCartPersistenceProvider shoppingCartPersistenceProvider,
      ShoppingCartPersistenceEntityRepository shoppingCartPersistenceEntityRepository,
      TestEntityManager testEntityManager) {
    this.shoppingCartUpdateProvider = shoppingCartUpdateProvider;
    this.provider = shoppingCartPersistenceProvider;
    this.repository = shoppingCartPersistenceEntityRepository;
    this.testEntityManager = testEntityManager;
  }

  @Test
  @DisplayName("Should update item price and total amount")
  void shouldUpdateItemPriceAndTotalAmount() {
    var productId = new ProductId(UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccccc"));
    var shoppingCart = shoppingCartWithItem(productId);

    provider.add(shoppingCart);

    var updatedPrice = new Money(new BigDecimal("1500.00"));
    shoppingCartUpdateProvider.adjustPrice(productId, updatedPrice);
    testEntityManager.flush();
    testEntityManager.clear();

    var persisted = repository.findById(shoppingCart.id().value()).orElseThrow();
    assertThat(persisted.getTotalAmount()).isEqualByComparingTo(new BigDecimal("3000.00"));
    assertThat(persisted.getTotalItems()).isEqualTo(2);
    assertThat(persisted.getItems()).hasSize(1);

    var item = persisted.getItems().iterator().next();
    assertThat(item.getProductId()).isEqualTo(productId.value());
    assertThat(item.getPrice()).isEqualByComparingTo(updatedPrice.value());
    assertThat(item.getTotalAmount()).isEqualByComparingTo(new BigDecimal("3000.00"));
  }

  private ShoppingCart shoppingCartWithItem(ProductId productId) {
    var shoppingCartId = new ShoppingCartId(UUID.fromString("55555555-5555-5555-5555-555555555555"));
    var customerId = new CustomerId(UUID.fromString("66666666-6666-6666-6666-666666666666"));
    var item = ShoppingCartItem.existingShoppingCartItemBuilder()
        .id(new ShoppingCartItemId(UUID.fromString("77777777-7777-7777-7777-777777777777")))
        .shoppingCartId(shoppingCartId)
        .product(productId)
        .productName(new ProductName("Notebook"))
        .price(new Money(new BigDecimal("1000.00")))
        .quantity(new Quantity(new BigDecimal("2")))
        .totalAmount(new Money(new BigDecimal("2000.00")))
        .available(true)
        .build();
    return ShoppingCartTestDataBuilder.aShoppingCart()
        .shoppingCartId(shoppingCartId)
        .customerId(customerId)
        .totalAmount(new Money(new BigDecimal("2000.00")))
        .totalItems(new Quantity(new BigDecimal("2")))
        .items(Set.of(item))
        .build();
  }

  @Test
  @DisplayName("Should update item availability")
  void shouldUpdateItemAvailability() {
    var productId = new ProductId(UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccccc"));
    var shoppingCart = shoppingCartWithItem(productId);

    provider.add(shoppingCart);

    shoppingCartUpdateProvider.changeAvailability(productId, false);
    testEntityManager.flush();
    testEntityManager.clear();

    var persisted = repository.findById(shoppingCart.id().value()).orElseThrow();
    assertThat(persisted.getItems()).hasSize(1);

    var item = persisted.getItems().iterator().next();
    assertThat(item.getProductId()).isEqualTo(productId.value());
    assertThat(item.getAvailable()).isFalse();
  }

  @Test
  @DisplayName("Should not adjust price when product is not in any cart")
  void shouldNotAdjustPriceWhenProductIsNotInAnyCart() {
    var productInCart = new ProductId(UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccccc"));
    var otherProduct = new ProductId(UUID.fromString("dddddddd-dddd-dddd-dddd-dddddddddddd"));
    var shoppingCart = shoppingCartWithItem(productInCart);

    provider.add(shoppingCart);

    var updatedPrice = new Money(new BigDecimal("1500.00"));
    shoppingCartUpdateProvider.adjustPrice(otherProduct, updatedPrice);

    testEntityManager.flush();
    testEntityManager.clear();

    var persisted = repository.findById(shoppingCart.id().value()).orElseThrow();

    assertThat(persisted.getItems()).hasSize(1);

    var item = persisted.getItems().iterator().next();
    assertThat(item.getProductId()).isEqualTo(productInCart.value());
    assertThat(item.getPrice()).isEqualByComparingTo(new BigDecimal("1000.00"));
    assertThat(item.getTotalAmount()).isEqualByComparingTo(new BigDecimal("2000.00"));
    assertThat(persisted.getTotalAmount()).isEqualByComparingTo(new BigDecimal("2000.00"));
  }

  @Test
  @DisplayName("Should not change availability when product is not in any cart")
  void shouldNotChangeAvailabilityWhenProductIsNotInAnyCart() {
    var productInCart = new ProductId(UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccccc"));
    var otherProduct = new ProductId(UUID.fromString("dddddddd-dddd-dddd-dddd-dddddddddddd"));
    var shoppingCart = shoppingCartWithItem(productInCart);

    provider.add(shoppingCart);

    shoppingCartUpdateProvider.changeAvailability(otherProduct, false);

    testEntityManager.flush();
    testEntityManager.clear();

    var persisted = repository.findById(shoppingCart.id().value()).orElseThrow();

    assertThat(persisted.getItems()).hasSize(1);

    var item = persisted.getItems().iterator().next();
    assertThat(item.getProductId()).isEqualTo(productInCart.value());
    assertThat(item.getAvailable()).isTrue();
  }
}