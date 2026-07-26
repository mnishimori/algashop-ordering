package com.algaworks.algashop.ordering.infrastructure.persistence.assembler;

import static org.assertj.core.api.Assertions.assertThat;

import com.algaworks.algashop.ordering.domain.entity.OrderTestDataBuilder;
import com.algaworks.algashop.ordering.domain.entity.ProductTestDataBuilder;
import com.algaworks.algashop.ordering.domain.model.entity.Order;
import com.algaworks.algashop.ordering.domain.model.entity.OrderItem;
import com.algaworks.algashop.ordering.domain.model.entity.OrderStatus;
import com.algaworks.algashop.ordering.domain.model.entity.PaymentMethod;
import com.algaworks.algashop.ordering.domain.model.valueobject.Money;
import com.algaworks.algashop.ordering.domain.model.valueobject.ProductName;
import com.algaworks.algashop.ordering.domain.model.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderId;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntityTestDataBuilder;
import io.hypersistence.tsid.TSID;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderPersistenceEntityAssemblerTest {

  private final OrderPersistenceEntityAssembler assembler = new OrderPersistenceEntityAssembler();

  @Test
  @DisplayName("Should create OrderPersistenceEntity from Order using fromDomain")
  void shouldCreateOrderPersistenceEntityFromOrderUsingFromDomain() {
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
    Set<OrderItem> items = new LinkedHashSet<>();

    Order order = Order.existingOrderBuilder()
        .id(orderId)
        .customerId(customerId)
        .totalAmount(totalAmount)
        .totalItems(totalItems)
        .placedAt(placedAt)
        .paidAt(paidAt)
        .canceledAt(canceledAt)
        .readyAt(readyAt)
        .status(status)
        .paymentMethod(paymentMethod)
        .items(items)
        .build();

    OrderPersistenceEntity entity = assembler.fromDomain(order);

    assertThat(entity.getId()).isEqualTo(123456789L);
    assertThat(entity.getCustomerId()).isEqualTo(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"));
    assertThat(entity.getTotalAmount()).isEqualByComparingTo("100.00");
    assertThat(entity.getTotalItems()).isEqualTo(5);
    assertThat(entity.getStatus()).isEqualTo("PLACED");
    assertThat(entity.getPaymentMethod()).isEqualTo("CREDIT_CARD");
    assertThat(entity.getPlacedAt()).isEqualTo(placedAt);
    assertThat(entity.getPaidAt()).isEqualTo(paidAt);
    assertThat(entity.getCanceledAt()).isNull();
    assertThat(entity.getReadyAt()).isNull();
  }

  @Test
  @DisplayName("Should merge Order data into existing OrderPersistenceEntity")
  void shouldMergeOrderDataIntoExistingOrderPersistenceEntity() {
    OrderPersistenceEntity existingEntity = new OrderPersistenceEntity();
    existingEntity.setId(999L);
    existingEntity.setCustomerId(UUID.fromString("99999999-9999-9999-9999-999999999999"));

    OrderId orderId = new OrderId(TSID.from(123456789L));
    CustomerId customerId = new CustomerId(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"));
    Money totalAmount = new Money("250.50");
    Quantity totalItems = new Quantity(new BigDecimal(10));
    OffsetDateTime placedAt = OffsetDateTime.now();
    OffsetDateTime paidAt = OffsetDateTime.now();
    OffsetDateTime canceledAt = OffsetDateTime.now();
    OffsetDateTime readyAt = OffsetDateTime.now();
    OrderStatus status = OrderStatus.PAID;
    PaymentMethod paymentMethod = PaymentMethod.GATEWAY_BALANCE;
    Set<OrderItem> items = new LinkedHashSet<>();

    Order order = Order.existingOrderBuilder()
        .id(orderId)
        .customerId(customerId)
        .totalAmount(totalAmount)
        .totalItems(totalItems)
        .placedAt(placedAt)
        .paidAt(paidAt)
        .canceledAt(canceledAt)
        .readyAt(readyAt)
        .status(status)
        .paymentMethod(paymentMethod)
        .items(items)
        .build();

    OrderPersistenceEntity result = assembler.merge(existingEntity, order);

    assertThat(result).isSameAs(existingEntity);
    assertThat(result.getId()).isEqualTo(123456789L);
    assertThat(result.getCustomerId()).isEqualTo(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"));
    assertThat(result.getTotalAmount()).isEqualByComparingTo("250.50");
    assertThat(result.getTotalItems()).isEqualTo(10);
    assertThat(result.getStatus()).isEqualTo("PAID");
    assertThat(result.getPaymentMethod()).isEqualTo("GATEWAY_BALANCE");
    assertThat(result.getPlacedAt()).isEqualTo(placedAt);
    assertThat(result.getPaidAt()).isEqualTo(paidAt);
    assertThat(result.getCanceledAt()).isEqualTo(canceledAt);
    assertThat(result.getReadyAt()).isEqualTo(readyAt);
  }

  @Test
  @DisplayName("Should handle null date fields in merge")
  void shouldHandleNullDateFieldsInMerge() {
    OrderPersistenceEntity existingEntity = new OrderPersistenceEntity();

    OrderId orderId = new OrderId(TSID.from(123456789L));
    CustomerId customerId = new CustomerId(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"));
    Money totalAmount = new Money("100.00");
    Quantity totalItems = new Quantity(new BigDecimal(5));
    OrderStatus status = OrderStatus.DRAFT;
    Set<OrderItem> items = new LinkedHashSet<>();

    Order order = Order.existingOrderBuilder()
        .id(orderId)
        .customerId(customerId)
        .totalAmount(totalAmount)
        .totalItems(totalItems)
        .placedAt(null)
        .paidAt(null)
        .canceledAt(null)
        .readyAt(null)
        .status(status)
        .paymentMethod(null)
        .items(items)
        .build();

    OrderPersistenceEntity result = assembler.merge(existingEntity, order);

    assertThat(result.getId()).isEqualTo(123456789L);
    assertThat(result.getCustomerId()).isEqualTo(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"));
    assertThat(result.getTotalAmount()).isEqualByComparingTo("100.00");
    assertThat(result.getTotalItems()).isEqualTo(5);
    assertThat(result.getStatus()).isEqualTo("DRAFT");
    assertThat(result.getPaymentMethod()).isNull();
    assertThat(result.getPlacedAt()).isNull();
    assertThat(result.getPaidAt()).isNull();
    assertThat(result.getCanceledAt()).isNull();
    assertThat(result.getReadyAt()).isNull();
  }

  @Test
  @DisplayName("Should handle all OrderStatus enum values")
  void shouldHandleAllOrderStatusEnumValues() {
    OrderPersistenceEntity existingEntity = new OrderPersistenceEntity();

    OrderId orderId = new OrderId(TSID.from(123456789L));
    CustomerId customerId = new CustomerId(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"));
    Money totalAmount = new Money("100.00");
    Quantity totalItems = new Quantity(new BigDecimal(5));
    Set<OrderItem> items = new LinkedHashSet<>();

    for (OrderStatus status : OrderStatus.values()) {
      Order order = Order.existingOrderBuilder()
          .id(orderId)
          .customerId(customerId)
          .totalAmount(totalAmount)
          .totalItems(totalItems)
          .status(status)
          .paymentMethod(PaymentMethod.CREDIT_CARD)
          .items(items)
          .build();

      OrderPersistenceEntity result = assembler.merge(existingEntity, order);

      assertThat(result.getStatus()).isEqualTo(status.name());
    }
  }

  @Test
  @DisplayName("Should handle all PaymentMethod enum values")
  void shouldHandleAllPaymentMethodEnumValues() {
    OrderPersistenceEntity existingEntity = new OrderPersistenceEntity();

    OrderId orderId = new OrderId(TSID.from(123456789L));
    CustomerId customerId = new CustomerId(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"));
    Money totalAmount = new Money("100.00");
    Quantity totalItems = new Quantity(new BigDecimal(5));
    Set<OrderItem> items = new LinkedHashSet<>();

    for (PaymentMethod paymentMethod : PaymentMethod.values()) {
      Order order = Order.existingOrderBuilder()
          .id(orderId)
          .customerId(customerId)
          .totalAmount(totalAmount)
          .totalItems(totalItems)
          .status(OrderStatus.PLACED)
          .paymentMethod(paymentMethod)
          .items(items)
          .build();

      OrderPersistenceEntity result = assembler.merge(existingEntity, order);

      assertThat(result.getPaymentMethod()).isEqualTo(paymentMethod.name());
    }
  }

  @Test
  @DisplayName("Should convert BigDecimal totalItems to Integer correctly")
  void shouldConvertBigDecimalTotalItemsToIntegerCorrectly() {
    OrderPersistenceEntity existingEntity = new OrderPersistenceEntity();

    OrderId orderId = new OrderId(TSID.from(123456789L));
    CustomerId customerId = new CustomerId(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"));
    Money totalAmount = new Money("100.00");
    Quantity totalItems = new Quantity(new BigDecimal("7"));
    Set<OrderItem> items = new LinkedHashSet<>();

    Order order = Order.existingOrderBuilder()
        .id(orderId)
        .customerId(customerId)
        .totalAmount(totalAmount)
        .totalItems(totalItems)
        .status(OrderStatus.PLACED)
        .paymentMethod(PaymentMethod.CREDIT_CARD)
        .items(items)
        .build();

    OrderPersistenceEntity result = assembler.merge(existingEntity, order);

    assertThat(result.getTotalItems()).isEqualTo(7);
  }

  @Test
  @DisplayName("Should return the same entity instance from merge")
  void shouldReturnSameEntityInstanceFromMerge() {
    OrderPersistenceEntity existingEntity = new OrderPersistenceEntity();

    OrderId orderId = new OrderId(TSID.from(123456789L));
    CustomerId customerId = new CustomerId(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"));
    Money totalAmount = new Money("100.00");
    Quantity totalItems = new Quantity(new BigDecimal(5));
    Set<OrderItem> items = new LinkedHashSet<>();

    Order order = Order.existingOrderBuilder()
        .id(orderId)
        .customerId(customerId)
        .totalAmount(totalAmount)
        .totalItems(totalItems)
        .status(OrderStatus.PLACED)
        .paymentMethod(PaymentMethod.CREDIT_CARD)
        .items(items)
        .build();

    OrderPersistenceEntity result = assembler.merge(existingEntity, order);

    assertThat(result).isSameAs(existingEntity);
  }

  @Test
  @DisplayName("Should merge Order with no items into OrderPersistenceEntity with items")
  void shouldMergeOrderWithNoItemsIntoOrderPersistenceEntityWithItems() {
    var order = OrderTestDataBuilder.anOrder().build();
    var orderPersistenceEntity = OrderPersistenceEntityTestDataBuilder.existingOrder().build();

    var result = assembler.merge(orderPersistenceEntity, order);

    assertThat(result).isNotNull();
    assertThat(result.getId()).isNotNull();
    assertThat(result.getItems()).isEmpty();
  }

  @Test
  @DisplayName("Should merge Order with items into OrderPersistenceEntity with no items")
  void shouldMergeOrderWithItemsIntoOrderPersistenceEntityWithNoItems() {
    var order = OrderTestDataBuilder.anOrder().withItems(true).build();
    order.addOrderItem(
        ProductTestDataBuilder.createProduct(new ProductName("Product 1"), new Money("100.00"), true).build(),
        new Quantity(new BigDecimal(1)));
    order.addOrderItem(
        ProductTestDataBuilder.createProduct(new ProductName("Product 2"), new Money("50.00"), true).build(),
        new Quantity(new BigDecimal(1)));
    var orderPersistenceEntity = OrderPersistenceEntityTestDataBuilder.existingOrder().items(new HashSet<>()).build();

    var result = assembler.merge(orderPersistenceEntity, order);

    assertThat(result).isNotNull();
    assertThat(result.getId()).isNotNull();
    assertThat(result.getItems()).isNotNull().isNotEmpty().hasSize(2);
  }

  @Test
  @DisplayName("Should merge Order with items into OrderPersistenceEntity with items")
  void shouldMergeOrderWithItemsIntoOrderPersistenceEntityWithItems() {
    var order = OrderTestDataBuilder.anOrder().withItems(true).build();
    order.addOrderItem(
        ProductTestDataBuilder.createProduct(new ProductName("Product 1"), new Money("500.00"), true).build(),
        new Quantity(new BigDecimal(2)));
    order.addOrderItem(
        ProductTestDataBuilder.createProduct(new ProductName("Product 2"), new Money("250.00"), true).build(),
        new Quantity(new BigDecimal(1)));
    var orderItems = order.items().stream().map(assembler::fromDomain).collect(Collectors.toSet());
    var orderPersistenceEntity = OrderPersistenceEntityTestDataBuilder.existingOrder()
        .build();
    orderPersistenceEntity.setItems(orderItems);
    var orderItem = order.items().iterator().next();
    order.removeItem(orderItem.id());

    var result = assembler.merge(orderPersistenceEntity, order);

    assertThat(result).isNotNull();
    assertThat(result.getId()).isNotNull();
    assertThat(result.getItems()).isNotNull().isNotEmpty().hasSize(1);
  }
}
