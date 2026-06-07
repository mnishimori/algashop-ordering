package com.algaworks.algashop.ordering.domain.model.exception;

import static com.algaworks.algashop.ordering.domain.model.messages.ErrorMessages.ORDER_ITEM_NOT_FOUND;

import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderItemId;

public class OrderItemNotFoundException extends DomainException {

  public OrderItemNotFoundException(OrderItemId orderItemId) {
    super(String.format(ORDER_ITEM_NOT_FOUND, orderItemId));
  }
}
