package com.algaworks.algashop.ordering.domain.valueobject;

import static com.algaworks.algashop.ordering.domain.messages.ErrorMessages.QUANTITY_MUST_BE_GREATER_THAN_ZERO;

import java.math.BigDecimal;

public record Quantity(BigDecimal value) implements Comparable<Quantity> {

  public static BigDecimal Quantity = BigDecimal.ZERO;

  public Quantity(BigDecimal value) {
    if (value == null || value.compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException(QUANTITY_MUST_BE_GREATER_THAN_ZERO);
    }
    this.value = value;
  }

  public Quantity add(Quantity other) {
    return new Quantity(this.value.add(other.value));
  }

  public String toString() {
    return value.toString();
  }

  @Override
  public int compareTo(Quantity o) {
    return this.value.compareTo(o.value);
  }
}
