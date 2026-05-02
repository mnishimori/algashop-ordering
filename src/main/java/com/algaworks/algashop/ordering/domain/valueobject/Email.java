package com.algaworks.algashop.ordering.domain.valueobject;

import com.algaworks.algashop.ordering.domain.validator.EmailFormatValidator;
import java.util.Objects;

public record Email(String value) {

  public Email(String value) {
    Objects.requireNonNull(value);
    EmailFormatValidator.validate(value);
    this.value = value;
  }

  public String toString() {
    return value;
  }
}
