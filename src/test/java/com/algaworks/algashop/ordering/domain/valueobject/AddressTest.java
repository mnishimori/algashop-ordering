package com.algaworks.algashop.ordering.domain.valueobject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AddressTest {

  private static final String STREET = "Bourbon Street";
  private static final String NUMBER = "1234";
  private static final String COMPLEMENT = "Apt 101";
  private static final String NEIGHBORHOOD = "North Valley";
  private static final String CITY = "New York";
  private static final String STATE = "New York";
  private static final ZipCode ZIP_CODE = new ZipCode("12345-678");

  @Test
  @DisplayName("Should create address with all fields")
  void shouldCreateAddressWithAllFields() {
    var address = new Address(STREET, NUMBER, COMPLEMENT, NEIGHBORHOOD, CITY, STATE, ZIP_CODE);

    assertThat(address.street()).isEqualTo(STREET);
    assertThat(address.number()).isEqualTo(NUMBER);
    assertThat(address.complement()).isEqualTo(COMPLEMENT);
    assertThat(address.neighborhood()).isEqualTo(NEIGHBORHOOD);
    assertThat(address.city()).isEqualTo(CITY);
    assertThat(address.state()).isEqualTo(STATE);
    assertThat(address.zipCode()).isEqualTo(ZIP_CODE);
  }

  @Test
  @DisplayName("Should create address with null complement")
  void shouldCreateAddressWithNullComplement() {
    var address = new Address(STREET, NUMBER, null, NEIGHBORHOOD, CITY, STATE, ZIP_CODE);

    assertThat(address.street()).isEqualTo(STREET);
    assertThat(address.number()).isEqualTo(NUMBER);
    assertThat(address.complement()).isNull();
    assertThat(address.neighborhood()).isEqualTo(NEIGHBORHOOD);
    assertThat(address.city()).isEqualTo(CITY);
    assertThat(address.state()).isEqualTo(STATE);
    assertThat(address.zipCode()).isEqualTo(ZIP_CODE);
  }

  @Test
  @DisplayName("Should create address using builder with all fields")
  void shouldCreateAddressUsingBuilderWithAllFields() {
    var address = Address.builder()
        .street(STREET)
        .number(NUMBER)
        .complement(COMPLEMENT)
        .neighborhood(NEIGHBORHOOD)
        .city(CITY)
        .state(STATE)
        .zipCode(ZIP_CODE)
        .build();

    assertThat(address.street()).isEqualTo(STREET);
    assertThat(address.number()).isEqualTo(NUMBER);
    assertThat(address.complement()).isEqualTo(COMPLEMENT);
    assertThat(address.neighborhood()).isEqualTo(NEIGHBORHOOD);
    assertThat(address.city()).isEqualTo(CITY);
    assertThat(address.state()).isEqualTo(STATE);
    assertThat(address.zipCode()).isEqualTo(ZIP_CODE);
  }

  @Test
  @DisplayName("Should create address using builder without complement")
  void shouldCreateAddressUsingBuilderWithoutComplement() {
    var address = Address.builder()
        .street(STREET)
        .number(NUMBER)
        .neighborhood(NEIGHBORHOOD)
        .city(CITY)
        .state(STATE)
        .zipCode(ZIP_CODE)
        .build();

    assertThat(address.street()).isEqualTo(STREET);
    assertThat(address.number()).isEqualTo(NUMBER);
    assertThat(address.complement()).isNull();
    assertThat(address.neighborhood()).isEqualTo(NEIGHBORHOOD);
    assertThat(address.city()).isEqualTo(CITY);
    assertThat(address.state()).isEqualTo(STATE);
    assertThat(address.zipCode()).isEqualTo(ZIP_CODE);
  }

  @Test
  @DisplayName("Should throw exception when street is null")
  void shouldThrowExceptionWhenStreetIsNull() {
    assertThatThrownBy(() -> new Address(null, NUMBER, COMPLEMENT, NEIGHBORHOOD, CITY, STATE, ZIP_CODE))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should throw exception when street is blank")
  void shouldThrowExceptionWhenStreetIsBlank() {
    assertThatThrownBy(() -> new Address("   ", NUMBER, COMPLEMENT, NEIGHBORHOOD, CITY, STATE, ZIP_CODE))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Attribute street cannot be null");
  }

  @Test
  @DisplayName("Should throw exception when number is null")
  void shouldThrowExceptionWhenNumberIsNull() {
    assertThatThrownBy(() -> new Address(STREET, null, COMPLEMENT, NEIGHBORHOOD, CITY, STATE, ZIP_CODE))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should throw exception when number is blank")
  void shouldThrowExceptionWhenNumberIsBlank() {
    assertThatThrownBy(() -> new Address(STREET, "   ", COMPLEMENT, NEIGHBORHOOD, CITY, STATE, ZIP_CODE))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Attribute number cannot be null");
  }

  @Test
  @DisplayName("Should throw exception when neighborhood is null")
  void shouldThrowExceptionWhenNeighborhoodIsNull() {
    assertThatThrownBy(() -> new Address(STREET, NUMBER, COMPLEMENT, null, CITY, STATE, ZIP_CODE))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should throw exception when neighborhood is blank")
  void shouldThrowExceptionWhenNeighborhoodIsBlank() {
    assertThatThrownBy(() -> new Address(STREET, NUMBER, COMPLEMENT, "   ", CITY, STATE, ZIP_CODE))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Attribute neighborhood cannot be null");
  }

  @Test
  @DisplayName("Should throw exception when city is null")
  void shouldThrowExceptionWhenCityIsNull() {
    assertThatThrownBy(() -> new Address(STREET, NUMBER, COMPLEMENT, NEIGHBORHOOD, null, STATE, ZIP_CODE))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should throw exception when city is blank")
  void shouldThrowExceptionWhenCityIsBlank() {
    assertThatThrownBy(() -> new Address(STREET, NUMBER, COMPLEMENT, NEIGHBORHOOD, "   ", STATE, ZIP_CODE))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Attribute city cannot be null");
  }

  @Test
  @DisplayName("Should throw exception when state is null")
  void shouldThrowExceptionWhenStateIsNull() {
    assertThatThrownBy(() -> new Address(STREET, NUMBER, COMPLEMENT, NEIGHBORHOOD, CITY, null, ZIP_CODE))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should throw exception when state is blank")
  void shouldThrowExceptionWhenStateIsBlank() {
    assertThatThrownBy(() -> new Address(STREET, NUMBER, COMPLEMENT, NEIGHBORHOOD, CITY, "   ", ZIP_CODE))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Attribute state cannot be null");
  }

  @Test
  @DisplayName("Should throw exception when zip code is null")
  void shouldThrowExceptionWhenZipCodeIsNull() {
    assertThatThrownBy(() -> new Address(STREET, NUMBER, COMPLEMENT, NEIGHBORHOOD, CITY, STATE, null))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should create copy using toBuilder")
  void shouldCreateCopyUsingToBuilder() {
    var originalAddress = new Address(STREET, NUMBER, COMPLEMENT, NEIGHBORHOOD, CITY, STATE, ZIP_CODE);

    var copiedAddress = originalAddress.toBuilder().build();

    assertThat(copiedAddress).isEqualTo(originalAddress);
    assertThat(copiedAddress.street()).isEqualTo(STREET);
    assertThat(copiedAddress.number()).isEqualTo(NUMBER);
    assertThat(copiedAddress.complement()).isEqualTo(COMPLEMENT);
    assertThat(copiedAddress.neighborhood()).isEqualTo(NEIGHBORHOOD);
    assertThat(copiedAddress.city()).isEqualTo(CITY);
    assertThat(copiedAddress.state()).isEqualTo(STATE);
    assertThat(copiedAddress.zipCode()).isEqualTo(ZIP_CODE);
  }

  @Test
  @DisplayName("Should modify address using toBuilder")
  void shouldModifyAddressUsingToBuilder() {
    var originalAddress = new Address(STREET, NUMBER, COMPLEMENT, NEIGHBORHOOD, CITY, STATE, ZIP_CODE);

    var modifiedAddress = originalAddress.toBuilder()
        .street("New Street")
        .number("5678")
        .build();

    assertThat(modifiedAddress.street()).isEqualTo("New Street");
    assertThat(modifiedAddress.number()).isEqualTo("5678");
    assertThat(modifiedAddress.complement()).isEqualTo(COMPLEMENT);
    assertThat(modifiedAddress.neighborhood()).isEqualTo(NEIGHBORHOOD);
    assertThat(modifiedAddress.city()).isEqualTo(CITY);
    assertThat(modifiedAddress.state()).isEqualTo(STATE);
    assertThat(modifiedAddress.zipCode()).isEqualTo(ZIP_CODE);
  }

  @Test
  @DisplayName("Should be equal when same values")
  void shouldBeEqualWhenSameValues() {
    var address1 = new Address(STREET, NUMBER, COMPLEMENT, NEIGHBORHOOD, CITY, STATE, ZIP_CODE);
    var address2 = new Address(STREET, NUMBER, COMPLEMENT, NEIGHBORHOOD, CITY, STATE, ZIP_CODE);

    assertThat(address1).isEqualTo(address2);
    assertThat(address1.hashCode()).isEqualTo(address2.hashCode());
  }

  @Test
  @DisplayName("Should not be equal when different values")
  void shouldNotBeEqualWhenDifferentValues() {
    var address1 = new Address(STREET, NUMBER, COMPLEMENT, NEIGHBORHOOD, CITY, STATE, ZIP_CODE);
    var address2 = new Address("Different Street", NUMBER, COMPLEMENT, NEIGHBORHOOD, CITY, STATE, ZIP_CODE);

    assertThat(address1).isNotEqualTo(address2);
  }

  @Test
  @DisplayName("Should not be equal when null")
  void shouldNotBeEqualWhenNull() {
    var address = new Address(STREET, NUMBER, COMPLEMENT, NEIGHBORHOOD, CITY, STATE, ZIP_CODE);

    assertThat(address).isNotEqualTo(null);
  }

  @Test
  @DisplayName("Should not be equal when different class")
  void shouldNotBeEqualWhenDifferentClass() {
    var address = new Address(STREET, NUMBER, COMPLEMENT, NEIGHBORHOOD, CITY, STATE, ZIP_CODE);

    assertThat(address).isNotEqualTo("not an address");
  }
}
