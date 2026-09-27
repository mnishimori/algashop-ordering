package com.algaworks.algashop.ordering.domain.model.exception;

import static com.algaworks.algashop.ordering.domain.model.messages.ErrorMessages.CUSTOMER_ALREADY_HAVE_SHOPPING_CART;

import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;

public class CustomerAlreadyHaveShoppingCartException extends DomainException {

  public CustomerAlreadyHaveShoppingCartException(CustomerId customerId) {
    super(CUSTOMER_ALREADY_HAVE_SHOPPING_CART.formatted(customerId.value()));
  }
}
