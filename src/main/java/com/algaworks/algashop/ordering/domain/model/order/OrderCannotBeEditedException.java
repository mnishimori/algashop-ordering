package com.algaworks.algashop.ordering.domain.model.order;

import static com.algaworks.algashop.ordering.domain.model.ErrorMessages.ORDER_CANNOT_BE_EDITED;

import com.algaworks.algashop.ordering.domain.model.DomainException;

public class OrderCannotBeEditedException extends DomainException {

  public OrderCannotBeEditedException(OrderId id, OrderStatus orderStatus) {
    super(ORDER_CANNOT_BE_EDITED.formatted(id, orderStatus.name()));
  }
}
