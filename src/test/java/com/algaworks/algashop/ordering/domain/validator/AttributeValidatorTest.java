package com.algaworks.algashop.ordering.domain.validator;

import static com.algaworks.algashop.ordering.domain.validator.AttributeValidator.ATTRIBUTE_CANNOT_BE_NULL;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class AttributeValidatorTest {

  @Test
  @DisplayName("Should validate attribute with valid value")
  void shouldValidateAttributeWithValidValue() {
    assertThatCode(() -> AttributeValidator.validateAttributeNotBlank("street", "Bourbon Street"))
        .doesNotThrowAnyException();
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "valid value",
      "123",
      "mixed-value_123",
      "   leading spaces trimmed",
      "trailing spaces trimmed   "
  })
  @DisplayName("Should accept various valid attribute values")
  void shouldAcceptVariousValidAttributeValues(String value) {
    assertThatCode(() -> AttributeValidator.validateAttributeNotBlank("attribute", value))
        .doesNotThrowAnyException();
  }

  @Test
  @DisplayName("Should throw exception when attribute value is null")
  void shouldThrowExceptionWhenAttributeValueIsNull() {
    assertThatThrownBy(() -> AttributeValidator.validateAttributeNotBlank("street", null))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should throw exception when attribute value is blank")
  void shouldThrowExceptionWhenAttributeValueIsBlank() {
    assertThatThrownBy(() -> AttributeValidator.validateAttributeNotBlank("street", "   "))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(ATTRIBUTE_CANNOT_BE_NULL.formatted("street"));
  }

  @Test
  @DisplayName("Should throw exception when attribute value is empty")
  void shouldThrowExceptionWhenAttributeValueIsEmpty() {
    assertThatThrownBy(() -> AttributeValidator.validateAttributeNotBlank("city", ""))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(ATTRIBUTE_CANNOT_BE_NULL.formatted("city"));
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "   ",
      "  ",
      "\t",
      "\n",
      "\t\n  "
  })
  @DisplayName("Should throw exception for various blank values")
  void shouldThrowExceptionForVariousBlankValues(String value) {
    assertThatThrownBy(() -> AttributeValidator.validateAttributeNotBlank("field", value))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(ATTRIBUTE_CANNOT_BE_NULL.formatted("field"));
  }

  @Test
  @DisplayName("Should include attribute name in error message")
  void shouldIncludeAttributeNameInErrorMessage() {
    assertThatThrownBy(() -> AttributeValidator.validateAttributeNotBlank("neighborhood", "   "))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Attribute neighborhood cannot be null");
  }

  @Test
  @DisplayName("Should validate different attribute names")
  void shouldValidateDifferentAttributeNames() {
    assertThatCode(() -> {
      AttributeValidator.validateAttributeNotBlank("street", "Main Street");
      AttributeValidator.validateAttributeNotBlank("number", "123");
      AttributeValidator.validateAttributeNotBlank("neighborhood", "Downtown");
      AttributeValidator.validateAttributeNotBlank("city", "New York");
      AttributeValidator.validateAttributeNotBlank("state", "NY");
      AttributeValidator.validateAttributeNotBlank("zipCode", "12345-678");
    }).doesNotThrowAnyException();
  }

  @Test
  @DisplayName("Should throw exception with correct message for each attribute")
  void shouldThrowExceptionWithCorrectMessageForEachAttribute() {
    assertThatThrownBy(() -> AttributeValidator.validateAttributeNotBlank("city", ""))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Attribute city cannot be null");

    assertThatThrownBy(() -> AttributeValidator.validateAttributeNotBlank("state", "   "))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Attribute state cannot be null");

    assertThatThrownBy(() -> AttributeValidator.validateAttributeNotBlank("number", "\t"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Attribute number cannot be null");
  }
}
