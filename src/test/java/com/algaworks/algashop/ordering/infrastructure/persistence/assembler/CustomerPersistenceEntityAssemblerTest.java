package com.algaworks.algashop.ordering.infrastructure.persistence.assembler;

import static org.assertj.core.api.Assertions.assertThat;

import com.algaworks.algashop.ordering.domain.entity.CustomerTestDataBuilder;
import com.algaworks.algashop.ordering.domain.model.entity.Customer;
import com.algaworks.algashop.ordering.domain.model.valueobject.Address;
import com.algaworks.algashop.ordering.domain.model.valueobject.FullName;
import com.algaworks.algashop.ordering.domain.model.valueobject.LoyaltyPoints;
import com.algaworks.algashop.ordering.domain.model.valueobject.ZipCode;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.CustomerPersistenceEntity;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CustomerPersistenceEntityAssemblerTest {

  private final CustomerPersistenceEntityAssembler assembler = new CustomerPersistenceEntityAssembler();

  @Test
  @DisplayName("Should create CustomerPersistenceEntity from Customer using fromDomain")
  void shouldCreateCustomerPersistenceEntityFromCustomerUsingFromDomain() {
    Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();

    CustomerPersistenceEntity entity = assembler.fromDomain(customer);

    assertThat(entity.getId()).isEqualTo(customer.id().value());
    assertThat(entity.getFirstName()).isEqualTo(customer.fullName().firstName());
    assertThat(entity.getLastName()).isEqualTo(customer.fullName().lastName());
    assertThat(entity.getBirthDate()).isEqualTo(customer.birthDate());
    assertThat(entity.getEmail()).isEqualTo(customer.email());
    assertThat(entity.getPhone()).isEqualTo(customer.phone());
    assertThat(entity.getDocument()).isEqualTo(customer.document());
    assertThat(entity.getPromotionNotificationsAllowed()).isEqualTo(customer.promotionNotificationsAllowed());
    assertThat(entity.getArchived()).isEqualTo(customer.archived());
    assertThat(entity.getLoyaltyPoints()).isEqualTo(customer.loyaltyPoints().value());
    assertThat(entity.getAddressEmbeddable()).isNotNull();
    assertThat(entity.getAddressEmbeddable().getStreet()).isEqualTo(customer.address().street());
    assertThat(entity.getAddressEmbeddable().getNumber()).isEqualTo(customer.address().number());
    assertThat(entity.getAddressEmbeddable().getCity()).isEqualTo(customer.address().city());
    assertThat(entity.getAddressEmbeddable().getState()).isEqualTo(customer.address().state());
    assertThat(entity.getAddressEmbeddable().getZipCode()).isEqualTo(customer.address().zipCode().toString());
  }

  @Test
  @DisplayName("Should merge Customer data into existing CustomerPersistenceEntity")
  void shouldMergeCustomerDataIntoExistingCustomerPersistenceEntity() {
    CustomerPersistenceEntity existingEntity = new CustomerPersistenceEntity();
    existingEntity.setId(UUID.randomUUID());

    Customer customer = CustomerTestDataBuilder.existedCustomer()
        .customerId(new CustomerId())
        .build();

    CustomerPersistenceEntity result = assembler.merge(existingEntity, customer);

    assertThat(result).isSameAs(existingEntity);
    assertThat(result.getId()).isEqualTo(customer.id().value());
    assertThat(result.getFirstName()).isEqualTo(customer.fullName().firstName());
    assertThat(result.getLastName()).isEqualTo(customer.fullName().lastName());
    assertThat(result.getEmail()).isEqualTo(customer.email());
    assertThat(result.getLoyaltyPoints()).isEqualTo(customer.loyaltyPoints().value());
  }

  @Test
  @DisplayName("Should return the same entity instance from merge")
  void shouldReturnSameEntityInstanceFromMerge() {
    CustomerPersistenceEntity existingEntity = new CustomerPersistenceEntity();
    Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();

    CustomerPersistenceEntity result = assembler.merge(existingEntity, customer);

    assertThat(result).isSameAs(existingEntity);
  }

  @Test
  @DisplayName("Should handle null birth date")
  void shouldHandleNullBirthDate() {
    Customer customer = Customer.brandnew()
        .fullName(new FullName("John", "Doe"))
        .birthDate(null)
        .email("john.doe@example.com")
        .phone("11999999999")
        .document("12345678900")
        .promotionNotificationsAllowed(true)
        .address(Address.builder()
            .street("Street")
            .number("123")
            .neighborhood("Neighborhood")
            .city("City")
            .state("SP")
            .zipCode(new ZipCode("12345-678"))
            .build())
        .build();

    CustomerPersistenceEntity entity = assembler.fromDomain(customer);

    assertThat(entity.getBirthDate()).isNull();
  }

  @Test
  @DisplayName("Should reuse existing address embeddable when present")
  void shouldReuseExistingAddressEmbeddableWhenPresent() {
    CustomerPersistenceEntity existingEntity = new CustomerPersistenceEntity();
    var existingAddressEmbeddable = existingEntity.getAddressEmbeddable();
    Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();

    CustomerPersistenceEntity result = assembler.merge(existingEntity, customer);

    assertThat(result.getAddressEmbeddable()).isNotNull();
    assertThat(result.getAddressEmbeddable().getStreet()).isEqualTo(customer.address().street());
  }

  @Test
  @DisplayName("Should convert loyalty points value correctly")
  void shouldConvertLoyaltyPointsValueCorrectly() {
    Customer customer = CustomerTestDataBuilder.existedCustomer()
        .customerId(new CustomerId())
        .loyaltyPoints(new LoyaltyPoints(150))
        .build();

    CustomerPersistenceEntity entity = assembler.fromDomain(customer);

    assertThat(entity.getLoyaltyPoints()).isEqualTo(150);
  }

  @Test
  @DisplayName("Should set archivedAt when customer is archived")
  void shouldSetArchivedAtWhenCustomerIsArchived() {
    Customer customer = CustomerTestDataBuilder.existedCustomer()
        .customerId(new CustomerId())
        .build();
    customer.archive();

    CustomerPersistenceEntity entity = assembler.fromDomain(customer);

    assertThat(entity.getArchived()).isTrue();
    assertThat(entity.getArchivedAt()).isNotNull();
  }

  @Test
  @DisplayName("Should reflect birth date update from LocalDate value")
  void shouldReflectBirthDateUpdateFromLocalDateValue() {
    LocalDate birthDate = LocalDate.of(2000, 1, 1);
    Customer customer = Customer.brandnew()
        .fullName(new FullName("John", "Doe"))
        .birthDate(birthDate)
        .email("john.doe@example.com")
        .phone("11999999999")
        .document("12345678900")
        .promotionNotificationsAllowed(true)
        .address(Address.builder()
            .street("Street")
            .number("123")
            .neighborhood("Neighborhood")
            .city("City")
            .state("SP")
            .zipCode(new ZipCode("12345-678"))
            .build())
        .build();

    CustomerPersistenceEntity entity = assembler.fromDomain(customer);

    assertThat(entity.getBirthDate()).isEqualTo(birthDate);
  }
}
