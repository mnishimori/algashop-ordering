package com.algaworks.algashop.ordering.domain.exception;

import static com.algaworks.algashop.ordering.domain.messages.ErrorMessages.CUSTOMER_ARCHIVED;

public class CustomerArchivedException extends DomainException {

  public CustomerArchivedException() {
    super(CUSTOMER_ARCHIVED);
  }
}
