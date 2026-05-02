package com.algaworks.algashop.ordering.domain.valueobject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class BirthDateTest {

  @Test
  void shouldCreateBirthDate() {
    var birthDate = new BirthDate(LocalDate.of(2000, 1, 1));

    assertThat(birthDate.value()).isEqualTo(LocalDate.of(2000, 1, 1));
  }

  @Test
  void shouldThrowExceptionWhenBirthDateIsInFuture() {
    assertThatThrownBy(() -> new BirthDate(LocalDate.of(2050, 1, 1)))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void shouldThrowExceptionWhenBirthDateIsNull() {
    assertThatThrownBy(() -> new BirthDate(null))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  void shouldReturnAge() {
    var birthDate = new BirthDate(LocalDate.of(1976, 10, 9));

    assertThat(birthDate.age()).isEqualTo(49);
  }

  @Test
  void shouldReturnCorrectAgeWhenBirthdayIsToday() {
    var today = LocalDate.now();
    var birthDate = new BirthDate(today.minusYears(30));

    assertThat(birthDate.age()).isEqualTo(30);
  }

  @Test
  void shouldReturnAgeMinusOneWhenBirthdayNotYetOccurredThisYear() {
    var today = LocalDate.now();
    var birthDate = new BirthDate(today.plusDays(1).minusYears(30));

    assertThat(birthDate.age()).isEqualTo(29);
  }

  @Test
  void shouldReturnCorrectAgeWhenBirthdayOccurredYesterday() {
    var today = LocalDate.now();
    var birthDate = new BirthDate(today.minusDays(1).minusYears(30));

    assertThat(birthDate.age()).isEqualTo(30);
  }

  @Test
  void shouldReturnZeroWhenBornThisYearBeforeToday() {
    var birthDate = new BirthDate(LocalDate.now().minusDays(1));

    assertThat(birthDate.age()).isEqualTo(0);
  }

  @Test
  void shouldAcceptTodayAsBirthDate() {
    assertThatCode(() -> new BirthDate(LocalDate.now())).doesNotThrowAnyException();
  }
}
