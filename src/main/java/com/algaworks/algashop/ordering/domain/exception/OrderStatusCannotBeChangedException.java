package com.algaworks.algashop.ordering.domain.exception;

import static com.algaworks.algashop.ordering.domain.messages.ErrorMessages.ORDER_STATUS_CANNOT_BE_CHANGED;

import com.algaworks.algashop.ordering.domain.entity.OrderStatus;
import com.algaworks.algashop.ordering.domain.valueobject.id.OrderId;

public class OrderStatusCannotBeChangedException extends DomainException {

  public OrderStatusCannotBeChangedException(OrderId id, OrderStatus status, OrderStatus orderStatus) {
    super(ORDER_STATUS_CANNOT_BE_CHANGED.formatted(id, status, orderStatus));
  }
}
