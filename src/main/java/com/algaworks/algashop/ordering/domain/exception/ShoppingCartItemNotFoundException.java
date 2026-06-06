package com.algaworks.algashop.ordering.domain.exception;

import static com.algaworks.algashop.ordering.domain.messages.ErrorMessages.SHOPPING_CART_ITEM_NOT_FOUND;

import java.util.UUID;

public class ShoppingCartItemNotFoundException extends DomainException {

  public ShoppingCartItemNotFoundException(UUID shoppinCartItemId) {
    super(SHOPPING_CART_ITEM_NOT_FOUND.formatted(shoppinCartItemId));
  }
}
