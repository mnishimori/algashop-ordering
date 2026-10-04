package com.algaworks.algashop.ordering.domain.model.order;

import static com.algaworks.algashop.ordering.domain.model.ErrorMessages.ORDER_CANNOT_BE_CANCELED;

import com.algaworks.algashop.ordering.domain.model.DomainException;

public class OrderCannotBeCanceledException extends DomainException {

  public OrderCannotBeCanceledException(OrderId id, OrderStatus status) {
    super(ORDER_CANNOT_BE_CANCELED.formatted(id, status.name()));
  }
}
