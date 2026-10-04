package com.algaworks.algashop.ordering.domain.model.shoppingcart;

import com.algaworks.algashop.ordering.domain.model.DomainException;

public class ShoppingCartItemIncompatibleProductException extends DomainException {

  public ShoppingCartItemIncompatibleProductException(String message) {
    super(message);
  }

  public ShoppingCartItemIncompatibleProductException(String message, Throwable cause) {
    super(message, cause);
  }
}
