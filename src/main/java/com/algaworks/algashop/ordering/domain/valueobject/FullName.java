package com.algaworks.algashop.ordering.domain.valueobject;

import static com.algaworks.algashop.ordering.domain.messages.ErrorMessages.FULL_NAME_CANNOT_BE_BLANK;

import java.util.Objects;

public record FullName(String firstName, String lastName) {

  public FullName(String firstName, String lastName){
    Objects.requireNonNull(firstName);
    Objects.requireNonNull(lastName);
    if (firstName.isBlank() || lastName.isBlank()) {
      throw new IllegalArgumentException(FULL_NAME_CANNOT_BE_BLANK);
    }
    this.firstName = firstName.trim();
    this.lastName = lastName.trim();
  }

  @Override
  public String toString() {
    return firstName + " " + lastName;
  }
}
