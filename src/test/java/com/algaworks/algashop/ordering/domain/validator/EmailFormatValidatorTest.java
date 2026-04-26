package com.algaworks.algashop.ordering.domain.validator;

import static com.algaworks.algashop.ordering.domain.messages.ErrorMessages.EMAIL_CANNOT_BE_BLANK;
import static com.algaworks.algashop.ordering.domain.messages.ErrorMessages.EMAIL_IS_INVALID;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class EmailFormatValidatorTest {

  @Test
  @DisplayName("Should validate valid email")
  void shouldValidateValidEmail() {
    assertThatCode(() -> EmailFormatValidator.validate("joao.silva@example.com"))
        .doesNotThrowAnyException();
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "user@domain.com",
      "user.name@domain.co.uk",
      "user+tag@domain.com",
      "user_name@domain.io",
      "123@domain.com",
      "user@subdomain.domain.com"
  })
  @DisplayName("Should accept various valid email formats")
  void shouldAcceptVariousValidEmailFormats(String email) {
    assertThatCode(() -> EmailFormatValidator.validate(email))
        .doesNotThrowAnyException();
  }

  @Test
  @DisplayName("Should throw exception when email is blank")
  void shouldThrowExceptionWhenEmailIsBlank() {
    assertThatThrownBy(() -> EmailFormatValidator.validate("   "))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(EMAIL_CANNOT_BE_BLANK);
  }

  @Test
  @DisplayName("Should throw exception when email is empty")
  void shouldThrowExceptionWhenEmailIsEmpty() {
    assertThatThrownBy(() -> EmailFormatValidator.validate(""))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(EMAIL_CANNOT_BE_BLANK);
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "invalid-email",
      "user@",
      "@domain.com",
      "user@domain",
      "user name@domain.com",
      "user@domain .com",
      "user..name@domain.com"
  })
  @DisplayName("Should throw exception for invalid email formats")
  void shouldThrowExceptionForInvalidEmailFormats(String email) {
    assertThatThrownBy(() -> EmailFormatValidator.validate(email))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(EMAIL_IS_INVALID);
  }
}