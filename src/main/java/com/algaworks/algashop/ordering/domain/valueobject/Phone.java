package com.algaworks.algashop.ordering.domain.valueobject;

import static com.algaworks.algashop.ordering.domain.messages.ErrorMessages.PHONE_CANNOT_BE_BLANK;

import java.util.Objects;

public record Phone(String value) {

  public Phone(String value) {
    Objects.requireNonNull(value);
    if (value.isBlank()) {
      throw new IllegalArgumentException(PHONE_CANNOT_BE_BLANK);
    }
    this.value = value;
  }

  public String toString() {
    return value;
  }
}
