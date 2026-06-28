package com.algaworks.algashop.ordering.infrastructure.persistence.disassembler;

import com.algaworks.algashop.ordering.domain.model.entity.Order;
import com.algaworks.algashop.ordering.domain.model.entity.OrderStatus;
import com.algaworks.algashop.ordering.domain.model.entity.PaymentMethod;
import com.algaworks.algashop.ordering.domain.model.valueobject.Money;
import com.algaworks.algashop.ordering.domain.model.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderId;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import java.math.BigDecimal;
import java.util.Collections;
import org.springframework.stereotype.Component;

@Component
public class OrderPersistenceEntityDisassembler {

  public Order toDomainEntity(OrderPersistenceEntity orderPersistenceEntity) {
    return Order.existingOrderBuilder()
        .id(new OrderId(orderPersistenceEntity.getId()))
        .customerId(new CustomerId(orderPersistenceEntity.getCustomerId()))
        .status(OrderStatus.valueOf(orderPersistenceEntity.getStatus()))
        .totalAmount(new Money(orderPersistenceEntity.getTotalAmount()))
        .totalItems(new Quantity(new BigDecimal(orderPersistenceEntity.getTotalItems())))
        .paymentMethod(orderPersistenceEntity.getPaymentMethod() != null 
            ? PaymentMethod.valueOf(orderPersistenceEntity.getPaymentMethod()) 
            : null)
        .placedAt(orderPersistenceEntity.getPlacedAt())
        .paidAt(orderPersistenceEntity.getPaidAt())
        .canceledAt(orderPersistenceEntity.getCanceledAt())
        .readyAt(orderPersistenceEntity.getReadyAt())
        .billing(null)
        .shipping(null)
        .items(Collections.emptySet())
        .build();
  }
}
