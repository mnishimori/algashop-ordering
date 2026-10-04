package com.algaworks.algashop.ordering.domain.model.customer;

import static com.algaworks.algashop.ordering.domain.model.ErrorMessages.CUSTOMER_ARCHIVED;

import com.algaworks.algashop.ordering.domain.model.DomainException;

public class CustomerArchivedException extends DomainException {

  public CustomerArchivedException() {
    super(CUSTOMER_ARCHIVED);
  }
}
