package com.algaworks.algashop.ordering.domain.entity;

import static com.algaworks.algashop.ordering.domain.messages.ErrorMessages.BIRTHDATE_MUST_IN_PAST;
import static com.algaworks.algashop.ordering.domain.messages.ErrorMessages.FULL_NAME_CANNOT_BE_BLANK;
import static com.algaworks.algashop.ordering.domain.messages.ErrorMessages.LOYALTY_POINTS_CANNOT_BE_NEGATIVE_OR_ZERO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.algaworks.algashop.ordering.domain.exception.CustomerArchivedException;

import com.algaworks.algashop.ordering.domain.valueobject.CustomerId;
import com.algaworks.algashop.ordering.domain.valueobject.FullName;
import com.algaworks.algashop.ordering.domain.valueobject.LoyaltyPoints;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CustomerTest {

  private static final UUID CUSTOMER_ID = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
  private static final String FIRST_NAME = "João";
  private static final String LAST_NAME = "Silva";
  private static final LocalDate BIRTH_DATE = LocalDate.of(1990, 5, 15);
  private static final String EMAIL = "joao.silva@example.com";
  private static final String PHONE = "11999999999";
  private static final String DOCUMENT = "12345678901";
  private static final OffsetDateTime REGISTERED_AT = OffsetDateTime.parse("2024-01-15T10:30:00+03:00");

  private Customer createCustomer() {
    return createCustomerWithId(new CustomerId());
  }

  private Customer createCustomerWithId(CustomerId customerId) {
    return new Customer(
        customerId,
        new FullName(FIRST_NAME, LAST_NAME),
        BIRTH_DATE,
        EMAIL,
        PHONE,
        DOCUMENT,
        true,
        false,
        REGISTERED_AT,
        null,
        LoyaltyPoints.ZERO
    );
  }

  @Test
  @DisplayName("Should create customer with full constructor")
  void shouldCreateCustomerWithFullConstructor() {
    var customerId = new CustomerId(CUSTOMER_ID);
    var customer = createCustomerWithId(customerId);

    assertThat(customer.id()).isEqualTo(customerId);
    assertThat(customer.fullName()).isEqualTo(new FullName(FIRST_NAME, LAST_NAME));
    assertThat(customer.birthDate()).isEqualTo(BIRTH_DATE);
    assertThat(customer.email()).isEqualTo(EMAIL);
    assertThat(customer.phone()).isEqualTo(PHONE);
    assertThat(customer.document()).isEqualTo(DOCUMENT);
    assertThat(customer.promotionNotificationsAllowed()).isTrue();
    assertThat(customer.archived()).isFalse();
    assertThat(customer.registeredAt()).isEqualTo(REGISTERED_AT);
    assertThat(customer.archivedAt()).isNull();
    assertThat(customer.loyaltyPoints()).isEqualTo(LoyaltyPoints.ZERO);
  }

  @Test
  @DisplayName("Should create customer with simplified constructor")
  void shouldCreateCustomerWithSimplifiedConstructor() {
    var customerId = new CustomerId(CUSTOMER_ID);
    var customer = new Customer(
        customerId,
        new FullName(FIRST_NAME, LAST_NAME),
        BIRTH_DATE,
        EMAIL,
        PHONE,
        DOCUMENT,
        true,
        REGISTERED_AT
    );

    assertThat(customer.id()).isEqualTo(customerId);
    assertThat(customer.fullName()).isEqualTo(new FullName(FIRST_NAME, LAST_NAME));
    assertThat(customer.birthDate()).isEqualTo(BIRTH_DATE);
    assertThat(customer.email()).isEqualTo(EMAIL);
    assertThat(customer.phone()).isEqualTo(PHONE);
    assertThat(customer.document()).isEqualTo(DOCUMENT);
    assertThat(customer.promotionNotificationsAllowed()).isTrue();
    assertThat(customer.registeredAt()).isEqualTo(REGISTERED_AT);
    assertThat(customer.loyaltyPoints()).isEqualTo(LoyaltyPoints.ZERO);
  }

  @Test
  @DisplayName("Should create customer with null birth date")
  void shouldCreateCustomerWithNullBirthDate() {
    var customer = new Customer(
        new CustomerId(),
        new FullName(FIRST_NAME, LAST_NAME),
        null,
        EMAIL,
        PHONE,
        DOCUMENT,
        true,
        REGISTERED_AT
    );

    assertThat(customer.birthDate()).isNull();
  }

  @Test
  @DisplayName("Should throw exception when full name is null")
  void shouldThrowExceptionWhenFullNameIsNull() {
    assertThatThrownBy(() -> new Customer(
        new CustomerId(),
        (FullName) null,
        BIRTH_DATE,
        EMAIL,
        PHONE,
        DOCUMENT,
        true,
        REGISTERED_AT
    )).isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should throw exception when full name is blank")
  void shouldThrowExceptionWhenFullNameIsBlank() {
    assertThatThrownBy(() -> new Customer(
        new CustomerId(),
        new FullName(" ", " "),
        BIRTH_DATE,
        EMAIL,
        PHONE,
        DOCUMENT,
        true,
        REGISTERED_AT
    )).isInstanceOf(IllegalArgumentException.class)
        .hasMessage(FULL_NAME_CANNOT_BE_BLANK);
  }

  @Test
  @DisplayName("Should throw exception when email is null")
  void shouldThrowExceptionWhenEmailIsNull() {
    assertThatThrownBy(() -> new Customer(
        new CustomerId(),
        new FullName("JOAO", "SILVA"),
        BIRTH_DATE,
        null,
        PHONE,
        DOCUMENT,
        true,
        REGISTERED_AT
    )).isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should throw exception when email is blank")
  void shouldThrowExceptionWhenEmailIsBlank() {
    assertThatThrownBy(() -> new Customer(
        new CustomerId(),
        new FullName("JOAO", "SILVA"),
        BIRTH_DATE,
        "   ",
        PHONE,
        DOCUMENT,
        true,
        REGISTERED_AT
    )).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Email cannot be blank");
  }

  @Test
  @DisplayName("Should throw exception when email is invalid")
  void shouldThrowExceptionWhenEmailIsInvalid() {
    assertThatThrownBy(() -> new Customer(
        new CustomerId(),
        new FullName("JOAO", "SILVA"),
        BIRTH_DATE,
        "invalid-email",
        PHONE,
        DOCUMENT,
        true,
        REGISTERED_AT
    )).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Email is invalid");
  }

  @Test
  @DisplayName("Should throw exception when phone is null")
  void shouldThrowExceptionWhenPhoneIsNull() {
    assertThatThrownBy(() -> new Customer(
        new CustomerId(),
        new FullName("JOAO", "SILVA"),
        BIRTH_DATE,
        EMAIL,
        null,
        DOCUMENT,
        true,
        REGISTERED_AT
    )).isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should throw exception when phone is blank")
  void shouldThrowExceptionWhenPhoneIsBlank() {
    assertThatThrownBy(() -> new Customer(
        new CustomerId(),
        new FullName("JOAO", "SILVA"),
        BIRTH_DATE,
        EMAIL,
        "   ",
        DOCUMENT,
        true,
        REGISTERED_AT
    )).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Phone cannot be blank");
  }

  @Test
  @DisplayName("Should throw exception when document is null")
  void shouldThrowExceptionWhenDocumentIsNull() {
    assertThatThrownBy(() -> new Customer(
        new CustomerId(),
        new FullName("JOAO", "SILVA"),
        BIRTH_DATE,
        EMAIL,
        PHONE,
        null,
        true,
        REGISTERED_AT
    )).isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should throw exception when document is blank")
  void shouldThrowExceptionWhenDocumentIsBlank() {
    assertThatThrownBy(() -> new Customer(
        new CustomerId(),
        new FullName("JOAO", "SILVA"),
        BIRTH_DATE,
        EMAIL,
        PHONE,
        "   ",
        true,
        REGISTERED_AT
    )).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Document cannot be blank");
  }

  @Test
  @DisplayName("Should throw exception when birth date is in the future")
  void shouldThrowExceptionWhenBirthDateIsInTheFuture() {
    LocalDate futureDate = LocalDate.now().plusDays(1);

    assertThatThrownBy(() -> new Customer(
        new CustomerId(),
        new FullName("JOAO", "SILVA"),
        futureDate,
        EMAIL,
        PHONE,
        DOCUMENT,
        true,
        REGISTERED_AT
    )).isInstanceOf(IllegalArgumentException.class)
        .hasMessage(BIRTHDATE_MUST_IN_PAST);
  }

  @Test
  @DisplayName("Should add loyalty points to customer")
  void shouldAddLoyaltyPointsToCustomer() {
    Customer customer = createCustomer();

    customer.addLoyaltyPoints(100);

    assertThat(customer.loyaltyPoints().value()).isEqualTo(100);
  }

  @Test
  @DisplayName("Should accumulate loyalty points when adding multiple times")
  void shouldAccumulateLoyaltyPointsWhenAddingMultipleTimes() {
    Customer customer = createCustomer();

    customer.addLoyaltyPoints(50);
    customer.addLoyaltyPoints(30);
    customer.addLoyaltyPoints(20);

    assertThat(customer.loyaltyPoints().value()).isEqualTo(100);
  }

  @Test
  @DisplayName("Should add loyalty points to existing points")
  void shouldAddLoyaltyPointsToExistingPoints() {
    Customer customer = new Customer(
        new CustomerId(),
        new FullName(FIRST_NAME, LAST_NAME),
        BIRTH_DATE,
        EMAIL,
        PHONE,
        DOCUMENT,
        true,
        false,
        REGISTERED_AT,
        null,
        new LoyaltyPoints(50)
    );

    customer.addLoyaltyPoints(25);

    assertThat(customer.loyaltyPoints().value()).isEqualTo(75);
  }

  @Test
  @DisplayName("Should throw exception when adding null loyalty points")
  void shouldThrowExceptionWhenAddingNullLoyaltyPoints() {
    Customer customer = createCustomer();

    assertThatThrownBy(() -> customer.addLoyaltyPoints(null))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should throw exception when adding zero loyalty points")
  void shouldThrowExceptionWhenAddingZeroLoyaltyPoints() {
    Customer customer = createCustomer();

    assertThatThrownBy(() -> customer.addLoyaltyPoints(0))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(LOYALTY_POINTS_CANNOT_BE_NEGATIVE_OR_ZERO);
  }

  @Test
  @DisplayName("Should throw exception when adding negative loyalty points")
  void shouldThrowExceptionWhenAddingNegativeLoyaltyPoints() {
    Customer customer = createCustomer();

    assertThatThrownBy(() -> customer.addLoyaltyPoints(-10))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(LOYALTY_POINTS_CANNOT_BE_NEGATIVE_OR_ZERO);
  }

  @Test
  @DisplayName("Should throw exception when adding loyalty points to archived customer")
  void shouldThrowExceptionWhenAddingLoyaltyPointsToArchivedCustomer() {
    Customer customer = createCustomer();
    customer.archive();

    assertThatThrownBy(() -> customer.addLoyaltyPoints(100))
        .isInstanceOf(CustomerArchivedException.class);
  }

  @Test
  @DisplayName("Should archive customer with all required changes")
  void shouldArchiveCustomerWithAllRequiredChanges() {
    Customer customer = new Customer(
        new CustomerId(),
        new FullName(FIRST_NAME, LAST_NAME),
        BIRTH_DATE,
        EMAIL,
        PHONE,
        DOCUMENT,
        true,
        false,
        REGISTERED_AT,
        null,
        new LoyaltyPoints(100)
    );

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
    Customer customer = new Customer(
        new CustomerId(),
        new FullName("JOAO", "SILVA"),
        BIRTH_DATE,
        EMAIL,
        PHONE,
        DOCUMENT,
        false,
        REGISTERED_AT
    );
    assertThat(customer.promotionNotificationsAllowed()).isFalse();

    customer.enablePromotionNotifications();

    assertThat(customer.promotionNotificationsAllowed()).isTrue();
  }

  @Test
  @DisplayName("Should disable promotion notifications")
  void shouldDisablePromotionNotifications() {
    Customer customer = createCustomer();
    assertThat(customer.promotionNotificationsAllowed()).isTrue();

    customer.disablePromotionNotifications();

    assertThat(customer.promotionNotificationsAllowed()).isFalse();
  }

  @Test
  @DisplayName("Should change name")
  void shouldChangeName() {
    Customer customer = createCustomer();

    customer.changeName(new FullName("MARIA", "SILVA"));

    assertThat(customer.fullName()).isEqualTo(new FullName("MARIA", "SILVA"));
  }

  @Test
  @DisplayName("Should throw exception when changing name to blank")
  void shouldThrowExceptionWhenChangingNameToBlank() {
    Customer customer = createCustomer();

    assertThatThrownBy(() -> customer.changeName(new FullName("", "")))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(FULL_NAME_CANNOT_BE_BLANK);
  }

  @Test
  @DisplayName("Should throw exception when changing name to null")
  void shouldThrowExceptionWhenChangingNameToNull() {
    Customer customer = createCustomer();

    assertThatThrownBy(() -> customer.changeName(null))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should change email")
  void shouldChangeEmail() {
    Customer customer = createCustomer();

    customer.changeEmail("maria.silva@example.com");

    assertThat(customer.email()).isEqualTo("maria.silva@example.com");
  }

  @Test
  @DisplayName("Should throw exception when changing email to invalid")
  void shouldThrowExceptionWhenChangingEmailToInvalid() {
    Customer customer = createCustomer();

    assertThatThrownBy(() -> customer.changeEmail("invalid"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Email is invalid");
  }

  @Test
  @DisplayName("Should throw exception when changing email to blank")
  void shouldThrowExceptionWhenChangingEmailToBlank() {
    Customer customer = createCustomer();

    assertThatThrownBy(() -> customer.changeEmail("   "))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Email cannot be blank");
  }

  @Test
  @DisplayName("Should throw exception when changing email to null")
  void shouldThrowExceptionWhenChangingEmailToNull() {
    Customer customer = createCustomer();

    assertThatThrownBy(() -> customer.changeEmail(null))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should change phone")
  void shouldChangePhone() {
    Customer customer = createCustomer();

    customer.changePhone("11888888888");

    assertThat(customer.phone()).isEqualTo("11888888888");
  }

  @Test
  @DisplayName("Should throw exception when changing phone to blank")
  void shouldThrowExceptionWhenChangingPhoneToBlank() {
    Customer customer = createCustomer();

    assertThatThrownBy(() -> customer.changePhone("   "))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Phone cannot be blank");
  }

  @Test
  @DisplayName("Should throw exception when changing phone to null")
  void shouldThrowExceptionWhenChangingPhoneToNull() {
    Customer customer = createCustomer();

    assertThatThrownBy(() -> customer.changePhone(null))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should throw exception when archiving an already archived customer")
  void shouldThrowExceptionWhenArchivingAlreadyArchivedCustomer() {
    Customer customer = createCustomer();
    customer.archive();

    assertThatThrownBy(customer::archive)
        .isInstanceOf(CustomerArchivedException.class);
  }

  @Test
  @DisplayName("Should throw exception when changing name of archived customer")
  void shouldThrowExceptionWhenChangingNameOfArchivedCustomer() {
    Customer customer = createCustomer();
    customer.archive();

    assertThatThrownBy(() -> customer.changeName(new FullName("MARIA", "SILVA")))
        .isInstanceOf(CustomerArchivedException.class);
  }

  @Test
  @DisplayName("Should throw exception when changing email of archived customer")
  void shouldThrowExceptionWhenChangingEmailOfArchivedCustomer() {
    Customer customer = createCustomer();
    customer.archive();

    assertThatThrownBy(() -> customer.changeEmail("maria.silva@example.com"))
        .isInstanceOf(CustomerArchivedException.class);
  }

  @Test
  @DisplayName("Should throw exception when changing phone of archived customer")
  void shouldThrowExceptionWhenChangingPhoneOfArchivedCustomer() {
    Customer customer = createCustomer();
    customer.archive();

    assertThatThrownBy(() -> customer.changePhone("11888888888"))
        .isInstanceOf(CustomerArchivedException.class);
  }

  @Test
  @DisplayName("Should throw exception when enabling promotion notifications of archived customer")
  void shouldThrowExceptionWhenEnablingPromotionNotificationsOfArchivedCustomer() {
    Customer customer = createCustomer();
    customer.archive();

    assertThatThrownBy(customer::enablePromotionNotifications)
        .isInstanceOf(CustomerArchivedException.class);
  }

  @Test
  @DisplayName("Should throw exception when disabling promotion notifications of archived customer")
  void shouldThrowExceptionWhenDisablingPromotionNotificationsOfArchivedCustomer() {
    Customer customer = createCustomer();
    customer.archive();

    assertThatThrownBy(customer::disablePromotionNotifications)
        .isInstanceOf(CustomerArchivedException.class);
  }

  @Test
  @DisplayName("Should be equal when same id")
  void shouldBeEqualWhenSameId() {
    var sharedId = new CustomerId(CUSTOMER_ID);
    Customer customer1 = createCustomerWithId(sharedId);
    Customer customer2 = createCustomerWithId(sharedId);

    assertThat(customer1).isEqualTo(customer2);
    assertThat(customer1.hashCode()).isEqualTo(customer2.hashCode());
  }

  @Test
  @DisplayName("Should not be equal when different id")
  void shouldNotBeEqualWhenDifferentId() {
    Customer customer1 = createCustomer();
    Customer customer2 = new Customer(
        new CustomerId(UUID.fromString("550e8400-e29b-41d4-a716-446655440001")),
        new FullName("JOAO", "SILVA"),
        BIRTH_DATE,
        EMAIL,
        PHONE,
        DOCUMENT,
        true,
        REGISTERED_AT
    );

    assertThat(customer1).isNotEqualTo(customer2);
  }

  @Test
  @DisplayName("Should not be equal when null")
  void shouldNotBeEqualWhenNull() {
    Customer customer = createCustomer();

    assertThat(customer).isNotEqualTo(null);
  }

  @Test
  @DisplayName("Should not be equal when different class")
  void shouldNotBeEqualWhenDifferentClass() {
    Customer customer = createCustomer();

    assertThat(customer).isNotEqualTo("not a customer");
  }
}
