package com.algaworks.algashop.ordering.domain.model.valueobject.id;

import com.algaworks.algashop.ordering.domain.model.utility.IdGenerator;
import java.util.Objects;
import java.util.UUID;

public record ShoppingCartItemId(UUID value) {

  public ShoppingCartItemId(){
    var shoppingCartItemId = IdGenerator.generateTimeBasedUuid();
    this(shoppingCartItemId);
  }

  public ShoppingCartItemId(UUID value){
    this.value = Objects.requireNonNull(value);
  }

  @Override
  public String toString() {
    return value.toString();
  }
}
