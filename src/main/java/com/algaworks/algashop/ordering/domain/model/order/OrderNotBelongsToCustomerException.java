package com.algaworks.algashop.ordering.domain.model.order;

import static com.algaworks.algashop.ordering.domain.model.ErrorMessages.ORDER_NOT_BELONGS_TO_CUSTOMER;

import com.algaworks.algashop.ordering.domain.model.DomainException;

public class OrderNotBelongsToCustomerException extends DomainException {

  public OrderNotBelongsToCustomerException() {
    super(ORDER_NOT_BELONGS_TO_CUSTOMER);
  }
}
