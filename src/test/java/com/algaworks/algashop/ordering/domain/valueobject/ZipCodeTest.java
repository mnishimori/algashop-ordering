package com.algaworks.algashop.ordering.domain.valueobject;

import static com.algaworks.algashop.ordering.domain.messages.ErrorMessages.ZIP_CODE_IS_INVALID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ZipCodeTest {

  @Test
  @DisplayName("Should create zip code with valid format")
  void shouldCreateZipCodeWithValidFormat() {
    var zipCode = new ZipCode("12345-678");

    assertThat(zipCode.value()).isEqualTo("12345-678");
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "00000-000",
      "12345-678",
      "99999-999",
      "87654-321"
  })
  @DisplayName("Should accept various valid zip code formats")
  void shouldAcceptVariousValidZipCodeFormats(String zipCodeValue) {
    assertThatCode(() -> new ZipCode(zipCodeValue))
        .doesNotThrowAnyException();
  }

  @Test
  @DisplayName("Should return zip code value as string")
  void shouldReturnZipCodeValueAsString() {
    var zipCode = new ZipCode("12345-678");

    assertThat(zipCode.toString()).isEqualTo("12345-678");
  }

  @Test
  @DisplayName("Should throw exception when zip code is null")
  void shouldThrowExceptionWhenZipCodeIsNull() {
    assertThatThrownBy(() -> new ZipCode(null))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should throw exception when zip code is blank")
  void shouldThrowExceptionWhenZipCodeIsBlank() {
    assertThatThrownBy(() -> new ZipCode("   "))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Attribute ZipCode cannot be null");
  }

  @Test
  @DisplayName("Should throw exception when zip code is empty")
  void shouldThrowExceptionWhenZipCodeIsEmpty() {
    assertThatThrownBy(() -> new ZipCode(""))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Attribute ZipCode cannot be null");
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "12345678",
      "1234-567",
      "12345-67",
      "12345-6789",
      "abcde-fgh",
      "12345-abc",
      "12345 678",
      "12345--678",
      "12345_678"
  })
  @DisplayName("Should throw exception for invalid zip code formats")
  void shouldThrowExceptionForInvalidZipCodeFormats(String zipCodeValue) {
    assertThatThrownBy(() -> new ZipCode(zipCodeValue))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(ZIP_CODE_IS_INVALID);
  }

  @Test
  @DisplayName("Should be equal when same value")
  void shouldBeEqualWhenSameValue() {
    var zipCode1 = new ZipCode("12345-678");
    var zipCode2 = new ZipCode("12345-678");

    assertThat(zipCode1).isEqualTo(zipCode2);
    assertThat(zipCode1.hashCode()).isEqualTo(zipCode2.hashCode());
  }

  @Test
  @DisplayName("Should not be equal when different value")
  void shouldNotBeEqualWhenDifferentValue() {
    var zipCode1 = new ZipCode("12345-678");
    var zipCode2 = new ZipCode("98765-432");

    assertThat(zipCode1).isNotEqualTo(zipCode2);
  }

  @Test
  @DisplayName("Should not be equal when null")
  void shouldNotBeEqualWhenNull() {
    var zipCode = new ZipCode("12345-678");

    assertThat(zipCode).isNotEqualTo(null);
  }

  @Test
  @DisplayName("Should not be equal when different class")
  void shouldNotBeEqualWhenDifferentClass() {
    var zipCode = new ZipCode("12345-678");

    assertThat(zipCode).isNotEqualTo("12345-678");
  }
}
