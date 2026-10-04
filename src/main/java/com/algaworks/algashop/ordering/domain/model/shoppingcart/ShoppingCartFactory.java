package com.algaworks.algashop.ordering.domain.model.shoppingcart;

import com.algaworks.algashop.ordering.domain.model.commons.Money;
import com.algaworks.algashop.ordering.domain.model.commons.Quantity;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerId;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Objects;

public class ShoppingCartFactory {

  private ShoppingCartFactory() {
  }

  public static ShoppingCart createShoppingCart(CustomerId customerId) {
    Objects.requireNonNull(customerId);
    return ShoppingCart.existingShoppingCartBuilder()
        .shoppingCartId(new ShoppingCartId())
        .customerId(customerId)
        .totalAmount(Money.ZERO)
        .totalItems(Quantity.ZERO)
        .createdAt(OffsetDateTime.now())
        .items(new HashSet<>())
        .build();
  }
}
