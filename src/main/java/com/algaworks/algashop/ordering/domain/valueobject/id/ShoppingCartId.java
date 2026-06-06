package com.algaworks.algashop.ordering.domain.valueobject.id;

import com.algaworks.algashop.ordering.domain.utility.IdGenerator;
import java.util.Objects;
import java.util.UUID;

public record ShoppingCartId(UUID value) {

  public ShoppingCartId() {
    var shoppingCartId = IdGenerator.generateTimeBasedUuid();
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
