package com.algaworks.algashop.ordering.domain.valueobject;

import static com.algaworks.algashop.ordering.domain.messages.ErrorMessages.VALUE_CANNOT_BE_NEGATIVE;
import static com.algaworks.algashop.ordering.domain.messages.ErrorMessages.VALUE_CANNOT_BE_NULL_OR_EMPTY;
import static java.math.RoundingMode.HALF_EVEN;

import java.math.BigDecimal;

public record Money(BigDecimal value) implements Comparable<Money>{

  private static final int SCALE = 2;

  public Money(BigDecimal value) {
    if (value == null) {
      throw new IllegalArgumentException(VALUE_CANNOT_BE_NULL_OR_EMPTY);
    }
    if (value.compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException(VALUE_CANNOT_BE_NEGATIVE);
    }
    this.value = value.setScale(2, HALF_EVEN);
  }

  public Money(String value) {
    if (value == null || value.isEmpty()) {
      throw new IllegalArgumentException(VALUE_CANNOT_BE_NULL_OR_EMPTY);
    }
    var bigDecimalValue = new BigDecimal(value);
    this(bigDecimalValue);
  }

  public Money add(Money other) {
    return new Money(this.value.add(other.value));
  }

  public Money multiply(int quantity) {
    return new Money(this.value.multiply(new BigDecimal(quantity)));
  }

  public Money divide(Money other) {
    return new Money(this.value.divide(other.value, SCALE, HALF_EVEN));
  }

  @Override
  public int compareTo(Money other) {
    return this.value.compareTo(other.value);
  }

  public String toString() {
    return this.value.toString();
  }
}
