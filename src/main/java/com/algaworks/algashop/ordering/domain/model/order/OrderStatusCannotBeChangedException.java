package com.algaworks.algashop.ordering.domain.model.order;

import static com.algaworks.algashop.ordering.domain.model.ErrorMessages.ORDER_STATUS_CANNOT_BE_CHANGED;

import com.algaworks.algashop.ordering.domain.model.DomainException;

public class OrderStatusCannotBeChangedException extends DomainException {

  public OrderStatusCannotBeChangedException(OrderId id, OrderStatus status, OrderStatus orderStatus) {
    super(ORDER_STATUS_CANNOT_BE_CHANGED.formatted(id, status, orderStatus));
  }
}
