package com.algaworks.algashop.ordering.domain.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.algaworks.algashop.ordering.domain.valueobject.Money;
import com.algaworks.algashop.ordering.domain.valueobject.ProductName;
import com.algaworks.algashop.ordering.domain.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.valueobject.id.OrderId;
import com.algaworks.algashop.ordering.domain.valueobject.id.OrderItemId;
import com.algaworks.algashop.ordering.domain.valueobject.id.ProductId;
import io.hypersistence.tsid.TSID;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderItemTest {

  @Test
  @DisplayName("Should create order item with all parameters")
  void shouldCreateOrderItemWithAllParameters() {
    OrderItemId id = new OrderItemId(TSID.from(123456789L));
    OrderId orderId = new OrderId(TSID.from(987654321L));
    ProductId productId = new ProductId(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"));
    ProductName productName = new ProductName("Product Name");
    Money price = new Money("50.00");
    Quantity quantity = new Quantity(new BigDecimal(2));
    Money totalAmount = new Money("100.00");

    OrderItem orderItem = new OrderItem(id, orderId, productId, productName, price, quantity, totalAmount);

    assertThat(orderItem.id()).isEqualTo(id);
    assertThat(orderItem.orderId()).isEqualTo(orderId);
    assertThat(orderItem.productId()).isEqualTo(productId);
    assertThat(orderItem.productName()).isEqualTo(productName);
    assertThat(orderItem.price()).isEqualTo(price);
    assertThat(orderItem.quantity()).isEqualTo(quantity);
    assertThat(orderItem.totalAmount()).isEqualTo(totalAmount);
  }

  @Test
  @DisplayName("Should create order item with test data builder")
  void shouldCreateOrderItemWithTestDataBuilder() {
    OrderItem orderItem = OrderItemTestDataBuilder.anOrderItem().build();

    assertThat(orderItem).isNotNull();
    assertThat(orderItem.id()).isNotNull();
    assertThat(orderItem.orderId()).isNotNull();
    assertThat(orderItem.productId()).isNotNull();
    assertThat(orderItem.productName()).isNotNull();
    assertThat(orderItem.price()).isNotNull();
    assertThat(orderItem.quantity()).isNotNull();
    assertThat(orderItem.totalAmount()).isNotNull();
  }

  @Test
  @DisplayName("Should create order item with no-arg constructors and constants")
  void shouldCreateOrderItemWithNoArgConstructorsAndConstants() {
    OrderItemId id = new OrderItemId();
    OrderId orderId = new OrderId();
    ProductId productId = new ProductId();
    ProductName productName = new ProductName("Product Name");
    Money price = new Money("50.00");
    Quantity quantity = new Quantity(new BigDecimal(2));
    Money totalAmount = Money.ZERO;

    OrderItem orderItem = new OrderItem(id, orderId, productId, productName, price, quantity, totalAmount);

    assertThat(orderItem.id()).isNotNull();
    assertThat(orderItem.id().value()).isNotNull();
    assertThat(orderItem.orderId()).isNotNull();
    assertThat(orderItem.orderId().value()).isNotNull();
    assertThat(orderItem.productId()).isNotNull();
    assertThat(orderItem.totalAmount()).isEqualTo(Money.ZERO);
  }

  @Test
  @DisplayName("Should throw exception when id is null")
  void shouldThrowExceptionWhenIdIsNull() {
    assertThatThrownBy(() -> OrderItemTestDataBuilder.anOrderItem().id(null).build())
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should throw exception when orderId is null")
  void shouldThrowExceptionWhenOrderIdIsNull() {
    assertThatThrownBy(() -> OrderItemTestDataBuilder.anOrderItem().orderId(null).build())
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should throw exception when productId is null")
  void shouldThrowExceptionWhenProductIdIsNull() {
    assertThatThrownBy(() -> OrderItemTestDataBuilder.anOrderItem().productId(null).build())
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should throw exception when productName is null")
  void shouldThrowExceptionWhenProductNameIsNull() {
    assertThatThrownBy(() -> OrderItemTestDataBuilder.anOrderItem().productName(null).build())
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should throw exception when price is null")
  void shouldThrowExceptionWhenPriceIsNull() {
    assertThatThrownBy(() -> OrderItemTestDataBuilder.anOrderItem().price(null).build())
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should throw exception when quantity is null")
  void shouldThrowExceptionWhenQuantityIsNull() {
    assertThatThrownBy(() -> OrderItemTestDataBuilder.anOrderItem().quantity(null).build())
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should throw exception when totalAmount is null")
  void shouldThrowExceptionWhenTotalAmountIsNull() {
    assertThatThrownBy(() -> OrderItemTestDataBuilder.anOrderItem().totalAmount(null).build())
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should be equal when same id")
  void shouldBeEqualWhenSameId() {
    OrderItemId id = new OrderItemId(TSID.from(123456789L));
    OrderItem orderItem1 = OrderItemTestDataBuilder.anOrderItem().id(id).build();
    OrderItem orderItem2 = OrderItemTestDataBuilder.anOrderItem().id(id).build();

    assertThat(orderItem1).isEqualTo(orderItem2);
    assertThat(orderItem1.hashCode()).isEqualTo(orderItem2.hashCode());
  }

  @Test
  @DisplayName("Should not be equal when different id")
  void shouldNotBeEqualWhenDifferentId() {
    OrderItem orderItem1 = OrderItemTestDataBuilder.anOrderItem()
        .id(new OrderItemId(TSID.from(123456789L)))
        .build();
    OrderItem orderItem2 = OrderItemTestDataBuilder.anOrderItem()
        .id(new OrderItemId(TSID.from(987654321L)))
        .build();

    assertThat(orderItem1).isNotEqualTo(orderItem2);
  }

  @Test
  @DisplayName("Should not be equal to null")
  void shouldNotBeEqualToNull() {
    OrderItem orderItem = OrderItemTestDataBuilder.anOrderItem().build();

    assertThat(orderItem).isNotEqualTo(null);
  }

  @Test
  @DisplayName("Should not be equal to different class")
  void shouldNotBeEqualToDifferentClass() {
    OrderItem orderItem = OrderItemTestDataBuilder.anOrderItem().build();

    assertThat(orderItem).isNotEqualTo("not an order item");
  }

  @Test
  @DisplayName("Should return id")
  void shouldReturnId() {
    OrderItemId id = new OrderItemId(TSID.from(123456789L));
    OrderItem orderItem = OrderItemTestDataBuilder.anOrderItem().id(id).build();

    assertThat(orderItem.id()).isEqualTo(id);
  }

  @Test
  @DisplayName("Should return orderId")
  void shouldReturnOrderId() {
    OrderId orderId = new OrderId(TSID.from(987654321L));
    OrderItem orderItem = OrderItemTestDataBuilder.anOrderItem().orderId(orderId).build();

    assertThat(orderItem.orderId()).isEqualTo(orderId);
  }

  @Test
  @DisplayName("Should return productId")
  void shouldReturnProductId() {
    ProductId productId = new ProductId(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"));
    OrderItem orderItem = OrderItemTestDataBuilder.anOrderItem().productId(productId).build();

    assertThat(orderItem.productId()).isEqualTo(productId);
  }

  @Test
  @DisplayName("Should return productName")
  void shouldReturnProductName() {
    ProductName productName = new ProductName("Test Product");
    OrderItem orderItem = OrderItemTestDataBuilder.anOrderItem().productName(productName).build();

    assertThat(orderItem.productName()).isEqualTo(productName);
  }

  @Test
  @DisplayName("Should return price")
  void shouldReturnPrice() {
    Money price = new Money("75.00");
    OrderItem orderItem = OrderItemTestDataBuilder.anOrderItem().price(price).build();

    assertThat(orderItem.price()).isEqualTo(price);
  }

  @Test
  @DisplayName("Should return quantity")
  void shouldReturnQuantity() {
    Quantity quantity = new Quantity(new BigDecimal(3));
    OrderItem orderItem = OrderItemTestDataBuilder.anOrderItem().quantity(quantity).build();

    assertThat(orderItem.quantity()).isEqualTo(quantity);
  }

  @Test
  @DisplayName("Should return totalAmount")
  void shouldReturnTotalAmount() {
    Money totalAmount = new Money("225.00");
    OrderItem orderItem = OrderItemTestDataBuilder.anOrderItem().totalAmount(totalAmount).build();

    assertThat(orderItem.totalAmount()).isEqualTo(totalAmount);
  }

  @Test
  @DisplayName("Should create order item using existingOrderItemBuilder")
  void shouldCreateOrderItemUsingExistingOrderItemBuilder() {
    OrderItemId id = new OrderItemId(TSID.from(123456789L));
    OrderId orderId = new OrderId(TSID.from(987654321L));
    ProductId productId = new ProductId(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"));
    ProductName productName = new ProductName("Product Name");
    Money price = new Money("50.00");
    Quantity quantity = new Quantity(new BigDecimal(2));
    Money totalAmount = new Money("100.00");

    OrderItem orderItem = OrderItem.existingOrderItemBuilder()
        .id(id)
        .orderId(orderId)
        .productId(productId)
        .productName(productName)
        .price(price)
        .quantity(quantity)
        .totalAmount(totalAmount)
        .build();

    assertThat(orderItem.id()).isEqualTo(id);
    assertThat(orderItem.orderId()).isEqualTo(orderId);
    assertThat(orderItem.productId()).isEqualTo(productId);
    assertThat(orderItem.productName()).isEqualTo(productName);
    assertThat(orderItem.price()).isEqualTo(price);
    assertThat(orderItem.quantity()).isEqualTo(quantity);
    assertThat(orderItem.totalAmount()).isEqualTo(totalAmount);
  }

  @Test
  @DisplayName("Should throw exception when using existingOrderItemBuilder with null id")
  void shouldThrowExceptionWhenUsingExistingOrderItemBuilderWithNullId() {
    assertThatThrownBy(() -> OrderItem.existingOrderItemBuilder()
        .id(null)
        .orderId(new OrderId())
        .productId(new ProductId())
        .productName(new ProductName("Product"))
        .price(new Money("10.00"))
        .quantity(new Quantity(new BigDecimal(1)))
        .totalAmount(Money.ZERO)
        .build())
        .isInstanceOf(NullPointerException.class);
  }
}
