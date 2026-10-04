package com.algaworks.algashop.ordering.domain.model.shoppingcart;

import com.algaworks.algashop.ordering.domain.model.DomainException;

public class ShoppingCartCantProceedToCheckoutException extends DomainException {

  public ShoppingCartCantProceedToCheckoutException(String message) {
    super(message);
  }
}
