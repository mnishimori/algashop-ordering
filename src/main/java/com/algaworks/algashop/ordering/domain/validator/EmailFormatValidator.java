package com.algaworks.algashop.ordering.domain.validator;

import static com.algaworks.algashop.ordering.domain.messages.ErrorMessages.EMAIL_CANNOT_BE_BLANK;
import static com.algaworks.algashop.ordering.domain.messages.ErrorMessages.EMAIL_IS_INVALID;

import org.apache.commons.validator.routines.EmailValidator;

public class EmailFormatValidator {

  private EmailFormatValidator() {
  }

  public static void validate(String email) {
    if (email.isBlank()) {
      throw new IllegalArgumentException(EMAIL_CANNOT_BE_BLANK);
    }
    if (!EmailValidator.getInstance().isValid(email)) {
      throw new IllegalArgumentException(EMAIL_IS_INVALID);
    }
  }
}
