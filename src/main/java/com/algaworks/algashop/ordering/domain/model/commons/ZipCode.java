package com.algaworks.algashop.ordering.domain.model.commons;

import static com.algaworks.algashop.ordering.domain.model.ErrorMessages.ZIP_CODE_IS_INVALID;

public record ZipCode(String value) {

  public ZipCode(String value) {
    AttributeValidator.validateAttributeNotBlank("ZipCode", value);
    if (!value.matches("\\d{5}-\\d{3}")) {
      throw new IllegalArgumentException(ZIP_CODE_IS_INVALID);
    }
    this.value = value;
  }

  @Override
  public String toString() {
    return this.value;
  }
}
