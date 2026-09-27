package com.algaworks.algashop.ordering.domain.model.exception;

import static com.algaworks.algashop.ordering.domain.model.messages.ErrorMessages.CUSTOMER_NOT_FOUND;

import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;

public class CustomerNotFoundException extends DomainException {

  public CustomerNotFoundException(CustomerId customerId) {
    super(CUSTOMER_NOT_FOUND.formatted(customerId.value()));
  }
}
