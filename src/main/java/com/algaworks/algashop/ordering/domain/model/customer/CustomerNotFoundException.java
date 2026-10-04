package com.algaworks.algashop.ordering.domain.model.customer;

import static com.algaworks.algashop.ordering.domain.model.ErrorMessages.CUSTOMER_NOT_FOUND;

import com.algaworks.algashop.ordering.domain.model.DomainException;

public class CustomerNotFoundException extends DomainException {

  public CustomerNotFoundException(CustomerId customerId) {
    super(CUSTOMER_NOT_FOUND.formatted(customerId.value()));
  }
}
