package com.algaworks.algashop.ordering.domain.valueobject.id;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.hypersistence.tsid.TSID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderItemIdTest {

  private static final TSID TEST_TSID = TSID.from(123456789L);

  @Test
  @DisplayName("Should create OrderItemId with provided TSID")
  void shouldCreateOrderItemIdWithProvidedTsid() {
    OrderItemId orderItemId = new OrderItemId(TEST_TSID);

    assertThat(orderItemId.value()).isEqualTo(TEST_TSID);
  }

  @Test
  @DisplayName("Should create OrderItemId with provided Long")
  void shouldCreateOrderItemIdWithProvidedLong() {
    OrderItemId orderItemId = new OrderItemId(123456789L);

    assertThat(orderItemId.value()).isEqualTo(TEST_TSID);
  }

  @Test
  @DisplayName("Should create OrderItemId with provided String")
  void shouldCreateOrderItemIdWithProvidedString() {
    String tsidString = TEST_TSID.toString();
    OrderItemId orderItemId = new OrderItemId(tsidString);

    assertThat(orderItemId.value()).isEqualTo(TEST_TSID);
  }

  @Test
  @DisplayName("Should throw exception when TSID is null")
  void shouldThrowExceptionWhenTsidIsNull() {
    assertThatThrownBy(() -> new OrderItemId((TSID) null))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should throw exception when Long is null")
  void shouldThrowExceptionWhenLongIsNull() {
    assertThatThrownBy(() -> new OrderItemId((Long) null))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should throw exception when String is null")
  void shouldThrowExceptionWhenStringIsNull() {
    assertThatThrownBy(() -> new OrderItemId((String) null))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("Should convert to string correctly")
  void shouldConvertToStringCorrectly() {
    OrderItemId orderItemId = new OrderItemId(TEST_TSID);

    assertThat(orderItemId.toString()).isEqualTo(TEST_TSID.toString());
  }

  @Test
  @DisplayName("Should be equal when same TSID value")
  void shouldBeEqualWhenSameTsidValue() {
    OrderItemId orderItemId1 = new OrderItemId(TEST_TSID);
    OrderItemId orderItemId2 = new OrderItemId(TEST_TSID);

    assertThat(orderItemId1).isEqualTo(orderItemId2);
    assertThat(orderItemId1.hashCode()).isEqualTo(orderItemId2.hashCode());
  }

  @Test
  @DisplayName("Should not be equal when different TSID value")
  void shouldNotBeEqualWhenDifferentTsidValue() {
    OrderItemId orderItemId1 = new OrderItemId(TEST_TSID);
    OrderItemId orderItemId2 = new OrderItemId(TSID.from(987654321L));

    assertThat(orderItemId1).isNotEqualTo(orderItemId2);
  }

  @Test
  @DisplayName("Should not be equal to null")
  void shouldNotBeEqualToNull() {
    OrderItemId orderItemId = new OrderItemId(TEST_TSID);

    assertThat(orderItemId).isNotEqualTo(null);
  }

  @Test
  @DisplayName("Should not be equal to different type")
  void shouldNotBeEqualToDifferentType() {
    OrderItemId orderItemId = new OrderItemId(TEST_TSID);

    assertThat(orderItemId).isNotEqualTo(TEST_TSID.toString());
  }
}
