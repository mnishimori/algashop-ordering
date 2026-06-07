package com.algaworks.algashop.ordering.domain.valueobject.id;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.algaworks.algashop.ordering.domain.model.valueobject.id.ProductId;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductIdTest {

  private static final UUID TEST_UUID = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");

  @Test
  @DisplayName("Should create ProductId with generated UUID")
  void shouldCreateProductIdWithGeneratedUuid() {
    ProductId productId = new ProductId();

    assertThat(productId.value()).isNotNull();
    assertThat(productId.value()).isInstanceOf(UUID.class);
  }

  @Test
  @DisplayName("Should create ProductId with provided UUID")
  void shouldCreateProductIdWithProvidedUuid() {
    ProductId productId = new ProductId(TEST_UUID);

    assertThat(productId.value()).isEqualTo(TEST_UUID);
  }

  @Test
  @DisplayName("Should throw exception when UUID is null")
  void shouldThrowExceptionWhenUuidIsNull() {
    assertThatThrownBy(() -> new ProductId(null))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should generate unique UUIDs for each instance")
  void shouldGenerateUniqueUuidsForEachInstance() {
    ProductId productId1 = new ProductId();
    ProductId productId2 = new ProductId();

    assertThat(productId1.value()).isNotEqualTo(productId2.value());
  }

  @Test
  @DisplayName("Should convert to string correctly")
  void shouldConvertToStringCorrectly() {
    ProductId productId = new ProductId(TEST_UUID);

    assertThat(productId.toString()).isEqualTo(TEST_UUID.toString());
  }

  @Test
  @DisplayName("Should be equal when same UUID value")
  void shouldBeEqualWhenSameUuidValue() {
    ProductId productId1 = new ProductId(TEST_UUID);
    ProductId productId2 = new ProductId(TEST_UUID);

    assertThat(productId1).isEqualTo(productId2);
    assertThat(productId1.hashCode()).isEqualTo(productId2.hashCode());
  }

  @Test
  @DisplayName("Should not be equal when different UUID value")
  void shouldNotBeEqualWhenDifferentUuidValue() {
    ProductId productId1 = new ProductId(TEST_UUID);
    ProductId productId2 = new ProductId(UUID.fromString("550e8400-e29b-41d4-a716-446655440001"));

    assertThat(productId1).isNotEqualTo(productId2);
  }

  @Test
  @DisplayName("Should not be equal to null")
  void shouldNotBeEqualToNull() {
    ProductId productId = new ProductId(TEST_UUID);

    assertThat(productId).isNotEqualTo(null);
  }

  @Test
  @DisplayName("Should not be equal to different type")
  void shouldNotBeEqualToDifferentType() {
    ProductId productId = new ProductId(TEST_UUID);

    assertThat(productId).isNotEqualTo(TEST_UUID.toString());
  }
}
