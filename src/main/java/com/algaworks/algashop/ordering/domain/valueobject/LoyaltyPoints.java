package com.algaworks.algashop.ordering.domain.valueobject;

import static com.algaworks.algashop.ordering.domain.messages.ErrorMessages.LOYALTY_POINTS_CANNOT_BE_NEGATIVE_OR_ZERO;

import java.util.Objects;

public record LoyaltyPoints(Integer value) implements Comparable<LoyaltyPoints> {

  public static final LoyaltyPoints ZERO = new LoyaltyPoints(0);

  public LoyaltyPoints() {
    this(0);
  }

  public LoyaltyPoints(Integer value) {
    Objects.requireNonNull(value);
    if (value < 0) {
      throw new IllegalArgumentException(LOYALTY_POINTS_CANNOT_BE_NEGATIVE_OR_ZERO);
    }
    this.value = value;
  }

  public LoyaltyPoints add(Integer points) {
    Objects.requireNonNull(points);
    if (points <= 0) {
      throw new IllegalArgumentException(LOYALTY_POINTS_CANNOT_BE_NEGATIVE_OR_ZERO);
    }
    return new LoyaltyPoints(this.value + points);
  }

  public LoyaltyPoints add(LoyaltyPoints points) {
    return new LoyaltyPoints(this.value + points.value());
  }

  public String toString() {
    return String.valueOf(this.value);
  }

  public int compareTo(LoyaltyPoints other) {
    return this.value.compareTo(other.value());
  }
}
