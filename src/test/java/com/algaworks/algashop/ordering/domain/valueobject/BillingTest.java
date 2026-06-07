package com.algaworks.algashop.ordering.domain.valueobject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.algaworks.algashop.ordering.domain.model.valueobject.Address;
import com.algaworks.algashop.ordering.domain.model.valueobject.Billing;
import com.algaworks.algashop.ordering.domain.model.valueobject.Document;
import com.algaworks.algashop.ordering.domain.model.valueobject.Email;
import com.algaworks.algashop.ordering.domain.model.valueobject.FullName;
import com.algaworks.algashop.ordering.domain.model.valueobject.Phone;
import com.algaworks.algashop.ordering.domain.model.valueobject.ZipCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BillingTest {

  private static final FullName FULL_NAME = new FullName("John", "Doe");
  private static final Document DOCUMENT = new Document("12345678900");
  private static final Phone PHONE = new Phone("11999999999");
  private static final ZipCode ZIP_CODE = new ZipCode("12345-678");
  private static final Address ADDRESS = new Address("Main Street", "123", "Apt 1", "Downtown", "New York", "NY",
      ZIP_CODE);
  private static final Email EMAIL = new Email("john.doe@example.com");

  @Test
  @DisplayName("Should create BillingInfo with all valid components")
  void shouldCreateBillingInfoWithAllValidComponents() {
    var billingInfo = Billing.builder().fullName(FULL_NAME).document(DOCUMENT).phone(PHONE).address(ADDRESS).email(EMAIL).build();

    assertThat(billingInfo.fullName()).isEqualTo(FULL_NAME);
    assertThat(billingInfo.document()).isEqualTo(DOCUMENT);
    assertThat(billingInfo.phone()).isEqualTo(PHONE);
    assertThat(billingInfo.address()).isEqualTo(ADDRESS);
  }

  @Test
  @DisplayName("Should create BillingInfo using builder")
  void shouldCreateBillingInfoUsingBuilder() {
    var billingInfo = Billing.builder()
        .fullName(FULL_NAME)
        .document(DOCUMENT)
        .phone(PHONE)
        .address(ADDRESS)
        .email(EMAIL)
        .build();

    assertThat(billingInfo.fullName()).isEqualTo(FULL_NAME);
    assertThat(billingInfo.document()).isEqualTo(DOCUMENT);
    assertThat(billingInfo.phone()).isEqualTo(PHONE);
    assertThat(billingInfo.address()).isEqualTo(ADDRESS);
  }

  @Test
  @DisplayName("Should throw NullPointerException when fullName is null")
  void shouldThrowNullPointerExceptionWhenFullNameIsNull() {
    assertThatThrownBy(() -> Billing.builder().fullName(null).document(DOCUMENT).phone(PHONE).address(ADDRESS).email(EMAIL).build())
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should throw NullPointerException when document is null")
  void shouldThrowNullPointerExceptionWhenDocumentIsNull() {
    assertThatThrownBy(() -> Billing.builder().fullName(FULL_NAME).document(null).phone(PHONE).address(ADDRESS).email(EMAIL).build())
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should throw NullPointerException when phone is null")
  void shouldThrowNullPointerExceptionWhenPhoneIsNull() {
    assertThatThrownBy(
        () -> Billing.builder().fullName(FULL_NAME).document(DOCUMENT).phone(null).address(ADDRESS).email(EMAIL).build())
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should throw NullPointerException when address is null")
  void shouldThrowNullPointerExceptionWhenAddressIsNull() {
    assertThatThrownBy(
        () -> Billing.builder().fullName(FULL_NAME).document(DOCUMENT).phone(PHONE).address(null).email(EMAIL).build())
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should be equal when same components")
  void shouldBeEqualWhenSameComponents() {
    var billingInfo1 = Billing.builder().fullName(FULL_NAME).document(DOCUMENT).phone(PHONE).address(ADDRESS).email(EMAIL).build();
    var billingInfo2 = Billing.builder().fullName(FULL_NAME).document(DOCUMENT).phone(PHONE).address(ADDRESS).email(EMAIL).build();

    assertThat(billingInfo1).isEqualTo(billingInfo2);
    assertThat(billingInfo1.hashCode()).isEqualTo(billingInfo2.hashCode());
  }

  @Test
  @DisplayName("Should not be equal when different fullName")
  void shouldNotBeEqualWhenDifferentFullName() {
    var billingInfo1 = Billing.builder().fullName(FULL_NAME).document(DOCUMENT).phone(PHONE).address(ADDRESS).email(EMAIL).build();
    var differentName = new FullName("Jane", "Smith");
    var billingInfo2 = Billing.builder().fullName(differentName).document(DOCUMENT).phone(PHONE).address(ADDRESS)
        .email(EMAIL).build();

    assertThat(billingInfo1).isNotEqualTo(billingInfo2);
  }

  @Test
  @DisplayName("Should not be equal when different document")
  void shouldNotBeEqualWhenDifferentDocument() {
    var billingInfo1 = Billing.builder().fullName(FULL_NAME).document(DOCUMENT).phone(PHONE).address(ADDRESS).email(EMAIL).build();
    var differentDocument = new Document("98765432100");
    var billingInfo2 = Billing.builder().fullName(FULL_NAME).document(differentDocument).phone(PHONE).address(ADDRESS)
        .email(EMAIL).build();

    assertThat(billingInfo1).isNotEqualTo(billingInfo2);
  }

  @Test
  @DisplayName("Should not be equal when different phone")
  void shouldNotBeEqualWhenDifferentPhone() {
    var billingInfo1 = Billing.builder().fullName(FULL_NAME).document(DOCUMENT).phone(PHONE).address(ADDRESS).email(EMAIL).build();
    var differentPhone = new Phone("11888888888");
    var billingInfo2 = Billing.builder().fullName(FULL_NAME).document(DOCUMENT).phone(differentPhone).address(ADDRESS)
        .email(EMAIL).build();

    assertThat(billingInfo1).isNotEqualTo(billingInfo2);
  }

  @Test
  @DisplayName("Should not be equal when different address")
  void shouldNotBeEqualWhenDifferentAddress() {
    var billingInfo1 = Billing.builder().fullName(FULL_NAME).document(DOCUMENT).phone(PHONE).address(ADDRESS).email(EMAIL).build();
    var differentZipCode = new ZipCode("54321-876");
    var differentAddress = new Address("Second Street", "456", null, "Uptown", "Los Angeles", "CA", differentZipCode);
    var billingInfo2 = Billing.builder().fullName(FULL_NAME).document(DOCUMENT).phone(PHONE).address(differentAddress)
        .email(EMAIL).build();

    assertThat(billingInfo1).isNotEqualTo(billingInfo2);
  }

  @Test
  @DisplayName("Should not be equal to null")
  void shouldNotBeEqualToNull() {
    var billingInfo = Billing.builder().fullName(FULL_NAME).document(DOCUMENT).phone(PHONE).address(ADDRESS).email(EMAIL).build();

    assertThat(billingInfo).isNotEqualTo(null);
  }

  @Test
  @DisplayName("Should not be equal to different type")
  void shouldNotBeEqualToDifferentType() {
    var billingInfo = Billing.builder().fullName(FULL_NAME).document(DOCUMENT).phone(PHONE).address(ADDRESS).email(EMAIL).build();

    assertThat(billingInfo).isNotEqualTo("not a billing info");
  }
}