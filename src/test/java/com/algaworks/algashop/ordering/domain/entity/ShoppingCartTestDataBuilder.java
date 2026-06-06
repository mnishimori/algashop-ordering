package com.algaworks.algashop.ordering.domain.entity;

import com.algaworks.algashop.ordering.domain.valueobject.Money;
import com.algaworks.algashop.ordering.domain.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.valueobject.id.ShoppingCartId;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ShoppingCartTestDataBuilder {

  private ShoppingCartId shoppingCartId;
  private CustomerId customerId;
  private Money totalAmount;
  private Quantity totalItems;
  private OffsetDateTime createdAt;
  private Set<ShoppingCartItem> items;

  private ShoppingCartTestDataBuilder() {
    this.shoppingCartId = new ShoppingCartId(UUID.fromString("11111111-1111-1111-1111-111111111111"));
    this.customerId = new CustomerId(UUID.fromString("22222222-2222-2222-2222-222222222222"));
    this.totalAmount = Money.ZERO;
    this.totalItems = Quantity.ZERO;
    this.createdAt = OffsetDateTime.now();
    this.items = new HashSet<>();
  }

  public static ShoppingCartTestDataBuilder aShoppingCart() {
    return new ShoppingCartTestDataBuilder();
  }

  public ShoppingCartTestDataBuilder shoppingCartId(ShoppingCartId shoppingCartId) {
    this.shoppingCartId = shoppingCartId;
    return this;
  }

  public ShoppingCartTestDataBuilder customerId(CustomerId customerId) {
    this.customerId = customerId;
    return this;
  }

  public ShoppingCartTestDataBuilder totalAmount(Money totalAmount) {
    this.totalAmount = totalAmount;
    return this;
  }

  public ShoppingCartTestDataBuilder totalItems(Quantity totalItems) {
    this.totalItems = totalItems;
    return this;
  }

  public ShoppingCartTestDataBuilder createdAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  public ShoppingCartTestDataBuilder items(Set<ShoppingCartItem> items) {
    this.items = items;
    return this;
  }

  public ShoppingCart build() {
    return ShoppingCart.existingShoppingCartBuilder()
        .shoppingCartId(shoppingCartId)
        .customerId(customerId)
        .totalAmount(totalAmount)
        .totalItems(totalItems)
        .createdAt(createdAt)
        .items(items)
        .build();
  }
}
