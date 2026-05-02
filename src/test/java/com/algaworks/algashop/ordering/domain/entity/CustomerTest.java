package com.algaworks.algashop.ordering.domain.entity;

import static com.algaworks.algashop.ordering.domain.entity.CustomerTestDataBuilder.BIRTH_DATE;
import static com.algaworks.algashop.ordering.domain.entity.CustomerTestDataBuilder.FIRST_NAME;
import static com.algaworks.algashop.ordering.domain.entity.CustomerTestDataBuilder.LAST_NAME;
import static com.algaworks.algashop.ordering.domain.messages.ErrorMessages.BIRTHDATE_MUST_IN_PAST;
import static com.algaworks.algashop.ordering.domain.messages.ErrorMessages.FULL_NAME_CANNOT_BE_BLANK;
import static com.algaworks.algashop.ordering.domain.messages.ErrorMessages.LOYALTY_POINTS_CANNOT_BE_NEGATIVE_OR_ZERO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.algaworks.algashop.ordering.domain.exception.CustomerArchivedException;
import com.algaworks.algashop.ordering.domain.valueobject.Address;
import com.algaworks.algashop.ordering.domain.valueobject.FullName;
import com.algaworks.algashop.ordering.domain.valueobject.LoyaltyPoints;
import com.algaworks.algashop.ordering.domain.valueobject.ZipCode;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CustomerTest {

  @Test
  @DisplayName("Should create customer with full constructor")
  void shouldCreateCustomerWithFullConstructor() {
    var customer = CustomerTestDataBuilder.brandNewCustomer().build();

    assertThat(customer.fullName()).isEqualTo(new FullName(FIRST_NAME, LAST_NAME));
    assertThat(customer.birthDate()).isEqualTo(BIRTH_DATE);
    assertThat(customer.email()).isEqualTo(CustomerTestDataBuilder.EMAIL);
    assertThat(customer.phone()).isEqualTo(CustomerTestDataBuilder.PHONE);
    assertThat(customer.document()).isEqualTo(CustomerTestDataBuilder.DOCUMENT);
    assertThat(customer.promotionNotificationsAllowed()).isFalse();
    assertThat(customer.archived()).isFalse();
    assertThat(customer.archivedAt()).isNull();
    assertThat(customer.loyaltyPoints()).isEqualTo(LoyaltyPoints.ZERO);
  }

  @Test
  @DisplayName("Should create customer with simplified constructor")
  void shouldCreateCustomerWithSimplifiedConstructor() {
    var customer = CustomerTestDataBuilder.brandNewCustomer().build();

    assertThat(customer.fullName()).isEqualTo(new FullName(FIRST_NAME, LAST_NAME));
    assertThat(customer.birthDate()).isEqualTo(BIRTH_DATE);
    assertThat(customer.email()).isEqualTo(CustomerTestDataBuilder.EMAIL);
    assertThat(customer.phone()).isEqualTo(CustomerTestDataBuilder.PHONE);
    assertThat(customer.document()).isEqualTo(CustomerTestDataBuilder.DOCUMENT);
    assertThat(customer.promotionNotificationsAllowed()).isFalse();
    assertThat(customer.loyaltyPoints()).isEqualTo(LoyaltyPoints.ZERO);
  }

  @Test
  @DisplayName("Should create customer with null birth date")
  void shouldCreateCustomerWithNullBirthDate() {
    var customer = CustomerTestDataBuilder.brandNewCustomer().birthDate(null).build();
    assertThat(customer.birthDate()).isNull();
  }

  @Test
  @DisplayName("Should throw exception when full name is null")
  void shouldThrowExceptionWhenFullNameIsNull() {
    assertThatThrownBy(
        () -> CustomerTestDataBuilder.brandNewCustomer().fullName(new FullName(null, null)).build()).isInstanceOf(
        NullPointerException.class);
  }

  @Test
  @DisplayName("Should throw exception when full name is blank")
  void shouldThrowExceptionWhenFullNameIsBlank() {
    assertThatThrownBy(
        () -> CustomerTestDataBuilder.brandNewCustomer().fullName(new FullName("", "")).build()).isInstanceOf(
            IllegalArgumentException.class)
        .hasMessage(FULL_NAME_CANNOT_BE_BLANK);
  }

  @Test
  @DisplayName("Should throw exception when email is null")
  void shouldThrowExceptionWhenEmailIsNull() {
    assertThatThrownBy(
        () -> CustomerTestDataBuilder.brandNewCustomer().email(null).build())
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should throw exception when email is blank")
  void shouldThrowExceptionWhenEmailIsBlank() {
    assertThatThrownBy(
        () -> CustomerTestDataBuilder.brandNewCustomer().email(" ").build()).isInstanceOf(
            IllegalArgumentException.class)
        .hasMessage("Email cannot be blank");
  }

  @Test
  @DisplayName("Should throw exception when email is invalid")
  void shouldThrowExceptionWhenEmailIsInvalid() {

    assertThatThrownBy(
        () -> CustomerTestDataBuilder.brandNewCustomer().email("invalid-email").build()).isInstanceOf(
            IllegalArgumentException.class)
        .hasMessage("Email is invalid");
  }

  @Test
  @DisplayName("Should throw exception when phone is null")
  void shouldThrowExceptionWhenPhoneIsNull() {

    assertThatThrownBy(
        () -> CustomerTestDataBuilder.brandNewCustomer().phone(null).build()).isInstanceOf(
        NullPointerException.class);
  }

  @Test
  @DisplayName("Should throw exception when phone is blank")
  void shouldThrowExceptionWhenPhoneIsBlank() {
    assertThatThrownBy(
        () -> CustomerTestDataBuilder.brandNewCustomer().phone(" ").build()).isInstanceOf(
            IllegalArgumentException.class)
        .hasMessage("Phone cannot be blank");
  }

  @Test
  @DisplayName("Should throw exception when document is null")
  void shouldThrowExceptionWhenDocumentIsNull() {

    assertThatThrownBy(
        () -> CustomerTestDataBuilder.brandNewCustomer().document(null).build()).isInstanceOf(
        NullPointerException.class);
  }

  @Test
  @DisplayName("Should throw exception when document is blank")
  void shouldThrowExceptionWhenDocumentIsBlank() {

    assertThatThrownBy(
        () -> CustomerTestDataBuilder.brandNewCustomer().document(" ").build()).isInstanceOf(
            IllegalArgumentException.class)
        .hasMessage("Document cannot be blank");
  }

  @Test
  @DisplayName("Should throw exception when birth date is in the future")
  void shouldThrowExceptionWhenBirthDateIsInTheFuture() {
    LocalDate futureDate = LocalDate.now().plusDays(1);

    assertThatThrownBy(
        () -> CustomerTestDataBuilder.brandNewCustomer().birthDate(futureDate).build())
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(BIRTHDATE_MUST_IN_PAST);
  }

  @Test
  @DisplayName("Should add loyalty points to customer")
  void shouldAddLoyaltyPointsToCustomer() {
    Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();

    customer.addLoyaltyPoints(100);

    assertThat(customer.loyaltyPoints().value()).isEqualTo(100);
  }

  @Test
  @DisplayName("Should accumulate loyalty points when adding multiple times")
  void shouldAccumulateLoyaltyPointsWhenAddingMultipleTimes() {
    Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();

    customer.addLoyaltyPoints(50);
    customer.addLoyaltyPoints(30);
    customer.addLoyaltyPoints(20);

    assertThat(customer.loyaltyPoints().value()).isEqualTo(100);
  }

  @Test
  @DisplayName("Should add loyalty points to existing points")
  void shouldAddLoyaltyPointsToExistingPoints() {

    Customer customer = CustomerTestDataBuilder.existedCustomer().loyaltyPoints(new LoyaltyPoints(50)).build();

    customer.addLoyaltyPoints(25);

    assertThat(customer.loyaltyPoints().value()).isEqualTo(75);
  }

  @Test
  @DisplayName("Should throw exception when adding null loyalty points")
  void shouldThrowExceptionWhenAddingNullLoyaltyPoints() {
    var customer = CustomerTestDataBuilder.existedCustomer().build();

    assertThatThrownBy(() -> customer.addLoyaltyPoints(null)).isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should throw exception when adding zero loyalty points")
  void shouldThrowExceptionWhenAddingZeroLoyaltyPoints() {
    var customer = CustomerTestDataBuilder.existedCustomer().build();

    assertThatThrownBy(() -> customer.addLoyaltyPoints(0))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(LOYALTY_POINTS_CANNOT_BE_NEGATIVE_OR_ZERO);
  }

  @Test
  @DisplayName("Should throw exception when adding negative loyalty points")
  void shouldThrowExceptionWhenAddingNegativeLoyaltyPoints() {
    var customer = CustomerTestDataBuilder.existedCustomer().build();

    assertThatThrownBy(() -> customer.addLoyaltyPoints(-10))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(LOYALTY_POINTS_CANNOT_BE_NEGATIVE_OR_ZERO);
  }

  @Test
  @DisplayName("Should throw exception when adding loyalty points to archived customer")
  void shouldThrowExceptionWhenAddingLoyaltyPointsToArchivedCustomer() {
    var customer = CustomerTestDataBuilder.existedCustomer().build();
    customer.archive();

    assertThatThrownBy(() -> customer.addLoyaltyPoints(100)).isInstanceOf(CustomerArchivedException.class);
  }

  @Test
  @DisplayName("Should archive customer with all required changes")
  void shouldArchiveCustomerWithAllRequiredChanges() {
    var customer = CustomerTestDataBuilder.existedCustomer().build();

    customer.archive();

    assertThat(customer.archived()).isTrue();
    assertThat(customer.archivedAt()).isNotNull();
    assertThat(customer.fullName()).isEqualTo(new FullName("Anounymous", "Anounymous"));
    assertThat(customer.email()).endsWith("@anonymous.com");
    assertThat(customer.promotionNotificationsAllowed()).isFalse();
    assertThat(customer.phone()).isEqualTo("0000-0000");
    assertThat(customer.document()).isEqualTo("00000000000");
    assertThat(customer.birthDate()).isNull();
  }

  @Test
  @DisplayName("Should enable promotion notifications")
  void shouldEnablePromotionNotifications() {
    var customer = CustomerTestDataBuilder.existedCustomer().promotionNotificationsAllowed(false).build();
    assertThat(customer.promotionNotificationsAllowed()).isFalse();

    customer.enablePromotionNotifications();

    assertThat(customer.promotionNotificationsAllowed()).isTrue();
  }

  @Test
  @DisplayName("Should disable promotion notifications")
  void shouldDisablePromotionNotifications() {
    var customer = CustomerTestDataBuilder.existedCustomer().promotionNotificationsAllowed(true).build();
    assertThat(customer.promotionNotificationsAllowed()).isTrue();

    customer.disablePromotionNotifications();

    assertThat(customer.promotionNotificationsAllowed()).isFalse();
  }

  @Test
  @DisplayName("Should change name")
  void shouldChangeName() {
    var customer = CustomerTestDataBuilder.existedCustomer().build();

    customer.changeName(new FullName("MARIA", "SILVA"));

    assertThat(customer.fullName()).isEqualTo(new FullName("MARIA", "SILVA"));
  }

  @Test
  @DisplayName("Should throw exception when changing name to blank")
  void shouldThrowExceptionWhenChangingNameToBlank() {
    var customer = CustomerTestDataBuilder.existedCustomer().build();

    assertThatThrownBy(() -> customer.changeName(new FullName("", "")))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(FULL_NAME_CANNOT_BE_BLANK);
  }

  @Test
  @DisplayName("Should throw exception when changing name to null")
  void shouldThrowExceptionWhenChangingNameToNull() {
    var customer = CustomerTestDataBuilder.existedCustomer().build();

    assertThatThrownBy(() -> customer.changeName(null))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should change email")
  void shouldChangeEmail() {
    var customer = CustomerTestDataBuilder.existedCustomer().email("joao.silva@example.com").build();

    customer.changeEmail("maria.silva@example.com");

    assertThat(customer.email()).isEqualTo("maria.silva@example.com");
  }

  @Test
  @DisplayName("Should throw exception when changing email to invalid")
  void shouldThrowExceptionWhenChangingEmailToInvalid() {
    var customer = CustomerTestDataBuilder.existedCustomer().build();

    assertThatThrownBy(() -> customer.changeEmail("invalid"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Email is invalid");
  }

  @Test
  @DisplayName("Should throw exception when changing email to blank")
  void shouldThrowExceptionWhenChangingEmailToBlank() {
    var customer = CustomerTestDataBuilder.existedCustomer().build();

    assertThatThrownBy(() -> customer.changeEmail("   "))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Email cannot be blank");
  }

  @Test
  @DisplayName("Should throw exception when changing email to null")
  void shouldThrowExceptionWhenChangingEmailToNull() {
    var customer = CustomerTestDataBuilder.existedCustomer().build();

    assertThatThrownBy(() -> customer.changeEmail(null))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should change phone")
  void shouldChangePhone() {
    var customer = CustomerTestDataBuilder.existedCustomer().build();

    customer.changePhone("11888888888");

    assertThat(customer.phone()).isEqualTo("11888888888");
  }

  @Test
  @DisplayName("Should throw exception when changing phone to blank")
  void shouldThrowExceptionWhenChangingPhoneToBlank() {
    var customer = CustomerTestDataBuilder.existedCustomer().build();

    assertThatThrownBy(() -> customer.changePhone("   "))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Phone cannot be blank");
  }

  @Test
  @DisplayName("Should throw exception when changing phone to null")
  void shouldThrowExceptionWhenChangingPhoneToNull() {
    var customer = CustomerTestDataBuilder.existedCustomer().build();

    assertThatThrownBy(() -> customer.changePhone(null))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should throw exception when archiving an already archived customer")
  void shouldThrowExceptionWhenArchivingAlreadyArchivedCustomer() {
    var customer = CustomerTestDataBuilder.existedCustomer().build();
    customer.archive();

    assertThatThrownBy(customer::archive)
        .isInstanceOf(CustomerArchivedException.class);
  }

  @Test
  @DisplayName("Should throw exception when changing name of archived customer")
  void shouldThrowExceptionWhenChangingNameOfArchivedCustomer() {
    var customer = CustomerTestDataBuilder.existedCustomer().build();
    customer.archive();

    assertThatThrownBy(() -> customer.changeName(new FullName("MARIA", "SILVA")))
        .isInstanceOf(CustomerArchivedException.class);
  }

  @Test
  @DisplayName("Should throw exception when changing email of archived customer")
  void shouldThrowExceptionWhenChangingEmailOfArchivedCustomer() {
    var customer = CustomerTestDataBuilder.existedCustomer().build();
    customer.archive();

    assertThatThrownBy(() -> customer.changeEmail("maria.silva@example.com"))
        .isInstanceOf(CustomerArchivedException.class);
  }

  @Test
  @DisplayName("Should throw exception when changing phone of archived customer")
  void shouldThrowExceptionWhenChangingPhoneOfArchivedCustomer() {
    var customer = CustomerTestDataBuilder.existedCustomer().build();
    customer.archive();

    assertThatThrownBy(() -> customer.changePhone("11888888888"))
        .isInstanceOf(CustomerArchivedException.class);
  }

  @Test
  @DisplayName("Should throw exception when enabling promotion notifications of archived customer")
  void shouldThrowExceptionWhenEnablingPromotionNotificationsOfArchivedCustomer() {
    var customer = CustomerTestDataBuilder.existedCustomer().build();
    customer.archive();

    assertThatThrownBy(customer::enablePromotionNotifications)
        .isInstanceOf(CustomerArchivedException.class);
  }

  @Test
  @DisplayName("Should throw exception when disabling promotion notifications of archived customer")
  void shouldThrowExceptionWhenDisablingPromotionNotificationsOfArchivedCustomer() {
    var customer = CustomerTestDataBuilder.existedCustomer().build();
    customer.archive();

    assertThatThrownBy(customer::disablePromotionNotifications)
        .isInstanceOf(CustomerArchivedException.class);
  }

  @Test
  @DisplayName("Should not be equal when different id")
  void shouldNotBeEqualWhenDifferentId() {
    var customer1 = CustomerTestDataBuilder.brandNewCustomer().build();
    var customer2 = CustomerTestDataBuilder.existedCustomer().build();

    assertThat(customer1).isNotEqualTo(customer2);
  }

  @Test
  @DisplayName("Should not be equal when null")
  void shouldNotBeEqualWhenNull() {
    var customer = CustomerTestDataBuilder.existedCustomer().build();

    assertThat(customer).isNotEqualTo(null);
  }

  @Test
  @DisplayName("Should not be equal when different class")
  void shouldNotBeEqualWhenDifferentClass() {
    var customer = CustomerTestDataBuilder.existedCustomer().build();

    assertThat(customer).isNotEqualTo("not a customer");
  }

  @Test
  @DisplayName("Should change address")
  void shouldChangeAddress() {
    var customer = CustomerTestDataBuilder.existedCustomer().build();
    var newAddress = Address.builder()
        .street("New Street")
        .number("5678")
        .neighborhood("South Valley")
        .city("Los Angeles")
        .state("California")
        .zipCode(new ZipCode("98765-432"))
        .build();

    customer.changeAddress(newAddress);

    assertThat(customer.address()).isEqualTo(newAddress);
    assertThat(customer.address().street()).isEqualTo("New Street");
    assertThat(customer.address().number()).isEqualTo("5678");
    assertThat(customer.address().city()).isEqualTo("Los Angeles");
  }

  @Test
  @DisplayName("Should throw exception when changing address to null")
  void shouldThrowExceptionWhenChangingAddressToNull() {
    var customer = CustomerTestDataBuilder.existedCustomer().build();

    assertThatThrownBy(() -> customer.changeAddress(null))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should throw exception when changing address of archived customer")
  void shouldThrowExceptionWhenChangingAddressOfArchivedCustomer() {
    var customer = CustomerTestDataBuilder.existedCustomer().build();
    customer.archive();
    var newAddress = Address.builder()
        .street("New Street")
        .number("5678")
        .neighborhood("South Valley")
        .city("Los Angeles")
        .state("California")
        .zipCode(new ZipCode("98765-432"))
        .build();

    assertThatThrownBy(() -> customer.changeAddress(newAddress))
        .isInstanceOf(CustomerArchivedException.class);
  }

  @Test
  @DisplayName("Should throw exception when address is null in constructor")
  void shouldThrowExceptionWhenAddressIsNullInConstructor() {
    assertThatThrownBy(
        () -> CustomerTestDataBuilder.existedCustomer().address(null).build()).isInstanceOf(
        NullPointerException.class);
  }

  @Test
  @DisplayName("Should return address")
  void shouldReturnAddress() {
    var customer = CustomerTestDataBuilder.existedCustomer().build();

    Address address = customer.address();

    assertThat(address).isNotNull();
    assertThat(address.street()).isEqualTo("Bourbon Street");
    assertThat(address.number()).isEqualTo("1234");
    assertThat(address.neighborhood()).isEqualTo("North Valley");
    assertThat(address.city()).isEqualTo("New York");
    assertThat(address.state()).isEqualTo("New York");
    assertThat(address.zipCode().value()).isEqualTo("12345-678");
  }

  @Test
  @DisplayName("Should return document")
  void shouldReturnDocument() {
    var customer = CustomerTestDataBuilder.existedCustomer().build();

    assertThat(customer.document()).isEqualTo(CustomerTestDataBuilder.DOCUMENT);
  }

  @Test
  @DisplayName("Should return birth date")
  void shouldReturnBirthDate() {
    var customer = CustomerTestDataBuilder.existedCustomer().build();

    assertThat(customer.birthDate()).isEqualTo(BIRTH_DATE);
  }

  @Test
  @DisplayName("Should return archived status")
  void shouldReturnArchivedStatus() {
    var customer = CustomerTestDataBuilder.existedCustomer().build();

    customer.archive();

    assertThat(customer.archived()).isTrue();
  }

  @Test
  @DisplayName("Should return archived at")
  void shouldReturnArchivedAt() {
    Customer customer = CustomerTestDataBuilder.existedCustomer().build();
    assertThat(customer.archivedAt()).isNull();

    customer.archive();

    assertThat(customer.archivedAt()).isNotNull();
  }

  @Test
  @DisplayName("Should anonymize address when archiving customer")
  void shouldAnonymizeAddressWhenArchivingCustomer() {
    Customer customer = CustomerTestDataBuilder.existedCustomer().build();

    customer.archive();

    assertThat(customer.address().number()).isEqualTo("Anounymous");
    assertThat(customer.address().complement()).isEqualTo("Anounymous");
  }

  @Test
  @DisplayName("Should return loyalty points")
  void shouldReturnLoyaltyPoints() {
    Customer customer = CustomerTestDataBuilder.existedCustomer().build();

    customer.addLoyaltyPoints(150);

    assertThat(customer.loyaltyPoints().value()).isEqualTo(150);
  }
}
