package com.algaworks.algashop.ordering.infrastructure.persistence.provider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

import com.algaworks.algashop.ordering.IntegrationTest;
import com.algaworks.algashop.ordering.domain.entity.OrderItemTestDataBuilder;
import com.algaworks.algashop.ordering.domain.entity.OrderTestDataBuilder;
import com.algaworks.algashop.ordering.domain.model.entity.OrderStatus;
import com.algaworks.algashop.ordering.domain.model.valueobject.Money;
import com.algaworks.algashop.ordering.domain.model.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderItemId;
import com.algaworks.algashop.ordering.infrastructure.persistence.assembler.OrderPersistenceEntityAssembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.config.SpringDataAuditingConfig;
import com.algaworks.algashop.ordering.infrastructure.persistence.disassembler.OrderPersistenceEntityDisassembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.CustomerPersistenceEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.repository.CustomerPersistenceEntityRepository;
import com.algaworks.algashop.ordering.infrastructure.persistence.repository.OrderPersistenceEntityRepository;
import io.hypersistence.tsid.TSID;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.Year;
import java.time.ZoneOffset;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@IntegrationTest
@Import({OrdersPersistenceProvider.class, OrderPersistenceEntityDisassembler.class,
    OrderPersistenceEntityAssembler.class, SpringDataAuditingConfig.class})
class OrdersPersistenceProviderIntegrationTest {

  private final OrdersPersistenceProvider persistenceProvider;
  private final OrderPersistenceEntityRepository repository;
  private final CustomerPersistenceEntityRepository customerRepository;

  @Autowired
  OrdersPersistenceProviderIntegrationTest(OrdersPersistenceProvider persistenceProvider,
      OrderPersistenceEntityRepository repository,
      CustomerPersistenceEntityRepository customerRepository) {
    this.persistenceProvider = persistenceProvider;
    this.repository = repository;
    this.customerRepository = customerRepository;
  }

  private void createCustomer(UUID customerId) {
    if (!customerRepository.existsById(customerId)) {
      var customer = new CustomerPersistenceEntity();
      customer.setId(customerId);
      customer.setFirstName("Test");
      customer.setLastName("Customer");
      customer.setEmail("test@example.com");
      customerRepository.saveAndFlush(customer);
    }
  }

  @Test
  void shouldUpdateAndKeepPersistenceEntityState() {
    var orderId = new OrderId(TSID.from(123456789L));
    var customerId = new CustomerId(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"));
    createCustomer(customerId.value());
    var order = OrderTestDataBuilder.anOrder()
        .id(orderId)
        .customerId(customerId)
        .status(OrderStatus.PLACED)
        .items(Set.of(
            OrderItemTestDataBuilder.anOrderItem()
                .id(new OrderItemId(TSID.from(111111111L)))
                .orderId(orderId)
                .build(),
            OrderItemTestDataBuilder.anOrderItem()
                .id(new OrderItemId(TSID.from(222222222L)))
                .orderId(orderId)
                .build()
        ))
        .build();

    persistenceProvider.add(order);

    var orderPersistenceEntity = repository.findById(orderId.value().toLong()).orElseThrow();
    assertThat(orderPersistenceEntity).isNotNull();
    assertThat(orderPersistenceEntity.getStatus()).isNotNull();
    assertThat(orderPersistenceEntity.getStatus()).isEqualTo(order.status().name());
    assertThat(orderPersistenceEntity.getCreatedByUserId()).isNotNull();
    assertThat(orderPersistenceEntity.getLastModifiedAt()).isNotNull();
    assertThat(orderPersistenceEntity.getLastModifiedByUserId()).isNotNull();
    assertThat(orderPersistenceEntity.getItems()).isNotNull().isNotEmpty();
    assertThat(orderPersistenceEntity.getItems()).hasSize(2);

    order = persistenceProvider.findById(orderId).orElseThrow();
    order.markAsPaid();
    persistenceProvider.add(order);

    orderPersistenceEntity = repository.findById(orderId.value().toLong()).orElseThrow();
    assertThat(orderPersistenceEntity).isNotNull();
    assertThat(orderPersistenceEntity.getStatus()).isNotNull();
    assertThat(orderPersistenceEntity.getStatus()).isEqualTo(order.status().name());
    assertThat(orderPersistenceEntity.getCreatedByUserId()).isNotNull();
    assertThat(orderPersistenceEntity.getLastModifiedAt()).isNotNull();
    assertThat(orderPersistenceEntity.getLastModifiedByUserId()).isNotNull();
    assertThat(orderPersistenceEntity.getItems()).isNotNull();
    assertThat(orderPersistenceEntity.getItems()).hasSize(2);
  }

  @Test
  void shouldReturnTrueWhenOrderExists() {
    var orderId = new OrderId(TSID.from(123456789L));
    var customerId = new CustomerId(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"));
    createCustomer(customerId.value());
    var order = OrderTestDataBuilder.anOrder()
        .id(orderId)
        .customerId(customerId)
        .status(OrderStatus.PLACED)
        .items(Set.of(
            OrderItemTestDataBuilder.anOrderItem()
                .id(new OrderItemId(TSID.from(111111111L)))
                .orderId(orderId)
                .build(),
            OrderItemTestDataBuilder.anOrderItem()
                .id(new OrderItemId(TSID.from(222222222L)))
                .orderId(orderId)
                .build()
        ))
        .build();
    persistenceProvider.add(order);

    var orderExists = persistenceProvider.existsById(orderId);

    assertThat(orderExists).isTrue();
  }

  @Test
  void shouldReturnFalseWhenOrderExists() {
    var orderId = new OrderId(TSID.from(123456789L));

    var orderExists = persistenceProvider.existsById(orderId);

    assertThat(orderExists).isFalse();
  }

  @Test
  void shouldReturnQuantityOneWhenOrderExists() {
    var orderId = new OrderId(TSID.from(123456789L));
    var customerId = new CustomerId(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"));
    createCustomer(customerId.value());
    var order = OrderTestDataBuilder.anOrder()
        .id(orderId)
        .customerId(customerId)
        .status(OrderStatus.PLACED)
        .items(Set.of(
            OrderItemTestDataBuilder.anOrderItem()
                .id(new OrderItemId(TSID.from(111111111L)))
                .orderId(orderId)
                .build(),
            OrderItemTestDataBuilder.anOrderItem()
                .id(new OrderItemId(TSID.from(222222222L)))
                .orderId(orderId)
                .build()
        ))
        .build();
    persistenceProvider.add(order);

    var quantity = persistenceProvider.count();

    assertThat(quantity).isEqualTo(1);
  }

  @Test
  void shouldReturnQuantityZeroWhenOrderExists() {
    var quantity = persistenceProvider.count();

    assertThat(quantity).isEqualTo(0);
  }

  @Test
  void shouldAddOrderAndNotThrowLazyInitializationException() {
    var orderId = new OrderId(TSID.from(123456789L));
    var customerId = new CustomerId(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"));
    createCustomer(customerId.value());
    var order = OrderTestDataBuilder.anOrder()
        .id(orderId)
        .customerId(customerId)
        .status(OrderStatus.PLACED)
        .items(Set.of(
            OrderItemTestDataBuilder.anOrderItem()
                .id(new OrderItemId(TSID.from(111111111L)))
                .orderId(orderId)
                .build(),
            OrderItemTestDataBuilder.anOrderItem()
                .id(new OrderItemId(TSID.from(222222222L)))
                .orderId(orderId)
                .build()
        ))
        .build();
    persistenceProvider.add(order);

    assertThatNoException().isThrownBy(() -> persistenceProvider.findById(orderId));
  }

  @Test
  void shouldReturnOrdersPlacedByCustomerInYear() {
    var customerId = new CustomerId(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"));
    var year = Year.of(2024);
    var orderId1 = new OrderId(TSID.from(123456789L));
    var orderId2 = new OrderId(TSID.from(987654321L));
    var placedAt1 = OffsetDateTime.of(2024, 6, 15, 10, 0, 0, 0, ZoneOffset.UTC);
    var placedAt2 = OffsetDateTime.of(2024, 12, 1, 14, 30, 0, 0, ZoneOffset.UTC);

    createCustomer(customerId.value());

    var order1 = OrderTestDataBuilder.anOrder()
        .id(orderId1)
        .customerId(customerId)
        .status(OrderStatus.PLACED)
        .placedAt(placedAt1)
        .items(Set.of(
            OrderItemTestDataBuilder.anOrderItem()
                .id(new OrderItemId(TSID.from(111111111L)))
                .orderId(orderId1)
                .build()
        ))
        .build();

    var order2 = OrderTestDataBuilder.anOrder()
        .id(orderId2)
        .customerId(customerId)
        .status(OrderStatus.PLACED)
        .placedAt(placedAt2)
        .items(Set.of(
            OrderItemTestDataBuilder.anOrderItem()
                .id(new OrderItemId(TSID.from(222222222L)))
                .orderId(orderId2)
                .build()
        ))
        .build();

    persistenceProvider.add(order1);
    persistenceProvider.add(order2);

    var orders = persistenceProvider.placedByCustomerInYear(customerId, year);

    assertThat(orders).hasSize(2);
    assertThat(orders).extracting("id").containsExactlyInAnyOrder(orderId1, orderId2);
  }

  @Test
  void shouldReturnSalesQuantityByCustomerInYear() {
    var customerId = new CustomerId(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"));
    var year = Year.of(2024);
    var orderId1 = new OrderId(TSID.from(123456789L));
    var orderId2 = new OrderId(TSID.from(987654321L));

    createCustomer(customerId.value());

    var order1 = OrderTestDataBuilder.anOrder()
        .id(orderId1)
        .customerId(customerId)
        .totalItems(new Quantity(BigDecimal.ONE))
        .status(OrderStatus.PLACED)
        .placedAt(OffsetDateTime.of(2024, 6, 15, 10, 0, 0, 0, ZoneOffset.UTC))
        .paidAt(OffsetDateTime.of(2024, 6, 16, 10, 0, 0, 0, ZoneOffset.UTC))
        .items(Set.of(
            OrderItemTestDataBuilder.anOrderItem()
                .id(new OrderItemId(TSID.from(111111111L)))
                .orderId(orderId1)
                .quantity(new Quantity(BigDecimal.ONE))
                .build()
        ))
        .build();

    var order2 = OrderTestDataBuilder.anOrder()
        .id(orderId2)
        .customerId(customerId)
        .totalItems(new Quantity(BigDecimal.ONE))
        .status(OrderStatus.PLACED)
        .placedAt(OffsetDateTime.of(2024, 12, 1, 14, 30, 0, 0, ZoneOffset.UTC))
        .paidAt(OffsetDateTime.of(2024, 12, 2, 14, 30, 0, 0, ZoneOffset.UTC))
        .items(Set.of(
            OrderItemTestDataBuilder.anOrderItem()
                .id(new OrderItemId(TSID.from(222222222L)))
                .orderId(orderId2)
                .quantity(new Quantity(BigDecimal.ONE))
                .build()
        ))
        .build();

    persistenceProvider.add(order1);
    persistenceProvider.add(order2);

    var quantity = persistenceProvider.salesQuantityByCustomerInYear(customerId, year);

    assertThat(quantity).isEqualTo(2L);
  }

  @Test
  void shouldReturnZeroSalesQuantityWhenCustomerHasNoOrdersInYear() {
    var customerId = new CustomerId(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"));
    var year = Year.of(2024);

    createCustomer(customerId.value());

    var quantity = persistenceProvider.salesQuantityByCustomerInYear(customerId, year);

    assertThat(quantity).isEqualTo(0L);
  }

  @Test
  void shouldReturnTotalSoldForCustomer() {
    var customerId = new CustomerId(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"));
    var orderId1 = new OrderId(TSID.from(123456789L));
    var orderId2 = new OrderId(TSID.from(987654321L));

    createCustomer(customerId.value());

    var order1 = OrderTestDataBuilder.anOrder()
        .id(orderId1)
        .customerId(customerId)
        .status(OrderStatus.PLACED)
        .placedAt(OffsetDateTime.of(2024, 6, 15, 10, 0, 0, 0, ZoneOffset.UTC))
        .paidAt(OffsetDateTime.of(2024, 6, 16, 10, 0, 0, 0, ZoneOffset.UTC))
        .items(Set.of(
            OrderItemTestDataBuilder.anOrderItem()
                .id(new OrderItemId(TSID.from(111111111L)))
                .orderId(orderId1)
                .build()
        ))
        .build();
    order1.recalculateTotalAmount();

    var order2 = OrderTestDataBuilder.anOrder()
        .id(orderId2)
        .customerId(customerId)
        .status(OrderStatus.PLACED)
        .placedAt(OffsetDateTime.of(2024, 12, 1, 14, 30, 0, 0, ZoneOffset.UTC))
        .paidAt(OffsetDateTime.of(2024, 12, 2, 14, 30, 0, 0, ZoneOffset.UTC))
        .items(Set.of(
            OrderItemTestDataBuilder.anOrderItem()
                .id(new OrderItemId(TSID.from(222222222L)))
                .orderId(orderId2)
                .build()
        ))
        .build();
    order2.recalculateTotalAmount();

    persistenceProvider.add(order1);
    persistenceProvider.add(order2);

    var totalSold = persistenceProvider.totalSoldForCustomer(customerId);

    assertThat(totalSold).isEqualTo(order1.totalAmount().add(order2.totalAmount()));
  }

  @Test
  void shouldReturnZeroTotalSoldWhenCustomerHasNoOrders() {
    var customerId = new CustomerId(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"));

    createCustomer(customerId.value());

    var totalSold = persistenceProvider.totalSoldForCustomer(customerId);

    assertThat(totalSold).isEqualTo(Money.ZERO);
  }

  @Test
  void shouldNotReturnOrdersFromDifferentYear() {
    var customerId = new CustomerId(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"));
    var year = Year.of(2024);
    var orderId1 = new OrderId(TSID.from(123456789L));
    var orderId2 = new OrderId(TSID.from(987654321L));
    var placedAt2024 = OffsetDateTime.of(2024, 6, 15, 10, 0, 0, 0, ZoneOffset.UTC);
    var placedAt2023 = OffsetDateTime.of(2023, 12, 1, 14, 30, 0, 0, ZoneOffset.UTC);

    createCustomer(customerId.value());

    var order2024 = OrderTestDataBuilder.anOrder()
        .id(orderId1)
        .customerId(customerId)
        .status(OrderStatus.PLACED)
        .placedAt(placedAt2024)
        .items(Set.of(
            OrderItemTestDataBuilder.anOrderItem()
                .id(new OrderItemId(TSID.from(111111111L)))
                .orderId(orderId1)
                .build()
        ))
        .build();

    var order2023 = OrderTestDataBuilder.anOrder()
        .id(orderId2)
        .customerId(customerId)
        .status(OrderStatus.PLACED)
        .placedAt(placedAt2023)
        .items(Set.of(
            OrderItemTestDataBuilder.anOrderItem()
                .id(new OrderItemId(TSID.from(222222222L)))
                .orderId(orderId2)
                .build()
        ))
        .build();

    persistenceProvider.add(order2024);
    persistenceProvider.add(order2023);

    var orders = persistenceProvider.placedByCustomerInYear(customerId, year);

    assertThat(orders).hasSize(1);
    assertThat(orders.get(0).id()).isEqualTo(orderId1);
  }

  @Test
  void shouldNotReturnOrdersFromDifferentCustomer() {
    var customerId1 = new CustomerId(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"));
    var customerId2 = new CustomerId(UUID.fromString("660e8400-e29b-41d4-a716-446655440001"));
    var year = Year.of(2024);
    var orderId1 = new OrderId(TSID.from(123456789L));
    var orderId2 = new OrderId(TSID.from(987654321L));
    var placedAt = OffsetDateTime.of(2024, 6, 15, 10, 0, 0, 0, ZoneOffset.UTC);

    createCustomer(customerId1.value());
    createCustomer(customerId2.value());

    var order1 = OrderTestDataBuilder.anOrder()
        .id(orderId1)
        .customerId(customerId1)
        .status(OrderStatus.PLACED)
        .placedAt(placedAt)
        .items(Set.of(
            OrderItemTestDataBuilder.anOrderItem()
                .id(new OrderItemId(TSID.from(111111111L)))
                .orderId(orderId1)
                .build()
        ))
        .build();

    var order2 = OrderTestDataBuilder.anOrder()
        .id(orderId2)
        .customerId(customerId2)
        .status(OrderStatus.PLACED)
        .placedAt(placedAt)
        .items(Set.of(
            OrderItemTestDataBuilder.anOrderItem()
                .id(new OrderItemId(TSID.from(222222222L)))
                .orderId(orderId2)
                .build()
        ))
        .build();

    persistenceProvider.add(order1);
    persistenceProvider.add(order2);

    var orders = persistenceProvider.placedByCustomerInYear(customerId1, year);

    assertThat(orders).hasSize(1);
    assertThat(orders.get(0).id()).isEqualTo(orderId1);
  }

  @Test
  void shouldReturnEmptyListWhenNoOrdersForCustomerInYear() {
    var customerId = new CustomerId(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"));
    var year = Year.of(2024);

    var orders = persistenceProvider.placedByCustomerInYear(customerId, year);

    assertThat(orders).isEmpty();
  }

  @Test
  void shouldReturnOrdersAtYearBoundaries() {
    var customerId = new CustomerId(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"));
    var year = Year.of(2024);
    var orderId1 = new OrderId(TSID.from(123456789L));
    var orderId2 = new OrderId(TSID.from(987654321L));
    var placedAtStart = OffsetDateTime.of(2024, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
    var placedAtEnd = OffsetDateTime.of(2024, 12, 31, 23, 59, 59, 999000000, ZoneOffset.UTC);

    createCustomer(customerId.value());

    var order1 = OrderTestDataBuilder.anOrder()
        .id(orderId1)
        .customerId(customerId)
        .status(OrderStatus.PLACED)
        .placedAt(placedAtStart)
        .items(Set.of(
            OrderItemTestDataBuilder.anOrderItem()
                .id(new OrderItemId(TSID.from(111111111L)))
                .orderId(orderId1)
                .build()
        ))
        .build();

    var order2 = OrderTestDataBuilder.anOrder()
        .id(orderId2)
        .customerId(customerId)
        .status(OrderStatus.PLACED)
        .placedAt(placedAtEnd)
        .items(Set.of(
            OrderItemTestDataBuilder.anOrderItem()
                .id(new OrderItemId(TSID.from(222222222L)))
                .orderId(orderId2)
                .build()
        ))
        .build();

    persistenceProvider.add(order1);
    persistenceProvider.add(order2);

    var orders = persistenceProvider.placedByCustomerInYear(customerId, year);

    assertThat(orders).hasSize(2);
    assertThat(orders).extracting("id").containsExactlyInAnyOrder(orderId1, orderId2);
  }
}