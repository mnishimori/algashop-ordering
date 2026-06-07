package com.algaworks.algashop.ordering.domain.model.exception;

import static com.algaworks.algashop.ordering.domain.model.messages.ErrorMessages.ORDER_CANNOT_BE_EDITED;

import com.algaworks.algashop.ordering.domain.model.entity.OrderStatus;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderId;

public class OrderCannotBeEditedException extends DomainException {

  public OrderCannotBeEditedException(OrderId id, OrderStatus orderStatus) {
    super(ORDER_CANNOT_BE_EDITED.formatted(id, orderStatus.name()));
  }
}
