package com.algaworks.algashop.ordering.application.service;

import static com.algaworks.algashop.ordering.domain.entity.CustomerTestDataBuilder.BIRTH_DATE;
import static com.algaworks.algashop.ordering.domain.entity.CustomerTestDataBuilder.BOURBON_STREET;
import static com.algaworks.algashop.ordering.domain.entity.CustomerTestDataBuilder.DOCUMENT;
import static com.algaworks.algashop.ordering.domain.entity.CustomerTestDataBuilder.EMAIL;
import static com.algaworks.algashop.ordering.domain.entity.CustomerTestDataBuilder.FIRST_NAME;
import static com.algaworks.algashop.ordering.domain.entity.CustomerTestDataBuilder.LAST_NAME;
import static com.algaworks.algashop.ordering.domain.entity.CustomerTestDataBuilder.NEW_YORK;
import static com.algaworks.algashop.ordering.domain.entity.CustomerTestDataBuilder.NORTH_VALLEY;
import static com.algaworks.algashop.ordering.domain.entity.CustomerTestDataBuilder.NUMBER;
import static com.algaworks.algashop.ordering.domain.entity.CustomerTestDataBuilder.PHONE;
import static com.algaworks.algashop.ordering.domain.entity.CustomerTestDataBuilder.ZIP_CODE;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.algaworks.algashop.ordering.IntegrationTest;
import com.algaworks.algashop.ordering.application.customer.management.CustomerManagementApplicationService;
import com.algaworks.algashop.ordering.application.commons.AddressData;
import com.algaworks.algashop.ordering.application.customer.management.CustomerInput;
import com.algaworks.algashop.ordering.application.customer.management.CustomerUpdateInput;
import com.algaworks.algashop.ordering.domain.entity.CustomerTestDataBuilder;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerNotFoundException;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerEmailIsInUseException;
import com.algaworks.algashop.ordering.domain.model.customer.Customers;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
class CustomerManagementApplicationServiceIntegrationTest {

  @Autowired
  private CustomerManagementApplicationService customerManagementApplicationService;

  @Autowired
  private Customers customers;

  @Test
  void shouldCreateCustomer() {
    var addressData = uniqueAddressData();
    var customerInput = createCustomerInput(false, addressData);

    var customerId = customerManagementApplicationService.create(customerInput);

    assertThat(customerId).isNotNull();
  }

  @Test
  void shouldThrowExceptionWhenCreatingCustomerWithDuplicateEmail() {
    var existingCustomer = CustomerTestDataBuilder.brandNewCustomer().build();
    customers.add(existingCustomer);

    var customerInput = createCustomerInput(false, uniqueAddressData());

    assertThatThrownBy(() -> customerManagementApplicationService.create(customerInput))
        .isInstanceOf(CustomerEmailIsInUseException.class);
  }

  @Test
  void shouldFindCustomerById() {
    var customer = CustomerTestDataBuilder.existedCustomer().build();
    customers.add(customer);

    var customerOutput = customerManagementApplicationService.findById(customer.id().value());

    assertThat(customerOutput.getId()).isEqualTo(customer.id().value());
    assertThat(customerOutput.getFirstName()).isEqualTo(customer.fullName().firstName());
    assertThat(customerOutput.getLastName()).isEqualTo(customer.fullName().lastName());
    assertThat(customerOutput.getEmail()).isEqualTo(customer.email().value());
    assertThat(customerOutput.getPhone()).isEqualTo(customer.phone());
    assertThat(customerOutput.getDocument()).isEqualTo(customer.document());
    assertThat(customerOutput.getBirthDate()).isEqualTo(customer.birthDate());
    assertThat(customerOutput.isPromotionNotificationsAllowed()).isEqualTo(customer.promotionNotificationsAllowed());
    assertThat(customerOutput.getLoyaltyPoints()).isEqualTo(customer.loyaltyPoints().value());
    assertThat(customerOutput.getRegisteredAt()).isEqualTo(customer.registeredAt());
    assertThat(customerOutput.isArchived()).isEqualTo(customer.archived());
    assertThat(customerOutput.getAddress().getStreet()).isEqualTo(customer.address().street());
    assertThat(customerOutput.getAddress().getNumber()).isEqualTo(customer.address().number());
    assertThat(customerOutput.getAddress().getComplement()).isEqualTo(customer.address().complement());
    assertThat(customerOutput.getAddress().getNeighboorhood()).isEqualTo(customer.address().neighborhood());
    assertThat(customerOutput.getAddress().getCity()).isEqualTo(customer.address().city());
    assertThat(customerOutput.getAddress().getState()).isEqualTo(customer.address().state());
    assertThat(customerOutput.getAddress().getZipCode()).isEqualTo(customer.address().zipCode().value());
  }

  @Test
  void shouldThrowExceptionWhenCustomerIsNotFound() {
    assertThatThrownBy(() -> customerManagementApplicationService.findById(UUID.randomUUID()))
        .isInstanceOf(CustomerNotFoundException.class);
  }

  @Test
  void shouldUpdateCustomer() {
    var customer = CustomerTestDataBuilder.existedCustomer().build();
    customers.add(customer);

    var customerUpdateInput = createCustomerUpdateInput();

    assertThatCode(() -> customerManagementApplicationService.update(customer.id().value(), customerUpdateInput))
        .doesNotThrowAnyException();

    var updatedCustomer = customers.findById(customer.id()).orElseThrow();
    assertThat(updatedCustomer.fullName().firstName()).isEqualTo(customerUpdateInput.getFirstName());
    assertThat(updatedCustomer.fullName().lastName()).isEqualTo(customerUpdateInput.getLastName());
    assertThat(updatedCustomer.phone()).isEqualTo(customerUpdateInput.getPhone());
    assertThat(updatedCustomer.promotionNotificationsAllowed()).isEqualTo(customerUpdateInput.getPromotionNotificationsAllowed());
    assertThat(updatedCustomer.address().street()).isEqualTo(customerUpdateInput.getAddress().getStreet());
    assertThat(updatedCustomer.address().number()).isEqualTo(customerUpdateInput.getAddress().getNumber());
    assertThat(updatedCustomer.address().complement()).isEqualTo(customerUpdateInput.getAddress().getComplement());
    assertThat(updatedCustomer.address().neighborhood()).isEqualTo(customerUpdateInput.getAddress().getNeighboorhood());
    assertThat(updatedCustomer.address().city()).isEqualTo(customerUpdateInput.getAddress().getCity());
    assertThat(updatedCustomer.address().state()).isEqualTo(customerUpdateInput.getAddress().getState());
    assertThat(updatedCustomer.address().zipCode().value()).isEqualTo(customerUpdateInput.getAddress().getZipCode());
  }

  @Test
  void shouldThrowExceptionWhenUpdatingCustomerThatDoesNotExist() {
    var customerUpdateInput = createCustomerUpdateInput();

    assertThatThrownBy(() -> customerManagementApplicationService.update(UUID.randomUUID(), customerUpdateInput))
        .isInstanceOf(CustomerNotFoundException.class);
  }

  private static CustomerInput createCustomerInput(boolean promotionNotificationsAllowed, AddressData addressData) {
    return CustomerInput.builder()
        .firstName(FIRST_NAME)
        .lastName(LAST_NAME)
        .email(EMAIL)
        .phone(PHONE)
        .document(DOCUMENT)
        .birthDate(BIRTH_DATE)
        .promotionNotificationsAllowed(promotionNotificationsAllowed)
        .address(addressData)
        .build();
  }

  private static CustomerUpdateInput createCustomerUpdateInput() {
    return CustomerUpdateInput.builder()
        .firstName("Maria")
        .lastName("Oliveira")
        .phone("21988887777")
        .promotionNotificationsAllowed(true)
        .address(AddressData.builder()
            .street("Rua das Flores")
            .number("456")
            .complement("Apto 101")
            .neighboorhood("Centro")
            .city("Rio de Janeiro")
            .state("RJ")
            .zipCode("20000-000")
            .build())
        .build();
  }

  private static AddressData uniqueAddressData() {
    return AddressData.builder()
        .street(BOURBON_STREET)
        .number(NUMBER)
        .neighboorhood(NORTH_VALLEY)
        .city(NEW_YORK)
        .state(NEW_YORK)
        .zipCode(ZIP_CODE)
        .build();
  }

}
