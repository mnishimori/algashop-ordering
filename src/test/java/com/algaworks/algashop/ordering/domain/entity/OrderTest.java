package com.algaworks.algashop.ordering.domain.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.algaworks.algashop.ordering.domain.valueobject.Money;
import com.algaworks.algashop.ordering.domain.valueobject.ProductName;
import com.algaworks.algashop.ordering.domain.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.valueobject.id.OrderId;
import com.algaworks.algashop.ordering.domain.valueobject.id.ProductId;
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
  @DisplayName("Should create order with test data builder")
  void shouldCreateOrderWithTestDataBuilder() {
    Order order = OrderTestDataBuilder.anOrder().build();

    assertThat(order).isNotNull();
    assertThat(order.id()).isNotNull();
    assertThat(order.customerId()).isNotNull();
    assertThat(order.totalAmount()).isNotNull();
    assertThat(order.totalItems()).isNotNull();
    assertThat(order.status()).isEqualTo(OrderStatus.DRAFT);
  }

  @Test
  @DisplayName("Should create order with no-arg OrderId constructor")
  void shouldCreateOrderWithNoArgOrderIdConstructor() {
    OrderId orderId = new OrderId();
    CustomerId customerId = new CustomerId(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"));
    Money totalAmount = Money.ZERO;
    Quantity totalItems = Quantity.ZERO;
    Set<OrderItem> items = new HashSet<>();

    Order order = new Order(orderId, customerId, totalAmount, totalItems, null, null, null, null, null, null,
        OrderStatus.DRAFT, null, null, null, items);

    assertThat(order.id()).isNotNull();
    assertThat(order.id().value()).isNotNull();
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

  @Test
  @DisplayName("Should add order item successfully")
  void shouldAddOrderItemSuccessfully() {
    var order = OrderTestDataBuilder.anOrder().customerId(new CustomerId(UUID.randomUUID())).build();
    var productId = new ProductId(UUID.randomUUID());
    var productName = new ProductName("Notebook Pro");
    var price = new Money("100.00");
    var quantity = new Quantity(new BigDecimal("2"));

    order.addOrderItem(productId, productName, price, quantity);

    assertThat(order.items()).hasSize(1);
    var orderItem = order.items().iterator().next();
    assertThat(orderItem.id()).isNotNull();
    assertThat(orderItem.orderId()).isEqualTo(order.id());
    assertThat(orderItem.productId()).isEqualTo(productId);
    assertThat(orderItem.productName()).isEqualTo(productName);
    assertThat(orderItem.price()).isEqualTo(price);
    assertThat(orderItem.quantity()).isEqualTo(quantity);
    assertThat(orderItem.totalAmount()).isEqualTo(new Money("200.00"));
  }

  @Test
  void shouldThrowExceptionWhenTryToChangeOrderItem() {
    var order = OrderTestDataBuilder.anOrder().customerId(new CustomerId(UUID.randomUUID())).build();
    var productId = new ProductId(UUID.randomUUID());
    var productName = new ProductName("Notebook Pro");
    var price = new Money("100.00");
    var quantity = new Quantity(new BigDecimal("2"));
    order.addOrderItem(productId, productName, price, quantity);

    assertThatThrownBy(() -> order.items().clear())
        .isInstanceOf(UnsupportedOperationException.class);
  }

  @Test
  @DisplayName("Should create draft order using draftOrderBuilder")
  void shouldCreateDraftOrderUsingDraftOrderBuilder() {
    var customerId = new CustomerId(UUID.randomUUID());

    Order order = Order.createDraftOrder(customerId);

    assertThat(order).isNotNull();
    assertThat(order.id()).isNotNull();
    assertThat(order.customerId()).isEqualTo(customerId);
    assertThat(order.totalAmount()).isEqualTo(Money.ZERO);
    assertThat(order.totalItems()).isEqualTo(Quantity.ZERO);
    assertThat(order.status()).isEqualTo(OrderStatus.DRAFT);
    assertThat(order.items()).isEmpty();
    assertThat(order.placedAt()).isNull();
    assertThat(order.paidAt()).isNull();
    assertThat(order.canceledAt()).isNull();
    assertThat(order.readyAt()).isNull();
  }

  @Test
  @DisplayName("Should add multiple order items and recalculate totals")
  void shouldAddMultipleOrderItemsAndRecalculateTotals() {
    var order = Order.createDraftOrder(new CustomerId(UUID.randomUUID()));

    order.addOrderItem(
        new ProductId(UUID.randomUUID()),
        new ProductName("Notebook Pro"),
        new Money("100.00"),
        new Quantity(new BigDecimal("2"))
    );

    order.addOrderItem(
        new ProductId(UUID.randomUUID()),
        new ProductName("Mouse Wireless"),
        new Money("50.00"),
        new Quantity(new BigDecimal("3"))
    );

    assertThat(order.items()).hasSize(2);
    assertThat(order.totalAmount()).isEqualTo(new Money("350.00"));
    assertThat(order.totalItems()).isEqualTo(new Quantity(new BigDecimal("5")));
  }

  @Test
  @DisplayName("Should recalculate total amount including shipping cost")
  void shouldRecalculateTotalAmountIncludingShippingCost() {
    var order = Order.createDraftOrder(new CustomerId(UUID.randomUUID()));

    order.addOrderItem(
        new ProductId(UUID.randomUUID()),
        new ProductName("Product A"),
        new Money("100.00"),
        new Quantity(new BigDecimal("1"))
    );

    order.addOrderItem(
        new ProductId(UUID.randomUUID()),
        new ProductName("Product B"),
        new Money("50.00"),
        new Quantity(new BigDecimal("1"))
    );

    order.recalculateTotalAmount();

    assertThat(order.totalAmount()).isEqualTo(new Money("150.00"));
  }

  @Test
  @DisplayName("Should recalculate total amount with shipping cost when items exist")
  void shouldRecalculateTotalAmountWithShippingCostWhenItemsExist() {
    var order = Order.existingOrderBuilder()
        .id(new OrderId())
        .customerId(new CustomerId(UUID.randomUUID()))
        .totalAmount(Money.ZERO)
        .totalItems(Quantity.ZERO)
        .status(OrderStatus.DRAFT)
        .items(new HashSet<>())
        .shippingCost(new Money("15.00"))
        .build();

    order.addOrderItem(
        new ProductId(UUID.randomUUID()),
        new ProductName("Product A"),
        new Money("100.00"),
        new Quantity(new BigDecimal("1"))
    );

    assertThat(order.totalAmount()).isEqualTo(new Money("115.00"));
  }

  @Test
  @DisplayName("Should update total items when adding order item")
  void shouldUpdateTotalItemsWhenAddingOrderItem() {
    var order = Order.createDraftOrder(new CustomerId(UUID.randomUUID()));

    assertThat(order.totalItems()).isEqualTo(Quantity.ZERO);

    order.addOrderItem(
        new ProductId(UUID.randomUUID()),
        new ProductName("Product A"),
        new Money("100.00"),
        new Quantity(new BigDecimal("3"))
    );

    assertThat(order.totalItems()).isEqualTo(new Quantity(new BigDecimal("3")));
  }

  @Test
  @DisplayName("Should use shipping cost as ZERO when null during recalculation")
  void shouldUseShippingCostAsZeroWhenNullDuringRecalculation() {
    var order = Order.existingOrderBuilder()
        .id(new OrderId())
        .customerId(new CustomerId(UUID.randomUUID()))
        .totalAmount(Money.ZERO)
        .totalItems(Quantity.ZERO)
        .status(OrderStatus.DRAFT)
        .items(new HashSet<>())
        .shippingCost(null)
        .build();

    order.addOrderItem(
        new ProductId(UUID.randomUUID()),
        new ProductName("Product A"),
        new Money("75.00"),
        new Quantity(new BigDecimal("2"))
    );

    assertThat(order.shippingCost()).isEqualTo(Money.ZERO);
    assertThat(order.totalAmount()).isEqualTo(new Money("150.00"));
  }

  @Test
  @DisplayName("Should return billingInfo")
  void shouldReturnBillingInfo() {
    var fullName = new com.algaworks.algashop.ordering.domain.valueobject.FullName("John", "Doe");
    var document = new com.algaworks.algashop.ordering.domain.valueobject.Document("12345678900");
    var phone = new com.algaworks.algashop.ordering.domain.valueobject.Phone("11999999999");
    var zipCode = new com.algaworks.algashop.ordering.domain.valueobject.ZipCode("12345-678");
    var address = new com.algaworks.algashop.ordering.domain.valueobject.Address("Main Street", "123", "Apt 1", "Downtown", "New York", "NY", zipCode);
    var billingInfo = com.algaworks.algashop.ordering.domain.valueobject.BillingInfo.builder()
        .fullName(fullName)
        .document(document)
        .phone(phone)
        .address(address)
        .build();
    var order = OrderTestDataBuilder.anOrder().billingInfo(billingInfo).build();

    assertThat(order.billingInfo()).isEqualTo(billingInfo);
  }

  @Test
  @DisplayName("Should return shippingInfo")
  void shouldReturnShippingInfo() {
    var fullName = new com.algaworks.algashop.ordering.domain.valueobject.FullName("John", "Doe");
    var document = new com.algaworks.algashop.ordering.domain.valueobject.Document("12345678900");
    var phone = new com.algaworks.algashop.ordering.domain.valueobject.Phone("11999999999");
    var zipCode = new com.algaworks.algashop.ordering.domain.valueobject.ZipCode("12345-678");
    var address = new com.algaworks.algashop.ordering.domain.valueobject.Address("Main Street", "123", "Apt 1", "Downtown", "New York", "NY", zipCode);
    var shippingInfo = com.algaworks.algashop.ordering.domain.valueobject.ShippingInfo.builder()
        .fullName(fullName)
        .document(document)
        .phone(phone)
        .address(address)
        .build();
    var order = OrderTestDataBuilder.anOrder().shippingInfo(shippingInfo).build();

    assertThat(order.shippingInfo()).isEqualTo(shippingInfo);
  }

  @Test
  @DisplayName("Should recalculate total amount when items is empty")
  void shouldRecalculateTotalAmountWhenItemsIsEmpty() {
    var order = Order.existingOrderBuilder()
        .id(new OrderId())
        .customerId(new CustomerId(UUID.randomUUID()))
        .totalAmount(Money.ZERO)
        .totalItems(Quantity.ZERO)
        .status(OrderStatus.DRAFT)
        .items(new HashSet<>())
        .shippingCost(new Money("10.00"))
        .build();

    order.recalculateTotalAmount();

    assertThat(order.totalAmount()).isEqualTo(new Money("10.00"));
  }

  @Test
  @DisplayName("Should recalculate total items when items is empty")
  void shouldRecalculateTotalItemsWhenItemsIsEmpty() {
    var order = Order.existingOrderBuilder()
        .id(new OrderId())
        .customerId(new CustomerId(UUID.randomUUID()))
        .totalAmount(Money.ZERO)
        .totalItems(Quantity.ZERO)
        .status(OrderStatus.DRAFT)
        .items(new HashSet<>())
        .build();

    order.addOrderItem(
        new ProductId(UUID.randomUUID()),
        new ProductName("Product A"),
        new Money("100.00"),
        new Quantity(new BigDecimal("5"))
    );

    assertThat(order.totalItems()).isEqualTo(new Quantity(new BigDecimal("5")));
  }

  @Test
  @DisplayName("Should add order item to existing order with items")
  void shouldAddOrderItemToExistingOrderWithItems() {
    var order = Order.createDraftOrder(new CustomerId(UUID.randomUUID()));

    order.addOrderItem(
        new ProductId(UUID.randomUUID()),
        new ProductName("Product A"),
        new Money("100.00"),
        new Quantity(new BigDecimal("2"))
    );

    order.addOrderItem(
        new ProductId(UUID.randomUUID()),
        new ProductName("Product B"),
        new Money("50.00"),
        new Quantity(new BigDecimal("3"))
    );

    assertThat(order.items()).hasSize(2);
    assertThat(order.totalAmount()).isEqualTo(new Money("350.00"));
    assertThat(order.totalItems()).isEqualTo(new Quantity(new BigDecimal("5")));
  }

  @Test
  @DisplayName("Should be equal to itself")
  void shouldBeEqualToItself() {
    var order = OrderTestDataBuilder.anOrder().build();

    assertThat(order).isEqualTo(order);
  }

  @Test
  @DisplayName("Should have consistent hashCode")
  void shouldHaveConsistentHashCode() {
    var order = OrderTestDataBuilder.anOrder().build();

    var hashCode1 = order.hashCode();
    var hashCode2 = order.hashCode();

    assertThat(hashCode1).isEqualTo(hashCode2);
  }

  @Test
  @DisplayName("Should recalculate total amount when shipping cost is set after items")
  void shouldRecalculateTotalAmountWhenShippingCostIsSetAfterItems() {
    var order = Order.createDraftOrder(new CustomerId(UUID.randomUUID()));

    order.addOrderItem(
        new ProductId(UUID.randomUUID()),
        new ProductName("Product A"),
        new Money("100.00"),
        new Quantity(new BigDecimal("1"))
    );

    var orderWithShipping = Order.existingOrderBuilder()
        .id(order.id())
        .customerId(order.customerId())
        .totalAmount(order.totalAmount())
        .totalItems(order.totalItems())
        .placedAt(order.placedAt())
        .paidAt(order.paidAt())
        .canceledAt(order.canceledAt())
        .readyAt(order.readyAt())
        .billingInfo(order.billingInfo())
        .shippingInfo(order.shippingInfo())
        .status(order.status())
        .paymentMethod(order.paymentMethod())
        .shippingCost(new Money("20.00"))
        .expectedDeliveryDate(order.expectedDeliveryDate())
        .items(order.items())
        .build();

    orderWithShipping.recalculateTotalAmount();

    assertThat(orderWithShipping.totalAmount()).isEqualTo(new Money("120.00"));
  }

  @Test
  @DisplayName("Should throw exception when createDraftOrder customerId is null")
  void shouldThrowExceptionWhenCreateDraftOrderCustomerIdIsNull() {
    assertThatThrownBy(() -> Order.createDraftOrder(null))
        .isInstanceOf(NullPointerException.class);
  }
}
