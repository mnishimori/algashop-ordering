package com.algaworks.algashop.ordering.domain.model.exception;

import static com.algaworks.algashop.ordering.domain.model.messages.ErrorMessages.ORDER_CANNOT_BE_CANCELED;

import com.algaworks.algashop.ordering.domain.model.entity.OrderStatus;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderId;

public class OrderCannotBeCanceledException extends DomainException {

  public OrderCannotBeCanceledException(OrderId id, OrderStatus status) {
    super(ORDER_CANNOT_BE_CANCELED.formatted(id, status.name()));
  }
}
