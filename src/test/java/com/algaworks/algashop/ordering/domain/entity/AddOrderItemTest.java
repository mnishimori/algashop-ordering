package com.algaworks.algashop.ordering.domain.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.algaworks.algashop.ordering.domain.exception.ProductOutOfStockException;
import com.algaworks.algashop.ordering.domain.valueobject.Money;
import com.algaworks.algashop.ordering.domain.valueobject.Product;
import com.algaworks.algashop.ordering.domain.valueobject.ProductName;
import com.algaworks.algashop.ordering.domain.valueobject.Quantity;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Order.addOrderItem")
class AddOrderItemTest {

  private Order order;
  private Product product;
  private Quantity quantity;

  @BeforeEach
  void setUp() {
    order = OrderTestDataBuilder.anOrder().build();
    product = ProductTestDataBuilder.createProduct()
        .name(new ProductName("Notebook Pro"))
        .price(new Money("100.00"))
        .inStock(true)
        .build();
    quantity = new Quantity(new BigDecimal("2"));
  }

  @Nested
  @DisplayName("Success scenarios")
  class SuccessScenarios {

    @Test
    @DisplayName("Should add item to the order")
    void shouldAddItemToOrder() {
      order.addOrderItem(product, quantity);

      assertThat(order.items()).hasSize(1);
    }

    @Test
    @DisplayName("Should bind the item to the order id")
    void shouldBindItemToOrderId() {
      order.addOrderItem(product, quantity);

      OrderItem addedItem = order.items().iterator().next();
      assertThat(addedItem.orderId()).isEqualTo(order.id());
    }

    @Test
    @DisplayName("Should persist item product data")
    void shouldPersistItemProductData() {
      order.addOrderItem(product, quantity);

      OrderItem addedItem = order.items().iterator().next();
      assertThat(addedItem.productId()).isEqualTo(product.id());
      assertThat(addedItem.productName()).isEqualTo(product.name());
      assertThat(addedItem.price()).isEqualTo(product.price());
      assertThat(addedItem.quantity()).isEqualTo(quantity);
    }

    @Test
    @DisplayName("Should increment totalItems by the item quantity")
    void shouldIncrementTotalItems() {
      Quantity initialTotalItems = order.totalItems();

      order.addOrderItem(product, quantity);

      assertThat(order.totalItems()).isEqualTo(initialTotalItems.add(quantity));
    }

    @Test
    @DisplayName("Should accumulate totalItems across multiple items")
    void shouldAccumulateTotalItemsAcrossMultipleItems() {
      var product2 = ProductTestDataBuilder.createProduct()
          .name(new ProductName("Mouse"))
          .price(new Money("30.00"))
          .inStock(true)
          .build();
      Quantity quantity2 = new Quantity(new BigDecimal("3"));

      order.addOrderItem(product, quantity);
      order.addOrderItem(product2, quantity2);

      assertThat(order.totalItems()).isEqualTo(new Quantity(new BigDecimal("5")));
      assertThat(order.items()).hasSize(2);
    }

    @Test
    @DisplayName("Should generate a non-null id for the added item")
    void shouldGenerateNonNullItemId() {
      order.addOrderItem(product, quantity);

      OrderItem addedItem = order.items().iterator().next();
      assertThat(addedItem.id()).isNotNull();
    }

    @Test
    @DisplayName("Should calculate totalAmount as price multiplied by quantity")
    void shouldCalculateTotalAmount() {
      order.addOrderItem(product, quantity);

      OrderItem addedItem = order.items().iterator().next();
      Money expectedTotalAmount = product.price().multiply(quantity.value().intValue());
      assertThat(addedItem.totalAmount()).isEqualTo(expectedTotalAmount);
    }

    @Test
    @DisplayName("Should calculate totalAmount correctly for different quantities")
    void shouldCalculateTotalAmountForDifferentQuantities() {
      Quantity quantity1 = new Quantity(new BigDecimal("5"));
      Quantity quantity2 = new Quantity(new BigDecimal("10"));
      var product2 = ProductTestDataBuilder.createProduct()
          .name(new ProductName("Mouse"))
          .price(new Money("30.00"))
          .inStock(true)
          .build();

      order.addOrderItem(product, quantity1);
      order.addOrderItem(product2, quantity2);

      var items = order.items().iterator();
      OrderItem item1 = items.next();
      OrderItem item2 = items.next();

      assertThat(item1.totalAmount()).isEqualTo(product.price().multiply(5));
      assertThat(item2.totalAmount()).isEqualTo(product2.price().multiply(10));
    }
  }

  @Nested
  @DisplayName("Failure scenarios")
  class FailureScenarios {

    @Test
    @DisplayName("Should throw NullPointerException when product is null")
    void shouldThrowWhenProductIsNull() {
      assertThatThrownBy(() -> order.addOrderItem(null, quantity))
          .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("Should throw NullPointerException when quantity is null")
    void shouldThrowWhenQuantityIsNull() {
      assertThatThrownBy(() -> order.addOrderItem(product, null))
          .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("Should not add item when product is null")
    void shouldNotAddItemWhenProductIsNull() {
      try {
        order.addOrderItem(null, quantity);
      } catch (NullPointerException ignored) {
      }

      assertThat(order.items()).isEmpty();
    }

    @Test
    @DisplayName("Should not change totalItems when product is null")
    void shouldNotChangeTotalItemsWhenProductIsNull() {
      Quantity initialTotalItems = order.totalItems();

      try {
        order.addOrderItem(null, quantity);
      } catch (NullPointerException ignored) {
      }

      assertThat(order.totalItems()).isEqualTo(initialTotalItems);
    }

    @Test
    @DisplayName("Should throw ProductOutOfStockException when product is out of stock")
    void shouldThrowWhenProductIsOutOfStock() {
      var outOfStockProduct = ProductTestDataBuilder.createProduct()
          .name(new ProductName("Notebook Pro"))
          .price(new Money("100.00"))
          .inStock(false)
          .build();

      assertThatThrownBy(() -> order.addOrderItem(outOfStockProduct, quantity))
          .isInstanceOf(ProductOutOfStockException.class);
    }

    @Test
    @DisplayName("Should not add item when product is out of stock")
    void shouldNotAddItemWhenProductIsOutOfStock() {
      var outOfStockProduct = ProductTestDataBuilder.createProduct()
          .name(new ProductName("Notebook Pro"))
          .price(new Money("100.00"))
          .inStock(false)
          .build();

      try {
        order.addOrderItem(outOfStockProduct, quantity);
      } catch (ProductOutOfStockException ignored) {
      }

      assertThat(order.items()).isEmpty();
    }

    @Test
    @DisplayName("Should not change totalItems when product is out of stock")
    void shouldNotChangeTotalItemsWhenProductIsOutOfStock() {
      var outOfStockProduct = ProductTestDataBuilder.createProduct()
          .name(new ProductName("Notebook Pro"))
          .price(new Money("100.00"))
          .inStock(false)
          .build();
      Quantity initialTotalItems = order.totalItems();

      try {
        order.addOrderItem(outOfStockProduct, quantity);
      } catch (ProductOutOfStockException ignored) {
      }

      assertThat(order.totalItems()).isEqualTo(initialTotalItems);
    }
  }
}
