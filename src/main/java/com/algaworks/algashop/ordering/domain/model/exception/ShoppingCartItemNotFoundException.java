package com.algaworks.algashop.ordering.domain.model.exception;

import static com.algaworks.algashop.ordering.domain.model.messages.ErrorMessages.SHOPPING_CART_ITEM_NOT_FOUND;

import java.util.UUID;

public class ShoppingCartItemNotFoundException extends DomainException {

  public ShoppingCartItemNotFoundException(UUID shoppinCartItemId) {
    super(SHOPPING_CART_ITEM_NOT_FOUND.formatted(shoppinCartItemId));
  }
}
