package com.algaworks.algashop.ordering.domain.model.valueobject.id;

import com.algaworks.algashop.ordering.domain.model.utility.IdGenerator;
import java.util.Objects;
import java.util.UUID;

public record ShoppingCartId(UUID value) {

  public ShoppingCartId() {
    var shoppingCartId = IdGenerator.generateTimeBasedUUID();
    this(shoppingCartId);
  }

  public ShoppingCartId(UUID value) {
    this.value = Objects.requireNonNull(value);
  }

  @Override
  public String toString() {
    return value.toString();
  }
}
