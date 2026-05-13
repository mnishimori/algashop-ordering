package com.algaworks.algashop.ordering.domain.valueobject.id;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.hypersistence.tsid.TSID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderIdTest {

  private static final TSID TEST_TSID = TSID.from(123456789L);

  @Test
  @DisplayName("Should create OrderId with no-arg constructor")
  void shouldCreateOrderIdWithNoArgConstructor() {
    OrderId orderId = new OrderId();

    assertThat(orderId.value()).isNotNull();
    assertThat(orderId.value()).isInstanceOf(io.hypersistence.tsid.TSID.class);
  }

  @Test
  @DisplayName("Should create OrderId with provided TSID")
  void shouldCreateOrderIdWithProvidedTsid() {
    OrderId orderId = new OrderId(TEST_TSID);

    assertThat(orderId.value()).isEqualTo(TEST_TSID);
  }

  @Test
  @DisplayName("Should create OrderId with provided Long")
  void shouldCreateOrderIdWithProvidedLong() {
    OrderId orderId = new OrderId(123456789L);

    assertThat(orderId.value()).isEqualTo(TEST_TSID);
  }

  @Test
  @DisplayName("Should create OrderId with provided String")
  void shouldCreateOrderIdWithProvidedString() {
    String tsidString = TEST_TSID.toString();
    OrderId orderId = new OrderId(tsidString);

    assertThat(orderId.value()).isEqualTo(TEST_TSID);
  }

  @Test
  @DisplayName("Should throw exception when TSID is null")
  void shouldThrowExceptionWhenTsidIsNull() {
    assertThatThrownBy(() -> new OrderId((TSID) null))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should throw exception when Long is null")
  void shouldThrowExceptionWhenLongIsNull() {
    assertThatThrownBy(() -> new OrderId((Long) null))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should throw exception when String is null")
  void shouldThrowExceptionWhenStringIsNull() {
    assertThatThrownBy(() -> new OrderId((String) null))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("Should convert to string correctly")
  void shouldConvertToStringCorrectly() {
    OrderId orderId = new OrderId(TEST_TSID);

    assertThat(orderId.toString()).isEqualTo(TEST_TSID.toString());
  }

  @Test
  @DisplayName("Should be equal when same TSID value")
  void shouldBeEqualWhenSameTsidValue() {
    OrderId orderId1 = new OrderId(TEST_TSID);
    OrderId orderId2 = new OrderId(TEST_TSID);

    assertThat(orderId1).isEqualTo(orderId2);
    assertThat(orderId1.hashCode()).isEqualTo(orderId2.hashCode());
  }

  @Test
  @DisplayName("Should not be equal when different TSID value")
  void shouldNotBeEqualWhenDifferentTsidValue() {
    OrderId orderId1 = new OrderId(TEST_TSID);
    OrderId orderId2 = new OrderId(TSID.from(987654321L));

    assertThat(orderId1).isNotEqualTo(orderId2);
  }

  @Test
  @DisplayName("Should not be equal to null")
  void shouldNotBeEqualToNull() {
    OrderId orderId = new OrderId(TEST_TSID);

    assertThat(orderId).isNotEqualTo(null);
  }

  @Test
  @DisplayName("Should not be equal to different type")
  void shouldNotBeEqualToDifferentType() {
    OrderId orderId = new OrderId(TEST_TSID);

    assertThat(orderId).isNotEqualTo(TEST_TSID.toString());
  }
}
