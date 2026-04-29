package com.algaworks.algashop.ordering.domain.valueobject;

import static com.algaworks.algashop.ordering.domain.messages.ErrorMessages.LOYALTY_POINTS_CANNOT_BE_NEGATIVE_OR_ZERO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LoyaltyPointsTest {

  @Test
  @DisplayName("Should create LoyaltyPoints with default value zero")
  void shouldCreateLoyaltyPointsWithDefaultValueZero() {
    LoyaltyPoints loyaltyPoints = new LoyaltyPoints();

    assertThat(loyaltyPoints.value()).isZero();
  }

  @Test
  @DisplayName("Should create LoyaltyPoints with positive value")
  void shouldCreateLoyaltyPointsWithPositiveValue() {
    LoyaltyPoints loyaltyPoints = new LoyaltyPoints(100);

    assertThat(loyaltyPoints.value()).isEqualTo(100);
  }

  @Test
  @DisplayName("Should create LoyaltyPoints with zero value")
  void shouldCreateLoyaltyPointsWithZeroValue() {
    LoyaltyPoints loyaltyPoints = new LoyaltyPoints(0);

    assertThat(loyaltyPoints.value()).isZero();
  }

  @Test
  @DisplayName("Should throw exception when value is null")
  void shouldThrowExceptionWhenValueIsNull() {
    assertThatThrownBy(() -> new LoyaltyPoints(null))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should throw exception when value is negative")
  void shouldThrowExceptionWhenValueIsNegative() {
    assertThatThrownBy(() -> new LoyaltyPoints(-10))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(LOYALTY_POINTS_CANNOT_BE_NEGATIVE_OR_ZERO);
  }

  @Test
  @DisplayName("Should add integer points and return new LoyaltyPoints")
  void shouldAddIntegerPointsAndReturnNewLoyaltyPoints() {
    LoyaltyPoints loyaltyPoints = new LoyaltyPoints(50);

    LoyaltyPoints result = loyaltyPoints.add(25);

    assertThat(result.value()).isEqualTo(75);
    assertThat(loyaltyPoints.value()).isEqualTo(50);
  }

  @Test
  @DisplayName("Should add LoyaltyPoints and return new LoyaltyPoints")
  void shouldAddLoyaltyPointsAndReturnNewLoyaltyPoints() {
    LoyaltyPoints loyaltyPoints = new LoyaltyPoints(50);
    LoyaltyPoints pointsToAdd = new LoyaltyPoints(30);

    LoyaltyPoints result = loyaltyPoints.add(pointsToAdd);

    assertThat(result.value()).isEqualTo(80);
    assertThat(loyaltyPoints.value()).isEqualTo(50);
    assertThat(pointsToAdd.value()).isEqualTo(30);
  }

  @Test
  @DisplayName("Should throw exception when adding zero points")
  void shouldThrowExceptionWhenAddingZeroPoints() {
    LoyaltyPoints loyaltyPoints = new LoyaltyPoints(100);

    assertThatThrownBy(() -> loyaltyPoints.add(0))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(LOYALTY_POINTS_CANNOT_BE_NEGATIVE_OR_ZERO);
  }

  @Test
  @DisplayName("Should convert to string correctly")
  void shouldConvertToStringCorrectly() {
    LoyaltyPoints loyaltyPoints = new LoyaltyPoints(150);

    assertThat(loyaltyPoints.toString()).isEqualTo("150");
  }

  @Test
  @DisplayName("Should convert zero to string correctly")
  void shouldConvertZeroToStringCorrectly() {
    LoyaltyPoints loyaltyPoints = new LoyaltyPoints();

    assertThat(loyaltyPoints.toString()).isEqualTo("0");
  }

  @Test
  @DisplayName("Should be equal when same value")
  void shouldBeEqualWhenSameValue() {
    LoyaltyPoints loyaltyPoints1 = new LoyaltyPoints(100);
    LoyaltyPoints loyaltyPoints2 = new LoyaltyPoints(100);

    assertThat(loyaltyPoints1).isEqualTo(loyaltyPoints2);
    assertThat(loyaltyPoints1.hashCode()).isEqualTo(loyaltyPoints2.hashCode());
  }

  @Test
  @DisplayName("Should not be equal when different value")
  void shouldNotBeEqualWhenDifferentValue() {
    LoyaltyPoints loyaltyPoints1 = new LoyaltyPoints(100);
    LoyaltyPoints loyaltyPoints2 = new LoyaltyPoints(200);

    assertThat(loyaltyPoints1).isNotEqualTo(loyaltyPoints2);
  }

  @Test
  @DisplayName("Should not be equal to null")
  void shouldNotBeEqualToNull() {
    LoyaltyPoints loyaltyPoints = new LoyaltyPoints(100);

    assertThat(loyaltyPoints).isNotEqualTo(null);
  }

  @Test
  @DisplayName("Should not be equal to different type")
  void shouldNotBeEqualToDifferentType() {
    LoyaltyPoints loyaltyPoints = new LoyaltyPoints(100);

    assertThat(loyaltyPoints).isNotEqualTo(100);
  }

  @Test
  @DisplayName("Should return zero when comparing equal values")
  void shouldReturnZeroWhenComparingEqualValues() {
    LoyaltyPoints loyaltyPoints1 = new LoyaltyPoints(100);
    LoyaltyPoints loyaltyPoints2 = new LoyaltyPoints(100);

    assertThat(loyaltyPoints1.compareTo(loyaltyPoints2)).isZero();
  }

  @Test
  @DisplayName("Should return negative when value is less than other")
  void shouldReturnNegativeWhenValueIsLessThanOther() {
    LoyaltyPoints loyaltyPoints1 = new LoyaltyPoints(50);
    LoyaltyPoints loyaltyPoints2 = new LoyaltyPoints(100);

    assertThat(loyaltyPoints1.compareTo(loyaltyPoints2)).isNegative();
  }

  @Test
  @DisplayName("Should return positive when value is greater than other")
  void shouldReturnPositiveWhenValueIsGreaterThanOther() {
    LoyaltyPoints loyaltyPoints1 = new LoyaltyPoints(150);
    LoyaltyPoints loyaltyPoints2 = new LoyaltyPoints(100);

    assertThat(loyaltyPoints1.compareTo(loyaltyPoints2)).isPositive();
  }
}
