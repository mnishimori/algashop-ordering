package com.algaworks.algashop.ordering.domain.exception;

import com.algaworks.algashop.ordering.domain.entity.OrderStatus;
import com.algaworks.algashop.ordering.domain.messages.ErrorMessages;
import com.algaworks.algashop.ordering.domain.valueobject.id.OrderId;

public class OrderCannotBeReadyException extends DomainException {

  public OrderCannotBeReadyException(OrderId id, OrderStatus orderStatus) {
    super(ErrorMessages.ORDER_CANNOT_BE_READY.formatted(id, orderStatus.name()));
  }
}
