package com.algaworks.algashop.ordering.domain.valueobject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CustomerIdTest {

  private static final UUID TEST_UUID = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");

  @Test
  @DisplayName("Should create CustomerId with generated UUID")
  void shouldCreateCustomerIdWithGeneratedUuid() {
    CustomerId customerId = new CustomerId();

    assertThat(customerId.value()).isNotNull();
    assertThat(customerId.value()).isInstanceOf(UUID.class);
  }

  @Test
  @DisplayName("Should create CustomerId with provided UUID")
  void shouldCreateCustomerIdWithProvidedUuid() {
    CustomerId customerId = new CustomerId(TEST_UUID);

    assertThat(customerId.value()).isEqualTo(TEST_UUID);
  }

  @Test
  @DisplayName("Should throw exception when UUID is null")
  void shouldThrowExceptionWhenUuidIsNull() {
    assertThatThrownBy(() -> new CustomerId(null))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should generate unique UUIDs for each instance")
  void shouldGenerateUniqueUuidsForEachInstance() {
    CustomerId customerId1 = new CustomerId();
    CustomerId customerId2 = new CustomerId();

    assertThat(customerId1.value()).isNotEqualTo(customerId2.value());
  }

  @Test
  @DisplayName("Should convert to string correctly")
  void shouldConvertToStringCorrectly() {
    CustomerId customerId = new CustomerId(TEST_UUID);

    assertThat(customerId.toString()).isEqualTo(TEST_UUID.toString());
  }

  @Test
  @DisplayName("Should be equal when same UUID value")
  void shouldBeEqualWhenSameUuidValue() {
    CustomerId customerId1 = new CustomerId(TEST_UUID);
    CustomerId customerId2 = new CustomerId(TEST_UUID);

    assertThat(customerId1).isEqualTo(customerId2);
    assertThat(customerId1.hashCode()).isEqualTo(customerId2.hashCode());
  }

  @Test
  @DisplayName("Should not be equal when different UUID value")
  void shouldNotBeEqualWhenDifferentUuidValue() {
    CustomerId customerId1 = new CustomerId(TEST_UUID);
    CustomerId customerId2 = new CustomerId(UUID.fromString("550e8400-e29b-41d4-a716-446655440001"));

    assertThat(customerId1).isNotEqualTo(customerId2);
  }

  @Test
  @DisplayName("Should not be equal to null")
  void shouldNotBeEqualToNull() {
    CustomerId customerId = new CustomerId(TEST_UUID);

    assertThat(customerId).isNotEqualTo(null);
  }

  @Test
  @DisplayName("Should not be equal to different type")
  void shouldNotBeEqualToDifferentType() {
    CustomerId customerId = new CustomerId(TEST_UUID);

    assertThat(customerId).isNotEqualTo(TEST_UUID.toString());
  }
}
