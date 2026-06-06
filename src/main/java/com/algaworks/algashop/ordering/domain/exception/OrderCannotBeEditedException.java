package com.algaworks.algashop.ordering.domain.exception;

import static com.algaworks.algashop.ordering.domain.messages.ErrorMessages.ORDER_CANNOT_BE_EDITED;

import com.algaworks.algashop.ordering.domain.entity.OrderStatus;
import com.algaworks.algashop.ordering.domain.valueobject.id.OrderId;

public class OrderCannotBeEditedException extends DomainException {

  public OrderCannotBeEditedException(OrderId id, OrderStatus orderStatus) {
    super(ORDER_CANNOT_BE_EDITED.formatted(id, orderStatus.name()));
  }
}
