package com.algaworks.algashop.ordering.domain.model.order;

import static com.algaworks.algashop.ordering.domain.model.ErrorMessages.ORDER_CANNOT_BE_READY;

import com.algaworks.algashop.ordering.domain.model.DomainException;

public class OrderCannotBeReadyException extends DomainException {

  public OrderCannotBeReadyException(OrderId id, OrderStatus orderStatus) {
    super(ORDER_CANNOT_BE_READY.formatted(id, orderStatus.name()));
  }
}
