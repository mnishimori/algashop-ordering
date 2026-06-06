package com.algaworks.algashop.ordering.domain.factory;

import com.algaworks.algashop.ordering.domain.entity.ShoppingCart;
import com.algaworks.algashop.ordering.domain.valueobject.Money;
import com.algaworks.algashop.ordering.domain.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.valueobject.id.ShoppingCartId;
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
