package com.algaworks.algashop.ordering.domain.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.algaworks.algashop.ordering.domain.valueobject.Money;
import com.algaworks.algashop.ordering.domain.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.valueobject.id.OrderId;
import io.hypersistence.tsid.TSID;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

  @Test
  @DisplayName("Should create order with all parameters")
  void shouldCreateOrderWithAllParameters() {
    OrderId orderId = new OrderId(TSID.from(123456789L));
    CustomerId customerId = new CustomerId(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"));
    Money totalAmount = new Money("100.00");
    Quantity totalItems = new Quantity(new BigDecimal(5));
    OffsetDateTime placedAt = OffsetDateTime.now();
    OffsetDateTime paidAt = OffsetDateTime.now();
    OffsetDateTime canceledAt = null;
    OffsetDateTime readyAt = null;
    OrderStatus status = OrderStatus.PLACED;
    PaymentMethod paymentMethod = PaymentMethod.CREDIT_CARD;
    Money shippingCost = new Money("10.00");
    LocalDate expectedDeliveryDate = LocalDate.now().plusDays(7);
    Set<OrderItem> items = new HashSet<>();

    Order order = new Order(orderId, customerId, totalAmount, totalItems, placedAt, paidAt, canceledAt,
        readyAt, null, null, status, paymentMethod, shippingCost, expectedDeliveryDate, items);

    assertThat(order.id()).isEqualTo(orderId);
    assertThat(order.customerId()).isEqualTo(customerId);
    assertThat(order.totalAmount()).isEqualTo(totalAmount);
    assertThat(order.totalItems()).isEqualTo(totalItems);
    assertThat(order.placedAt()).isEqualTo(placedAt);
    assertThat(order.paidAt()).isEqualTo(paidAt);
    assertThat(order.canceledAt()).isNull();
    assertThat(order.readyAt()).isNull();
    assertThat(order.status()).isEqualTo(status);
    assertThat(order.paymentMethod()).isEqualTo(paymentMethod);
    assertThat(order.shippingCost()).isEqualTo(shippingCost);
    assertThat(order.expectedDeliveryDate()).isEqualTo(expectedDeliveryDate);
    assertThat(order.items()).isEqualTo(items);
  }

  @Test
  @DisplayName("Should create order with builder")
  void shouldCreateOrderWithBuilder() {
    Order order = OrderTestDataBuilder.anOrder().build();

    assertThat(order).isNotNull();
    assertThat(order.id()).isNotNull();
    assertThat(order.customerId()).isNotNull();
    assertThat(order.totalAmount()).isNotNull();
    assertThat(order.totalItems()).isNotNull();
    assertThat(order.status()).isEqualTo(OrderStatus.DRAFT);
  }

  @Test
  @DisplayName("Should throw exception when id is null")
  void shouldThrowExceptionWhenIdIsNull() {
    assertThatThrownBy(() -> OrderTestDataBuilder.anOrder().id(null).build())
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should throw exception when customerId is null")
  void shouldThrowExceptionWhenCustomerIdIsNull() {
    assertThatThrownBy(() -> OrderTestDataBuilder.anOrder().customerId(null).build())
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should throw exception when totalAmount is null")
  void shouldThrowExceptionWhenTotalAmountIsNull() {
    assertThatThrownBy(() -> OrderTestDataBuilder.anOrder().totalAmount(null).build())
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should throw exception when totalItems is null")
  void shouldThrowExceptionWhenTotalItemsIsNull() {
    assertThatThrownBy(() -> OrderTestDataBuilder.anOrder().totalItems(null).build())
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should throw exception when status is null")
  void shouldThrowExceptionWhenStatusIsNull() {
    assertThatThrownBy(() -> OrderTestDataBuilder.anOrder().status(null).build())
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should throw exception when items is null")
  void shouldThrowExceptionWhenItemsIsNull() {
    assertThatThrownBy(() -> OrderTestDataBuilder.anOrder().items(null).build())
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should allow null placedAt")
  void shouldAllowNullPlacedAt() {
    Order order = OrderTestDataBuilder.anOrder().placedAt(null).build();

    assertThat(order.placedAt()).isNull();
  }

  @Test
  @DisplayName("Should allow null paidAt")
  void shouldAllowNullPaidAt() {
    Order order = OrderTestDataBuilder.anOrder().paidAt(null).build();

    assertThat(order.paidAt()).isNull();
  }

  @Test
  @DisplayName("Should allow null canceledAt")
  void shouldAllowNullCanceledAt() {
    Order order = OrderTestDataBuilder.anOrder().canceledAt(null).build();

    assertThat(order.canceledAt()).isNull();
  }

  @Test
  @DisplayName("Should allow null readyAt")
  void shouldAllowNullReadyAt() {
    Order order = OrderTestDataBuilder.anOrder().readyAt(null).build();

    assertThat(order.readyAt()).isNull();
  }

  @Test
  @DisplayName("Should allow null billingInfo")
  void shouldAllowNullBillingInfo() {
    Order order = OrderTestDataBuilder.anOrder().billingInfo(null).build();

    assertThat(order.billingInfo()).isNull();
  }

  @Test
  @DisplayName("Should allow null shippingInfo")
  void shouldAllowNullShippingInfo() {
    Order order = OrderTestDataBuilder.anOrder().shippingInfo(null).build();

    assertThat(order.shippingInfo()).isNull();
  }

  @Test
  @DisplayName("Should allow null paymentMethod")
  void shouldAllowNullPaymentMethod() {
    Order order = OrderTestDataBuilder.anOrder().paymentMethod(null).build();

    assertThat(order.paymentMethod()).isNull();
  }

  @Test
  @DisplayName("Should allow null shippingCost")
  void shouldAllowNullShippingCost() {
    Order order = OrderTestDataBuilder.anOrder().shippingCost(null).build();

    assertThat(order.shippingCost()).isNull();
  }

  @Test
  @DisplayName("Should allow null expectedDeliveryDate")
  void shouldAllowNullExpectedDeliveryDate() {
    Order order = OrderTestDataBuilder.anOrder().expectedDeliveryDate(null).build();

    assertThat(order.expectedDeliveryDate()).isNull();
  }

  @Test
  @DisplayName("Should be equal when same id")
  void shouldBeEqualWhenSameId() {
    OrderId orderId = new OrderId(TSID.from(123456789L));
    Order order1 = OrderTestDataBuilder.anOrder().id(orderId).build();
    Order order2 = OrderTestDataBuilder.anOrder().id(orderId).build();

    assertThat(order1).isEqualTo(order2);
    assertThat(order1.hashCode()).isEqualTo(order2.hashCode());
  }

  @Test
  @DisplayName("Should not be equal when different id")
  void shouldNotBeEqualWhenDifferentId() {
    Order order1 = OrderTestDataBuilder.anOrder()
        .id(new OrderId(TSID.from(123456789L)))
        .build();
    Order order2 = OrderTestDataBuilder.anOrder()
        .id(new OrderId(TSID.from(987654321L)))
        .build();

    assertThat(order1).isNotEqualTo(order2);
  }

  @Test
  @DisplayName("Should not be equal to null")
  void shouldNotBeEqualToNull() {
    Order order = OrderTestDataBuilder.anOrder().build();

    assertThat(order).isNotEqualTo(null);
  }

  @Test
  @DisplayName("Should not be equal to different class")
  void shouldNotBeEqualToDifferentClass() {
    Order order = OrderTestDataBuilder.anOrder().build();

    assertThat(order).isNotEqualTo("not an order");
  }

  @Test
  @DisplayName("Should return id")
  void shouldReturnId() {
    OrderId orderId = new OrderId(TSID.from(123456789L));
    Order order = OrderTestDataBuilder.anOrder().id(orderId).build();

    assertThat(order.id()).isEqualTo(orderId);
  }

  @Test
  @DisplayName("Should return customerId")
  void shouldReturnCustomerId() {
    CustomerId customerId = new CustomerId(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"));
    Order order = OrderTestDataBuilder.anOrder().customerId(customerId).build();

    assertThat(order.customerId()).isEqualTo(customerId);
  }

  @Test
  @DisplayName("Should return totalAmount")
  void shouldReturnTotalAmount() {
    Money totalAmount = new Money("150.00");
    Order order = OrderTestDataBuilder.anOrder().totalAmount(totalAmount).build();

    assertThat(order.totalAmount()).isEqualTo(totalAmount);
  }

  @Test
  @DisplayName("Should return totalItems")
  void shouldReturnTotalItems() {
    Quantity totalItems = new Quantity(new BigDecimal(10));
    Order order = OrderTestDataBuilder.anOrder().totalItems(totalItems).build();

    assertThat(order.totalItems()).isEqualTo(totalItems);
  }

  @Test
  @DisplayName("Should return placedAt")
  void shouldReturnPlacedAt() {
    OffsetDateTime placedAt = OffsetDateTime.now();
    Order order = OrderTestDataBuilder.anOrder().placedAt(placedAt).build();

    assertThat(order.placedAt()).isEqualTo(placedAt);
  }

  @Test
  @DisplayName("Should return paidAt")
  void shouldReturnPaidAt() {
    OffsetDateTime paidAt = OffsetDateTime.now();
    Order order = OrderTestDataBuilder.anOrder().paidAt(paidAt).build();

    assertThat(order.paidAt()).isEqualTo(paidAt);
  }

  @Test
  @DisplayName("Should return canceledAt")
  void shouldReturnCanceledAt() {
    OffsetDateTime canceledAt = OffsetDateTime.now();
    Order order = OrderTestDataBuilder.anOrder().canceledAt(canceledAt).build();

    assertThat(order.canceledAt()).isEqualTo(canceledAt);
  }

  @Test
  @DisplayName("Should return readyAt")
  void shouldReturnReadyAt() {
    OffsetDateTime readyAt = OffsetDateTime.now();
    Order order = OrderTestDataBuilder.anOrder().readyAt(readyAt).build();

    assertThat(order.readyAt()).isEqualTo(readyAt);
  }

  @Test
  @DisplayName("Should return status")
  void shouldReturnStatus() {
    Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PAID).build();

    assertThat(order.status()).isEqualTo(OrderStatus.PAID);
  }

  @Test
  @DisplayName("Should return paymentMethod")
  void shouldReturnPaymentMethod() {
    Order order = OrderTestDataBuilder.anOrder().paymentMethod(PaymentMethod.GATEWAY_BALANCE).build();

    assertThat(order.paymentMethod()).isEqualTo(PaymentMethod.GATEWAY_BALANCE);
  }

  @Test
  @DisplayName("Should return shippingCost")
  void shouldReturnShippingCost() {
    Money shippingCost = new Money("25.00");
    Order order = OrderTestDataBuilder.anOrder().shippingCost(shippingCost).build();

    assertThat(order.shippingCost()).isEqualTo(shippingCost);
  }

  @Test
  @DisplayName("Should return expectedDeliveryDate")
  void shouldReturnExpectedDeliveryDate() {
    LocalDate expectedDeliveryDate = LocalDate.now().plusDays(14);
    Order order = OrderTestDataBuilder.anOrder().expectedDeliveryDate(expectedDeliveryDate).build();

    assertThat(order.expectedDeliveryDate()).isEqualTo(expectedDeliveryDate);
  }

  @Test
  @DisplayName("Should return items")
  void shouldReturnItems() {
    Set<OrderItem> items = new HashSet<>();
    Order order = OrderTestDataBuilder.anOrder().items(items).build();

    assertThat(order.items()).isEqualTo(items);
  }
}
