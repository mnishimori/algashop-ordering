package com.algaworks.algashop.ordering.domain.model.shoppingcart;

import static com.algaworks.algashop.ordering.domain.model.ErrorMessages.SHOPPING_CART_NOT_FOUND;

import com.algaworks.algashop.ordering.domain.model.DomainException;

public class ShoppingCartNotFoundException extends DomainException {

  public ShoppingCartNotFoundException(ShoppingCartId shoppingCartId) {
    super(SHOPPING_CART_NOT_FOUND.formatted(shoppingCartId));
  }
}
