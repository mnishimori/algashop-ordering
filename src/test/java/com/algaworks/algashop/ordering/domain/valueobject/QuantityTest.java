package com.algaworks.algashop.ordering.domain.valueobject;

import static com.algaworks.algashop.ordering.domain.model.messages.ErrorMessages.QUANTITY_MUST_BE_GREATER_THAN_ZERO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.algaworks.algashop.ordering.domain.model.valueobject.Quantity;
import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class QuantityTest {

  @Test
  @DisplayName("Should create Quantity with positive value")
  void shouldCreateQuantityWithPositiveValue() {
    var quantity = new Quantity(BigDecimal.TEN);

    assertThat(quantity.value()).isEqualByComparingTo(BigDecimal.TEN);
  }

  @Test
  @DisplayName("Should create Quantity with zero value")
  void shouldCreateQuantityWithZeroValue() {
    var quantity = new Quantity(BigDecimal.ZERO);

    assertThat(quantity.value()).isEqualByComparingTo(BigDecimal.ZERO);
  }

  @Test
  @DisplayName("Should provide ZERO constant")
  void shouldProvideZeroConstant() {
    var zeroQuantity = Quantity.ZERO;

    assertThat(zeroQuantity.value()).isEqualByComparingTo(BigDecimal.ZERO);
  }

  @Test
  @DisplayName("Should throw exception when value is null")
  void shouldThrowExceptionWhenValueIsNull() {
    assertThatThrownBy(() -> new Quantity(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(QUANTITY_MUST_BE_GREATER_THAN_ZERO);
  }

  @Test
  @DisplayName("Should throw exception when value is negative")
  void shouldThrowExceptionWhenValueIsNegative() {
    assertThatThrownBy(() -> new Quantity(new BigDecimal("-1")))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(QUANTITY_MUST_BE_GREATER_THAN_ZERO);
  }

  @Test
  @DisplayName("Should add two quantities and return new Quantity")
  void shouldAddTwoQuantitiesAndReturnNewQuantity() {
    var quantity = new Quantity(new BigDecimal("5"));
    var other = new Quantity(new BigDecimal("3"));

    var result = quantity.add(other);

    assertThat(result.value()).isEqualByComparingTo(new BigDecimal("8"));
    assertThat(quantity.value()).isEqualByComparingTo(new BigDecimal("5"));
    assertThat(other.value()).isEqualByComparingTo(new BigDecimal("3"));
  }

  @Test
  @DisplayName("Should convert to string correctly")
  void shouldConvertToStringCorrectly() {
    var quantity = new Quantity(new BigDecimal("10"));

    assertThat(quantity.toString()).isEqualTo("10");
  }

  @Test
  @DisplayName("Should be equal when same value")
  void shouldBeEqualWhenSameValue() {
    var quantity1 = new Quantity(new BigDecimal("5"));
    var quantity2 = new Quantity(new BigDecimal("5"));

    assertThat(quantity1).isEqualTo(quantity2);
    assertThat(quantity1.hashCode()).isEqualTo(quantity2.hashCode());
  }

  @Test
  @DisplayName("Should not be equal when different value")
  void shouldNotBeEqualWhenDifferentValue() {
    var quantity1 = new Quantity(new BigDecimal("5"));
    var quantity2 = new Quantity(new BigDecimal("10"));

    assertThat(quantity1).isNotEqualTo(quantity2);
  }

  @Test
  @DisplayName("Should not be equal to null")
  void shouldNotBeEqualToNull() {
    var quantity = new Quantity(new BigDecimal("5"));

    assertThat(quantity).isNotEqualTo(null);
  }

  @Test
  @DisplayName("Should not be equal to different type")
  void shouldNotBeEqualToDifferentType() {
    var quantity = new Quantity(new BigDecimal("5"));

    assertThat(quantity.value()).isNotEqualTo("5");
  }

  @Test
  @DisplayName("Should return zero when comparing equal values")
  void shouldReturnZeroWhenComparingEqualValues() {
    var quantity1 = new Quantity(new BigDecimal("5"));
    var quantity2 = new Quantity(new BigDecimal("5"));

    assertThat(quantity1.compareTo(quantity2)).isZero();
  }

  @Test
  @DisplayName("Should return negative when value is less than other")
  void shouldReturnNegativeWhenValueIsLessThanOther() {
    var quantity1 = new Quantity(new BigDecimal("3"));
    var quantity2 = new Quantity(new BigDecimal("10"));

    assertThat(quantity1.compareTo(quantity2)).isNegative();
  }

  @Test
  @DisplayName("Should return positive when value is greater than other")
  void shouldReturnPositiveWhenValueIsGreaterThanOther() {
    var quantity1 = new Quantity(new BigDecimal("10"));
    var quantity2 = new Quantity(new BigDecimal("3"));

    assertThat(quantity1.compareTo(quantity2)).isPositive();
  }
}
