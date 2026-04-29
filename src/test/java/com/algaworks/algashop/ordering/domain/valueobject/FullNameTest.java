package com.algaworks.algashop.ordering.domain.valueobject;

import static com.algaworks.algashop.ordering.domain.messages.ErrorMessages.FULL_NAME_CANNOT_BE_BLANK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FullNameTest {

  @Test
  @DisplayName("Should create FullName with valid first and last name")
  void shouldCreateFullNameWithValidFirstAndLastName() {
    FullName fullName = new FullName("João", "Silva");

    assertThat(fullName.firstName()).isEqualTo("João");
    assertThat(fullName.lastName()).isEqualTo("Silva");
  }

  @Test
  @DisplayName("Should trim whitespace from first and last name")
  void shouldTrimWhitespaceFromFirstAndLastName() {
    FullName fullName = new FullName("  João  ", "  Silva  ");

    assertThat(fullName.firstName()).isEqualTo("João");
    assertThat(fullName.lastName()).isEqualTo("Silva");
  }

  @Test
  @DisplayName("Should throw exception when first name is null")
  void shouldThrowExceptionWhenFirstNameIsNull() {
    assertThatThrownBy(() -> new FullName(null, "Silva"))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should throw exception when last name is null")
  void shouldThrowExceptionWhenLastNameIsNull() {
    assertThatThrownBy(() -> new FullName("João", null))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should throw exception when first name is blank")
  void shouldThrowExceptionWhenFirstNameIsBlank() {
    assertThatThrownBy(() -> new FullName("   ", "Silva"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(FULL_NAME_CANNOT_BE_BLANK);
  }

  @Test
  @DisplayName("Should throw exception when last name is blank")
  void shouldThrowExceptionWhenLastNameIsBlank() {
    assertThatThrownBy(() -> new FullName("João", "   "))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(FULL_NAME_CANNOT_BE_BLANK);
  }

  @Test
  @DisplayName("Should throw exception when first name is empty")
  void shouldThrowExceptionWhenFirstNameIsEmpty() {
    assertThatThrownBy(() -> new FullName("", "Silva"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(FULL_NAME_CANNOT_BE_BLANK);
  }

  @Test
  @DisplayName("Should throw exception when last name is empty")
  void shouldThrowExceptionWhenLastNameIsEmpty() {
    assertThatThrownBy(() -> new FullName("João", ""))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(FULL_NAME_CANNOT_BE_BLANK);
  }

  @Test
  @DisplayName("Should convert to string correctly")
  void shouldConvertToStringCorrectly() {
    FullName fullName = new FullName("João", "Silva");

    assertThat(fullName.toString()).isEqualTo("João Silva");
  }

  @Test
  @DisplayName("Should be equal when same first and last name")
  void shouldBeEqualWhenSameFirstAndLastName() {
    FullName fullName1 = new FullName("João", "Silva");
    FullName fullName2 = new FullName("João", "Silva");

    assertThat(fullName1).isEqualTo(fullName2);
    assertThat(fullName1.hashCode()).isEqualTo(fullName2.hashCode());
  }

  @Test
  @DisplayName("Should not be equal when different first name")
  void shouldNotBeEqualWhenDifferentFirstName() {
    FullName fullName1 = new FullName("João", "Silva");
    FullName fullName2 = new FullName("Maria", "Silva");

    assertThat(fullName1).isNotEqualTo(fullName2);
  }

  @Test
  @DisplayName("Should not be equal when different last name")
  void shouldNotBeEqualWhenDifferentLastName() {
    FullName fullName1 = new FullName("João", "Silva");
    FullName fullName2 = new FullName("João", "Santos");

    assertThat(fullName1).isNotEqualTo(fullName2);
  }

  @Test
  @DisplayName("Should not be equal to null")
  void shouldNotBeEqualToNull() {
    FullName fullName = new FullName("João", "Silva");

    assertThat(fullName).isNotEqualTo(null);
  }

  @Test
  @DisplayName("Should not be equal to different type")
  void shouldNotBeEqualToDifferentType() {
    FullName fullName = new FullName("João", "Silva");

    assertThat(fullName).isNotEqualTo("João Silva");
  }
}
