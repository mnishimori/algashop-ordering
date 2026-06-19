package com.algaworks.algashop.ordering.infrastructure.persistence;

import com.algaworks.algashop.ordering.domain.model.entity.Order;
import com.algaworks.algashop.ordering.domain.model.entity.OrderStatus;
import com.algaworks.algashop.ordering.domain.model.entity.PaymentMethod;
import com.algaworks.algashop.ordering.domain.model.repository.Orders;
import com.algaworks.algashop.ordering.domain.model.valueobject.Money;
import com.algaworks.algashop.ordering.domain.model.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderId;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.repository.OrderPersistenceEntityRepository;
import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrdersPersistenceProvider implements Orders {

  private final OrderPersistenceEntityRepository repository;

  @Override
  public Optional<Order> findById(OrderId orderId) {
    return repository.findById(orderId.value().toLong()).map(this::toDomainModel);
  }

  @Override
  public boolean existsById(OrderId orderId) {
    return repository.existsById(orderId.value().toLong());
  }

  @Override
  public void add(Order aggregateRoot) {
    var entity = toPersistenceEntity(aggregateRoot);
    repository.save(entity);
  }

  @Override
  public int count() {
    return (int) repository.count();
  }

  private Order toDomainModel(OrderPersistenceEntity entity) {
    return Order.existingOrderBuilder()
        .id(new OrderId(entity.getId()))
        .customerId(new CustomerId(entity.getCustomerId()))
        .totalAmount(new Money(entity.getTotalAmount()))
        .totalItems(new Quantity(BigDecimal.valueOf(entity.getTotalItems())))
        .placedAt(entity.getPlacedAt())
        .paidAt(entity.getPaidAt())
        .canceledAt(entity.getCanceledAt())
        .readyAt(entity.getReadyAt())
        .billing(null)
        .shipping(null)
        .status(entity.getStatus() != null ? OrderStatus.valueOf(entity.getStatus()) : null)
        .paymentMethod(entity.getPaymentMethod() != null ? PaymentMethod.valueOf(entity.getPaymentMethod()) : null)
        .items(new LinkedHashSet<>())
        .build();
  }

  private OrderPersistenceEntity toPersistenceEntity(Order order) {
    return OrderPersistenceEntity.builder()
        .id(order.id().value().toLong())
        .customerId(order.customerId().value())
        .totalAmount(order.totalAmount().value())
        .totalItems(order.totalItems().value().intValue())
        .status(order.status() != null ? order.status().name() : null)
        .paymentMethod(order.paymentMethod() != null ? order.paymentMethod().name() : null)
        .placedAt(order.placedAt())
        .paidAt(order.paidAt())
        .canceledAt(order.canceledAt())
        .readyAt(order.readyAt())
        .build();
  }
}
