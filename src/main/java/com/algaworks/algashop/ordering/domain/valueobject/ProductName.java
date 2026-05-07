package com.algaworks.algashop.ordering.domain.valueobject;

import static com.algaworks.algashop.ordering.domain.messages.ErrorMessages.PRODUCT_NAME_CANNOT_BE_NULL_OR_EMPTY;

public record ProductName(String value) {

  public ProductName(String value) {
    if (value == null || value.trim().isEmpty()) {
      throw new IllegalArgumentException(PRODUCT_NAME_CANNOT_BE_NULL_OR_EMPTY);
    }
    this.value = value;
  }

  public String toString() {
    return this.value;
  }
}
