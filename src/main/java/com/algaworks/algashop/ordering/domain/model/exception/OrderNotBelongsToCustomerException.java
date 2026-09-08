package com.algaworks.algashop.ordering.domain.model.exception;

import static com.algaworks.algashop.ordering.domain.model.messages.ErrorMessages.ORDER_NOT_BELONGS_TO_CUSTOMER;

public class OrderNotBelongsToCustomerException extends DomainException {

  public OrderNotBelongsToCustomerException() {
    super(ORDER_NOT_BELONGS_TO_CUSTOMER);
  }
}
