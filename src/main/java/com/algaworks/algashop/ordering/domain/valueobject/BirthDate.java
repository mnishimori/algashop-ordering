package com.algaworks.algashop.ordering.domain.valueobject;

import static com.algaworks.algashop.ordering.domain.messages.ErrorMessages.BIRTHDATE_MUST_IN_PAST;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public record BirthDate(LocalDate value) {

  public BirthDate(LocalDate value) {
    Objects.requireNonNull(value);
    if (value.isAfter(LocalDate.now())) {
      throw new IllegalArgumentException(BIRTHDATE_MUST_IN_PAST);
    }
    this.value = value;
  }

  public Integer age() {
    return (int) ChronoUnit.YEARS.between(value, LocalDate.now());
  }

  public String toString() {
    return value.toString();
  }
}
