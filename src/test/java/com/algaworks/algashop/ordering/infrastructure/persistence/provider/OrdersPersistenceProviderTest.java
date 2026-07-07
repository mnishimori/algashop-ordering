package com.algaworks.algashop.ordering.infrastructure.persistence.provider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.algaworks.algashop.ordering.domain.model.entity.Order;
import com.algaworks.algashop.ordering.domain.model.entity.OrderStatus;
import com.algaworks.algashop.ordering.domain.model.valueobject.Money;
import com.algaworks.algashop.ordering.domain.model.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderId;
import com.algaworks.algashop.ordering.infrastructure.persistence.assembler.OrderPersistenceEntityAssembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.disassembler.OrderPersistenceEntityDisassembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.repository.OrderPersistenceEntityRepository;
import io.hypersistence.tsid.TSID;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrdersPersistenceProviderTest {

  @Mock
  private OrderPersistenceEntityRepository repository;

  @Mock
  private OrderPersistenceEntityDisassembler disassembler;

  @Mock
  private OrderPersistenceEntityAssembler assembler;

  @InjectMocks
  private OrdersPersistenceProvider provider;

  @Test
  @DisplayName("Should return Optional.empty when order not found by id")
  void shouldReturnEmptyWhenOrderNotFoundById() {
    OrderId orderId = new OrderId(TSID.from(123456789L));
    when(repository.findById(123456789L)).thenReturn(Optional.empty());

    Optional<Order> result = provider.findById(orderId);

    assertThat(result).isEmpty();
    verify(repository).findById(123456789L);
    verify(disassembler, never()).toDomainEntity(any());
  }

  @Test
  @DisplayName("Should return order when found by id")
  void shouldReturnOrderWhenFoundById() {
    OrderId orderId = new OrderId(TSID.from(123456789L));
    OrderPersistenceEntity entity = OrderPersistenceEntity.builder()
        .id(123456789L)
        .customerId(UUID.randomUUID())
        .totalAmount(BigDecimal.ZERO)
        .totalItems(0)
        .status("DRAFT")
        .build();
    Order order = Order.createDraftOrder(new CustomerId(UUID.randomUUID()));

    when(repository.findById(123456789L)).thenReturn(Optional.of(entity));
    when(disassembler.toDomainEntity(entity)).thenReturn(order);

    Optional<Order> result = provider.findById(orderId);

    assertThat(result).isPresent();
    assertThat(result.get()).isEqualTo(order);
    verify(repository).findById(123456789L);
    verify(disassembler).toDomainEntity(entity);
  }

  @Test
  @DisplayName("Should return true when order exists by id")
  void shouldReturnTrueWhenOrderExistsById() {
    OrderId orderId = new OrderId(TSID.from(123456789L));
    when(repository.existsById(123456789L)).thenReturn(true);

    boolean result = provider.existsById(orderId);

    assertThat(result).isTrue();
    verify(repository).existsById(123456789L);
  }

  @Test
  @DisplayName("Should return false when order does not exist by id")
  void shouldReturnFalseWhenOrderDoesNotExistById() {
    OrderId orderId = new OrderId(TSID.from(123456789L));
    when(repository.existsById(123456789L)).thenReturn(false);

    boolean result = provider.existsById(orderId);

    assertThat(result).isFalse();
    verify(repository).existsById(123456789L);
  }

  @Test
  @DisplayName("Should add order to repository")
  void shouldAddOrderToRepository() {
    Order order = Order.createDraftOrder(new CustomerId(UUID.randomUUID()));
    OrderPersistenceEntity entity = OrderPersistenceEntity.builder()
        .id(order.id().value().toLong())
        .customerId(order.customerId().value())
        .totalAmount(order.totalAmount().value())
        .totalItems(order.totalItems().value().intValue())
        .status(order.status().name())
        .build();

    when(assembler.fromDomain(order)).thenReturn(entity);

    provider.add(order);

    verify(assembler).fromDomain(order);
  }

  @Test
  @DisplayName("Should return count of orders")
  void shouldReturnCountOfOrders() {
    when(repository.count()).thenReturn(5L);

    int result = provider.count();

    assertThat(result).isEqualTo(5);
    verify(repository).count();
  }

  @Test
  @DisplayName("Should return zero when no orders exist")
  void shouldReturnZeroWhenNoOrdersExist() {
    when(repository.count()).thenReturn(0L);

    int result = provider.count();

    assertThat(result).isEqualTo(0);
    verify(repository).count();
  }

  @Test
  @DisplayName("Should handle large count value")
  void shouldHandleLargeCountValue() {
    when(repository.count()).thenReturn(1000L);

    int result = provider.count();

    assertThat(result).isEqualTo(1000);
    verify(repository).count();
  }
}
