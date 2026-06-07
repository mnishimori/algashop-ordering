package com.algaworks.algashop.ordering.domain.model.exception;

import static com.algaworks.algashop.ordering.domain.model.messages.ErrorMessages.CUSTOMER_ARCHIVED;

public class CustomerArchivedException extends DomainException {

  public CustomerArchivedException() {
    super(CUSTOMER_ARCHIVED);
  }
}
