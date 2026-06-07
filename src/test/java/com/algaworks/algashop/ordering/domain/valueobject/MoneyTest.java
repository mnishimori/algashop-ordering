package com.algaworks.algashop.ordering.domain.valueobject;

import static com.algaworks.algashop.ordering.domain.model.messages.ErrorMessages.VALUE_CANNOT_BE_NEGATIVE;
import static com.algaworks.algashop.ordering.domain.model.messages.ErrorMessages.VALUE_CANNOT_BE_NULL_OR_EMPTY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.algaworks.algashop.ordering.domain.model.valueobject.Money;
import com.algaworks.algashop.ordering.domain.model.valueobject.Quantity;
import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MoneyTest {

  @Test
  @DisplayName("Should create Money with positive BigDecimal value")
  void shouldCreateMoneyWithPositiveBigDecimalValue() {
    var money = new Money(new BigDecimal("10.50"));

    assertThat(money.value()).isEqualByComparingTo(new BigDecimal("10.50"));
  }

  @Test
  @DisplayName("Should create Money with zero BigDecimal value")
  void shouldCreateMoneyWithZeroBigDecimalValue() {
    var money = new Money(BigDecimal.ZERO);

    assertThat(money.value()).isEqualByComparingTo(BigDecimal.ZERO);
  }

  @Test
  @DisplayName("Should provide ZERO constant")
  void shouldProvideZeroConstant() {
    var zeroMoney = Money.ZERO;

    assertThat(zeroMoney.value()).isEqualByComparingTo(BigDecimal.ZERO);
  }

  @Test
  @DisplayName("Should scale value to two decimal places")
  void shouldScaleValueToTwoDecimalPlaces() {
    var money = new Money(new BigDecimal("10"));

    assertThat(money.value().scale()).isEqualTo(2);
    assertThat(money.value()).isEqualByComparingTo(new BigDecimal("10.00"));
  }

  @Test
  @DisplayName("Should create Money from string value")
  void shouldCreateMoneyFromStringValue() {
    var money = new Money("25.99");

    assertThat(money.value()).isEqualByComparingTo(new BigDecimal("25.99"));
  }

  @Test
  @DisplayName("Should throw exception when BigDecimal value is null")
  void shouldThrowExceptionWhenBigDecimalValueIsNull() {
    assertThatThrownBy(() -> new Money((BigDecimal) null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(VALUE_CANNOT_BE_NULL_OR_EMPTY);
  }

  @Test
  @DisplayName("Should throw exception when string value is null")
  void shouldThrowExceptionWhenStringValueIsNull() {
    assertThatThrownBy(() -> new Money((String) null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(VALUE_CANNOT_BE_NULL_OR_EMPTY);
  }

  @Test
  @DisplayName("Should throw exception when string value is empty")
  void shouldThrowExceptionWhenStringValueIsEmpty() {
    assertThatThrownBy(() -> new Money(""))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(VALUE_CANNOT_BE_NULL_OR_EMPTY);
  }

  @Test
  @DisplayName("Should throw exception when BigDecimal value is negative")
  void shouldThrowExceptionWhenBigDecimalValueIsNegative() {
    assertThatThrownBy(() -> new Money(new BigDecimal("-0.01")))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(VALUE_CANNOT_BE_NEGATIVE);
  }

  @Test
  @DisplayName("Should throw exception when string value is negative")
  void shouldThrowExceptionWhenStringValueIsNegative() {
    assertThatThrownBy(() -> new Money("-5.00"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(VALUE_CANNOT_BE_NEGATIVE);
  }

  @Test
  @DisplayName("Should add two Money values and return new Money")
  void shouldAddTwoMoneyValuesAndReturnNewMoney() {
    var money = new Money("10.00");
    var other = new Money("5.50");

    var result = money.add(other);

    assertThat(result.value()).isEqualByComparingTo(new BigDecimal("15.50"));
    assertThat(money.value()).isEqualByComparingTo(new BigDecimal("10.00"));
    assertThat(other.value()).isEqualByComparingTo(new BigDecimal("5.50"));
  }

  @Test
  @DisplayName("Should multiply Money by integer quantity and return new Money")
  void shouldMultiplyMoneyByIntegerQuantityAndReturnNewMoney() {
    var money = new Money("3.00");

    var result = money.multiply(4);

    assertThat(result.value()).isEqualByComparingTo(new BigDecimal("12.00"));
    assertThat(money.value()).isEqualByComparingTo(new BigDecimal("3.00"));
  }

  @Test
  @DisplayName("Should multiply Money by Quantity and return new Money")
  void shouldMultiplyMoneyByQuantityAndReturnNewMoney() {
    var money = new Money("3.00");
    var quantity = new Quantity(new BigDecimal("4"));

    var result = money.multiply(quantity);

    assertThat(result.value()).isEqualByComparingTo(new BigDecimal("12.00"));
    assertThat(money.value()).isEqualByComparingTo(new BigDecimal("3.00"));
  }

  @Test
  @DisplayName("Should divide Money by other Money and return new Money")
  void shouldDivideMoneyByOtherMoneyAndReturnNewMoney() {
    var money = new Money("10.00");
    var divisor = new Money("4.00");

    var result = money.divide(divisor);

    assertThat(result.value()).isEqualByComparingTo(new BigDecimal("2.50"));
    assertThat(money.value()).isEqualByComparingTo(new BigDecimal("10.00"));
    assertThat(divisor.value()).isEqualByComparingTo(new BigDecimal("4.00"));
  }

  @Test
  @DisplayName("Should round division result using HALF_EVEN to two decimal places")
  void shouldRoundDivisionResultUsingHalfEvenToTwoDecimalPlaces() {
    var money = new Money("1.00");
    var divisor = new Money("3.00");

    var result = money.divide(divisor);

    assertThat(result.value()).isEqualByComparingTo(new BigDecimal("0.33"));
  }

  @Test
  @DisplayName("Should convert to string correctly")
  void shouldConvertToStringCorrectly() {
    var money = new Money("19.99");

    assertThat(money.toString()).isEqualTo("19.99");
  }

  @Test
  @DisplayName("Should be equal when same value")
  void shouldBeEqualWhenSameValue() {
    var money1 = new Money("50.00");
    var money2 = new Money("50.00");

    assertThat(money1).isEqualTo(money2);
    assertThat(money1.hashCode()).isEqualTo(money2.hashCode());
  }

  @Test
  @DisplayName("Should not be equal when different value")
  void shouldNotBeEqualWhenDifferentValue() {
    var money1 = new Money("50.00");
    var money2 = new Money("99.00");

    assertThat(money1).isNotEqualTo(money2);
  }

  @Test
  @DisplayName("Should not be equal to null")
  void shouldNotBeEqualToNull() {
    var money = new Money("50.00");

    assertThat(money).isNotEqualTo(null);
  }

  @Test
  @DisplayName("Should return zero when comparing equal values")
  void shouldReturnZeroWhenComparingEqualValues() {
    var money1 = new Money("30.00");
    var money2 = new Money("30.00");

    assertThat(money1.compareTo(money2)).isZero();
  }

  @Test
  @DisplayName("Should return negative when value is less than other")
  void shouldReturnNegativeWhenValueIsLessThanOther() {
    var money1 = new Money("10.00");
    var money2 = new Money("20.00");

    assertThat(money1.compareTo(money2)).isNegative();
  }

  @Test
  @DisplayName("Should return positive when value is greater than other")
  void shouldReturnPositiveWhenValueIsGreaterThanOther() {
    var money1 = new Money("20.00");
    var money2 = new Money("10.00");

    assertThat(money1.compareTo(money2)).isPositive();
  }
}