package com.algaworks.algashop.ordering.domain.model.customer;

import static com.algaworks.algashop.ordering.domain.model.ErrorMessages.CUSTOMER_ALREADY_HAVE_SHOPPING_CART;

import com.algaworks.algashop.ordering.domain.model.DomainException;

public class CustomerAlreadyHaveShoppingCartException extends DomainException {

  public CustomerAlreadyHaveShoppingCartException(CustomerId customerId) {
    super(CUSTOMER_ALREADY_HAVE_SHOPPING_CART.formatted(customerId.value()));
  }
}
