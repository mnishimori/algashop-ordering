package com.algaworks.algashop.ordering.domain.model.valueobject;

import static com.algaworks.algashop.ordering.domain.model.messages.ErrorMessages.DOCUMENT_CANNOT_BE_BLANK;

import java.util.Objects;

public record Document(String value) {

  public Document(String value) {
    Objects.requireNonNull(value);
    if (value.isBlank()) {
      throw new IllegalArgumentException(DOCUMENT_CANNOT_BE_BLANK);
    }
    this.value = value;
  }

  public String toString() {
    return value;
  }
}
