package com.algaworks.algashop.ordering.domain.model.valueobject;

import com.algaworks.algashop.ordering.domain.model.validator.EmailFormatValidator;
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
