package com.algaworks.algashop.ordering.domain.valueobject;

import static com.algaworks.algashop.ordering.domain.messages.ErrorMessages.PRODUCT_NAME_CANNOT_BE_NULL_OR_EMPTY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class ProductNameTest {

  private static final String PRODUCT_NAME = "Product Name";

  @Test
  @DisplayName("Should create ProductName with valid value")
  void shouldCreateProductName() {
    var productName = new ProductName(PRODUCT_NAME);

    assertThat(productName).isNotNull();
    assertThat(productName.value()).isEqualTo(PRODUCT_NAME);
    assertThat(productName.toString()).isEqualTo(PRODUCT_NAME);
  }

  @Test
  @DisplayName("Should preserve original value including spaces")
  void shouldPreserveOriginalValueIncludingSpaces() {
    var nameWithSpaces = " Product Name with spaces ";

    var productName = new ProductName(nameWithSpaces);

    assertThat(productName.value()).isEqualTo(nameWithSpaces);
  }

  @ParameterizedTest
  @DisplayName("Should throw exception when product name is blank")
  @NullAndEmptySource
  @ValueSource(strings = {"   "})
  void shouldThrowExceptionWhenProductNameIsBlank(String value) {
    assertThatThrownBy(() -> new ProductName(value))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(PRODUCT_NAME_CANNOT_BE_NULL_OR_EMPTY);
  }

  @Test
  @DisplayName("Should be equal when same value")
  void shouldBeEqualWhenSameValue() {
    var productName1 = new ProductName(PRODUCT_NAME);
    var productName2 = new ProductName(PRODUCT_NAME);

    assertThat(productName1).isEqualTo(productName2);
    assertThat(productName1.hashCode()).isEqualTo(productName2.hashCode());
  }

  @Test
  @DisplayName("Should not be equal when different value")
  void shouldNotBeEqualWhenDifferentValue() {
    var productName1 = new ProductName(PRODUCT_NAME);
    var productName2 = new ProductName("Different Name");

    assertThat(productName1).isNotEqualTo(productName2);
  }

  @Test
  @DisplayName("Should not be equal to null")
  void shouldNotBeEqualToNull() {
    var productName = new ProductName(PRODUCT_NAME);

    assertThat(productName).isNotEqualTo(null);
  }

  @Test
  @DisplayName("Should not be equal to different type")
  void shouldNotBeEqualToDifferentType() {
    var productName = new ProductName(PRODUCT_NAME);

    assertThat(productName).isNotEqualTo(PRODUCT_NAME);
  }
}