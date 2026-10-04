package com.algaworks.algashop.ordering.domain.model.shoppingcart;

import static com.algaworks.algashop.ordering.domain.model.ErrorMessages.SHOPPING_CART_ITEM_NOT_FOUND;

import com.algaworks.algashop.ordering.domain.model.DomainException;
import java.util.UUID;

public class ShoppingCartItemNotFoundException extends DomainException {

  public ShoppingCartItemNotFoundException(UUID shoppinCartItemId) {
    super(SHOPPING_CART_ITEM_NOT_FOUND.formatted(shoppinCartItemId));
  }
}
