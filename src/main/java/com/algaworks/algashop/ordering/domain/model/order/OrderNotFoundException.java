package com.algaworks.algashop.ordering.domain.model.order;

import static com.algaworks.algashop.ordering.domain.model.ErrorMessages.ORDER_NOT_FOUND;

import com.algaworks.algashop.ordering.domain.model.DomainException;

public class OrderNotFoundException extends DomainException {

  public OrderNotFoundException(OrderId orderId) {
    super(ORDER_NOT_FOUND.formatted(orderId));
  }
}
