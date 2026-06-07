package com.algaworks.algashop.ordering.domain.model.exception;

import static com.algaworks.algashop.ordering.domain.model.messages.ErrorMessages.ORDER_STATUS_CANNOT_BE_CHANGED;

import com.algaworks.algashop.ordering.domain.model.entity.OrderStatus;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderId;

public class OrderStatusCannotBeChangedException extends DomainException {

  public OrderStatusCannotBeChangedException(OrderId id, OrderStatus status, OrderStatus orderStatus) {
    super(ORDER_STATUS_CANNOT_BE_CHANGED.formatted(id, status, orderStatus));
  }
}
