package com.algaworks.algashop.ordering.domain.valueobject;

import static com.algaworks.algashop.ordering.domain.validator.AttributeValidator.validateAttributeNotBlank;

import java.util.Objects;
import lombok.Builder;

public record Address(
    String street,
    String number,
    String complement,
    String neighborhood,
    String city,
    String state,
    ZipCode zipCode
) {

  @Builder(toBuilder = true)
  public Address(String street, String number, String complement, String neighborhood, String city, String state,
      ZipCode zipCode) {
    validateAttributeNotBlank("street", street);
    validateAttributeNotBlank("number", number);
    validateAttributeNotBlank("neighborhood", neighborhood);
    validateAttributeNotBlank("city", city);
    validateAttributeNotBlank("state", state);
    Objects.requireNonNull(zipCode);
    this.street = street;
    this.number = number;
    this.complement = complement;
    this.neighborhood = neighborhood;
    this.city = city;
    this.state = state;
    this.zipCode = zipCode;
  }
}
