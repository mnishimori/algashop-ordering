package com.algaworks.algashop.ordering.domain.valueobject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ShippingInfoTest {

  private static final FullName FULL_NAME = new FullName("John", "Doe");
  private static final Document DOCUMENT = new Document("12345678900");
  private static final Phone PHONE = new Phone("11999999999");
  private static final ZipCode ZIP_CODE = new ZipCode("12345-678");
  private static final Address ADDRESS = new Address("Main Street", "123", "Apt 1", "Downtown", "New York", "NY", ZIP_CODE);

  @Test
  @DisplayName("Should create ShippingInfo with all valid components")
  void shouldCreateShippingInfoWithAllValidComponents() {
    var shippingInfo = new ShippingInfo(FULL_NAME, DOCUMENT, PHONE, ADDRESS);

    assertThat(shippingInfo.fullName()).isEqualTo(FULL_NAME);
    assertThat(shippingInfo.document()).isEqualTo(DOCUMENT);
    assertThat(shippingInfo.phone()).isEqualTo(PHONE);
    assertThat(shippingInfo.address()).isEqualTo(ADDRESS);
  }

  @Test
  @DisplayName("Should create ShippingInfo using builder")
  void shouldCreateShippingInfoUsingBuilder() {
    var shippingInfo = ShippingInfo.builder()
        .fullName(FULL_NAME)
        .document(DOCUMENT)
        .phone(PHONE)
        .address(ADDRESS)
        .build();

    assertThat(shippingInfo.fullName()).isEqualTo(FULL_NAME);
    assertThat(shippingInfo.document()).isEqualTo(DOCUMENT);
    assertThat(shippingInfo.phone()).isEqualTo(PHONE);
    assertThat(shippingInfo.address()).isEqualTo(ADDRESS);
  }

  @Test
  @DisplayName("Should throw NullPointerException when fullName is null")
  void shouldThrowNullPointerExceptionWhenFullNameIsNull() {
    assertThatThrownBy(() -> new ShippingInfo(null, DOCUMENT, PHONE, ADDRESS))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should throw NullPointerException when document is null")
  void shouldThrowNullPointerExceptionWhenDocumentIsNull() {
    assertThatThrownBy(() -> new ShippingInfo(FULL_NAME, null, PHONE, ADDRESS))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should throw NullPointerException when phone is null")
  void shouldThrowNullPointerExceptionWhenPhoneIsNull() {
    assertThatThrownBy(() -> new ShippingInfo(FULL_NAME, DOCUMENT, null, ADDRESS))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should throw NullPointerException when address is null")
  void shouldThrowNullPointerExceptionWhenAddressIsNull() {
    assertThatThrownBy(() -> new ShippingInfo(FULL_NAME, DOCUMENT, PHONE, null))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should be equal when same components")
  void shouldBeEqualWhenSameComponents() {
    var shippingInfo1 = new ShippingInfo(FULL_NAME, DOCUMENT, PHONE, ADDRESS);
    var shippingInfo2 = new ShippingInfo(FULL_NAME, DOCUMENT, PHONE, ADDRESS);

    assertThat(shippingInfo1).isEqualTo(shippingInfo2);
    assertThat(shippingInfo1.hashCode()).isEqualTo(shippingInfo2.hashCode());
  }

  @Test
  @DisplayName("Should not be equal when different fullName")
  void shouldNotBeEqualWhenDifferentFullName() {
    var shippingInfo1 = new ShippingInfo(FULL_NAME, DOCUMENT, PHONE, ADDRESS);
    var differentName = new FullName("Jane", "Smith");
    var shippingInfo2 = new ShippingInfo(differentName, DOCUMENT, PHONE, ADDRESS);

    assertThat(shippingInfo1).isNotEqualTo(shippingInfo2);
  }

  @Test
  @DisplayName("Should not be equal when different document")
  void shouldNotBeEqualWhenDifferentDocument() {
    var shippingInfo1 = new ShippingInfo(FULL_NAME, DOCUMENT, PHONE, ADDRESS);
    var differentDocument = new Document("98765432100");
    var shippingInfo2 = new ShippingInfo(FULL_NAME, differentDocument, PHONE, ADDRESS);

    assertThat(shippingInfo1).isNotEqualTo(shippingInfo2);
  }

  @Test
  @DisplayName("Should not be equal when different phone")
  void shouldNotBeEqualWhenDifferentPhone() {
    var shippingInfo1 = new ShippingInfo(FULL_NAME, DOCUMENT, PHONE, ADDRESS);
    var differentPhone = new Phone("11888888888");
    var shippingInfo2 = new ShippingInfo(FULL_NAME, DOCUMENT, differentPhone, ADDRESS);

    assertThat(shippingInfo1).isNotEqualTo(shippingInfo2);
  }

  @Test
  @DisplayName("Should not be equal when different address")
  void shouldNotBeEqualWhenDifferentAddress() {
    var shippingInfo1 = new ShippingInfo(FULL_NAME, DOCUMENT, PHONE, ADDRESS);
    var differentZipCode = new ZipCode("54321-876");
    var differentAddress = new Address("Second Street", "456", null, "Uptown", "Los Angeles", "CA", differentZipCode);
    var shippingInfo2 = new ShippingInfo(FULL_NAME, DOCUMENT, PHONE, differentAddress);

    assertThat(shippingInfo1).isNotEqualTo(shippingInfo2);
  }

  @Test
  @DisplayName("Should not be equal to null")
  void shouldNotBeEqualToNull() {
    var shippingInfo = new ShippingInfo(FULL_NAME, DOCUMENT, PHONE, ADDRESS);

    assertThat(shippingInfo).isNotEqualTo(null);
  }

  @Test
  @DisplayName("Should not be equal to different type")
  void shouldNotBeEqualToDifferentType() {
    var shippingInfo = new ShippingInfo(FULL_NAME, DOCUMENT, PHONE, ADDRESS);

    assertThat(shippingInfo).isNotEqualTo("not a shipping info");
  }
}
