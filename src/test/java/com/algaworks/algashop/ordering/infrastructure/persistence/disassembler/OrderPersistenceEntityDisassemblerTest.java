package com.algaworks.algashop.ordering.infrastructure.persistence.disassembler;

import static org.assertj.core.api.Assertions.assertThat;

import com.algaworks.algashop.ordering.domain.model.entity.Order;
import com.algaworks.algashop.ordering.domain.model.entity.OrderStatus;
import com.algaworks.algashop.ordering.domain.model.entity.PaymentMethod;
import com.algaworks.algashop.ordering.domain.model.valueobject.Money;
import com.algaworks.algashop.ordering.domain.model.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderId;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.CustomerPersistenceEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderPersistenceEntityDisassemblerTest {

  @Test
  @DisplayName("Should convert OrderPersistenceEntity to Order domain entity")
  void shouldConvertOrderPersistenceEntityToOrderDomainEntity() {
    OrderPersistenceEntityDisassembler disassembler = new OrderPersistenceEntityDisassembler();

    Long id = 123456789L;
    UUID customerId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
    CustomerPersistenceEntity customer = new CustomerPersistenceEntity();
    customer.setId(customerId);
    BigDecimal totalAmount = new BigDecimal("100.00");
    Integer totalItems = 5;
    String status = "PLACED";
    String paymentMethod = "CREDIT_CARD";
    OffsetDateTime placedAt = OffsetDateTime.now();
    OffsetDateTime paidAt = OffsetDateTime.now();
    OffsetDateTime canceledAt = null;
    OffsetDateTime readyAt = null;

    OrderPersistenceEntity persistenceEntity = OrderPersistenceEntity.builder()
        .id(id)
        .customer(customer)
        .totalAmount(totalAmount)
        .totalItems(totalItems)
        .status(status)
        .paymentMethod(paymentMethod)
        .placedAt(placedAt)
        .paidAt(paidAt)
        .canceledAt(canceledAt)
        .readyAt(readyAt)
        .build();

    Order order = disassembler.toDomainEntity(persistenceEntity);

    assertThat(order).isNotNull();
    assertThat(order.id()).isEqualTo(new OrderId(id));
    assertThat(order.customerId()).isEqualTo(new CustomerId(customerId));
    assertThat(order.status()).isEqualTo(OrderStatus.PLACED);
    assertThat(order.totalAmount()).isEqualTo(new Money(totalAmount));
    assertThat(order.totalItems()).isEqualTo(new Quantity(new BigDecimal(totalItems)));
    assertThat(order.paymentMethod()).isEqualTo(PaymentMethod.CREDIT_CARD);
    assertThat(order.placedAt()).isEqualTo(placedAt);
    assertThat(order.paidAt()).isEqualTo(paidAt);
    assertThat(order.canceledAt()).isNull();
    assertThat(order.readyAt()).isNull();
  }

  @Test
  @DisplayName("Should convert OrderPersistenceEntity with all date fields populated")
  void shouldConvertOrderPersistenceEntityWithAllDateFieldsPopulated() {
    OrderPersistenceEntityDisassembler disassembler = new OrderPersistenceEntityDisassembler();

    CustomerPersistenceEntity customer = new CustomerPersistenceEntity();
    customer.setId(UUID.randomUUID());
    OrderPersistenceEntity persistenceEntity = OrderPersistenceEntity.builder()
        .id(987654321L)
        .customer(customer)
        .totalAmount(new BigDecimal("250.50"))
        .totalItems(10)
        .status("READY")
        .paymentMethod("GATEWAY_BALANCE")
        .placedAt(OffsetDateTime.now().minusDays(2))
        .paidAt(OffsetDateTime.now().minusDays(1))
        .canceledAt(null)
        .readyAt(OffsetDateTime.now())
        .build();

    Order order = disassembler.toDomainEntity(persistenceEntity);

    assertThat(order).isNotNull();
    assertThat(order.id()).isEqualTo(new OrderId(987654321L));
    assertThat(order.status()).isEqualTo(OrderStatus.READY);
    assertThat(order.paymentMethod()).isEqualTo(PaymentMethod.GATEWAY_BALANCE);
    assertThat(order.placedAt()).isNotNull();
    assertThat(order.paidAt()).isNotNull();
    assertThat(order.canceledAt()).isNull();
    assertThat(order.readyAt()).isNotNull();
  }

  @Test
  @DisplayName("Should convert OrderPersistenceEntity with canceled status")
  void shouldConvertOrderPersistenceEntityWithCanceledStatus() {
    OrderPersistenceEntityDisassembler disassembler = new OrderPersistenceEntityDisassembler();

    CustomerPersistenceEntity customer = new CustomerPersistenceEntity();
    customer.setId(UUID.randomUUID());
    OrderPersistenceEntity persistenceEntity = OrderPersistenceEntity.builder()
        .id(111222333L)
        .customer(customer)
        .totalAmount(new BigDecimal("75.00"))
        .totalItems(2)
        .status("CANCELED")
        .paymentMethod("GATEWAY_BALANCE")
        .placedAt(OffsetDateTime.now().minusDays(5))
        .paidAt(OffsetDateTime.now().minusDays(4))
        .canceledAt(OffsetDateTime.now().minusDays(3))
        .readyAt(null)
        .build();

    Order order = disassembler.toDomainEntity(persistenceEntity);

    assertThat(order).isNotNull();
    assertThat(order.status()).isEqualTo(OrderStatus.CANCELED);
    assertThat(order.paymentMethod()).isEqualTo(PaymentMethod.GATEWAY_BALANCE);
    assertThat(order.canceledAt()).isNotNull();
    assertThat(order.readyAt()).isNull();
  }

  @Test
  @DisplayName("Should convert OrderPersistenceEntity with DRAFT status and null dates")
  void shouldConvertOrderPersistenceEntityWithDraftStatusAndNullDates() {
    OrderPersistenceEntityDisassembler disassembler = new OrderPersistenceEntityDisassembler();

    CustomerPersistenceEntity customer = new CustomerPersistenceEntity();
    customer.setId(UUID.randomUUID());
    OrderPersistenceEntity persistenceEntity = OrderPersistenceEntity.builder()
        .id(444555666L)
        .customer(customer)
        .totalAmount(BigDecimal.ZERO)
        .totalItems(0)
        .status("DRAFT")
        .paymentMethod(null)
        .placedAt(null)
        .paidAt(null)
        .canceledAt(null)
        .readyAt(null)
        .build();

    Order order = disassembler.toDomainEntity(persistenceEntity);

    assertThat(order).isNotNull();
    assertThat(order.status()).isEqualTo(OrderStatus.DRAFT);
    assertThat(order.paymentMethod()).isNull();
    assertThat(order.placedAt()).isNull();
    assertThat(order.paidAt()).isNull();
    assertThat(order.canceledAt()).isNull();
    assertThat(order.readyAt()).isNull();
  }
}
