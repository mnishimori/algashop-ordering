package com.algaworks.algashop.ordering.domain.exception;

public class ShoppingCartItemIncompatibleProductException extends DomainException {

  public ShoppingCartItemIncompatibleProductException(String message) {
    super(message);
  }

  public ShoppingCartItemIncompatibleProductException(String message, Throwable cause) {
    super(message, cause);
  }
}
