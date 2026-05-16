package com.algaworks.algashop.ordering.domain.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.algaworks.algashop.ordering.domain.valueobject.Money;
import com.algaworks.algashop.ordering.domain.valueobject.ProductName;
import com.algaworks.algashop.ordering.domain.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.valueobject.id.ProductId;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Order.addOrderItem")
class AddOrderItemTest {

  private Order order;
  private ProductId productId;
  private ProductName productName;
  private Money price;
  private Quantity quantity;

  @BeforeEach
  void setUp() {
    order = OrderTestDataBuilder.anOrder().build();
    productId = new ProductId(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"));
    productName = new ProductName("Notebook Pro");
    price = new Money("100.00");
    quantity = new Quantity(new BigDecimal("2"));
  }

  @Nested
  @DisplayName("Success scenarios")
  class SuccessScenarios {

    @Test
    @DisplayName("Should add item to the order")
    void shouldAddItemToOrder() {
      order.addOrderItem(productId, productName, price, quantity);

      assertThat(order.items()).hasSize(1);
    }

    @Test
    @DisplayName("Should bind the item to the order id")
    void shouldBindItemToOrderId() {
      order.addOrderItem(productId, productName, price, quantity);

      OrderItem addedItem = order.items().iterator().next();
      assertThat(addedItem.orderId()).isEqualTo(order.id());
    }

    @Test
    @DisplayName("Should persist item product data")
    void shouldPersistItemProductData() {
      order.addOrderItem(productId, productName, price, quantity);

      OrderItem addedItem = order.items().iterator().next();
      assertThat(addedItem.productId()).isEqualTo(productId);
      assertThat(addedItem.productName()).isEqualTo(productName);
      assertThat(addedItem.price()).isEqualTo(price);
      assertThat(addedItem.quantity()).isEqualTo(quantity);
    }

    @Test
    @DisplayName("Should increment totalItems by the item quantity")
    void shouldIncrementTotalItems() {
      Quantity initialTotalItems = order.totalItems();

      order.addOrderItem(productId, productName, price, quantity);

      assertThat(order.totalItems()).isEqualTo(initialTotalItems.add(quantity));
    }

    @Test
    @DisplayName("Should accumulate totalItems across multiple items")
    void shouldAccumulateTotalItemsAcrossMultipleItems() {
      ProductId productId2 = new ProductId(UUID.fromString("660e8400-e29b-41d4-a716-446655440001"));
      Quantity quantity2 = new Quantity(new BigDecimal("3"));

      order.addOrderItem(productId, productName, price, quantity);
      order.addOrderItem(productId2, new ProductName("Mouse"), new Money("30.00"), quantity2);

      assertThat(order.totalItems()).isEqualTo(new Quantity(new BigDecimal("5")));
      assertThat(order.items()).hasSize(2);
    }

    @Test
    @DisplayName("Should generate a non-null id for the added item")
    void shouldGenerateNonNullItemId() {
      order.addOrderItem(productId, productName, price, quantity);

      OrderItem addedItem = order.items().iterator().next();
      assertThat(addedItem.id()).isNotNull();
    }
  }

  @Nested
  @DisplayName("Failure scenarios")
  class FailureScenarios {

    @Test
    @DisplayName("Should throw NullPointerException when productId is null")
    void shouldThrowWhenProductIdIsNull() {
      assertThatThrownBy(() -> order.addOrderItem(null, productName, price, quantity))
          .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("Should throw NullPointerException when productName is null")
    void shouldThrowWhenProductNameIsNull() {
      assertThatThrownBy(() -> order.addOrderItem(productId, null, price, quantity))
          .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("Should throw NullPointerException when price is null")
    void shouldThrowWhenPriceIsNull() {
      assertThatThrownBy(() -> order.addOrderItem(productId, productName, null, quantity))
          .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("Should throw NullPointerException when quantity is null")
    void shouldThrowWhenQuantityIsNull() {
      assertThatThrownBy(() -> order.addOrderItem(productId, productName, price, null))
          .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("Should not add item when productId is null")
    void shouldNotAddItemWhenProductIdIsNull() {
      try {
        order.addOrderItem(null, productName, price, quantity);
      } catch (NullPointerException ignored) {
      }

      assertThat(order.items()).isEmpty();
    }

    @Test
    @DisplayName("Should not change totalItems when productId is null")
    void shouldNotChangeTotalItemsWhenProductIdIsNull() {
      Quantity initialTotalItems = order.totalItems();

      try {
        order.addOrderItem(null, productName, price, quantity);
      } catch (NullPointerException ignored) {
      }

      assertThat(order.totalItems()).isEqualTo(initialTotalItems);
    }
  }
}
