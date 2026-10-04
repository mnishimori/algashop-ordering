package com.algaworks.algashop.ordering.domain.model.order;

import static com.algaworks.algashop.ordering.domain.model.ErrorMessages.ORDER_ITEM_NOT_FOUND;

import com.algaworks.algashop.ordering.domain.model.DomainException;

public class OrderItemNotFoundException extends DomainException {

  public OrderItemNotFoundException(OrderItemId orderItemId) {
    super(String.format(ORDER_ITEM_NOT_FOUND, orderItemId));
  }
}
