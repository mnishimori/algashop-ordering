package com.algaworks.algashop.ordering.domain.exception;

import static com.algaworks.algashop.ordering.domain.messages.ErrorMessages.ORDER_ITEM_NOT_FOUND;

import com.algaworks.algashop.ordering.domain.valueobject.id.OrderItemId;

public class OrderItemNotFoundException extends DomainException {

  public OrderItemNotFoundException(OrderItemId orderItemId) {
    super(String.format(ORDER_ITEM_NOT_FOUND, orderItemId));
  }
}
