package com.algaworks.algashop.ordering.domain.validator;

import java.util.Objects;

public class AttributeValidator {

  public static final String ATTRIBUTE_CANNOT_BE_NULL = "Attribute %s cannot be null";

  private AttributeValidator() {}

  public static void validateAttributeNotBlank(String attributeName, String attributeValue) {
    Objects.requireNonNull(attributeValue);
    if (attributeValue.isBlank()) {
      throw new IllegalArgumentException(ATTRIBUTE_CANNOT_BE_NULL.formatted(attributeName));
    }
  }
}
