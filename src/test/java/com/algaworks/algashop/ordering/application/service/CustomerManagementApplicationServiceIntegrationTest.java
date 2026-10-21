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
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.algaworks.algashop.ordering.IntegrationTest;
import com.algaworks.algashop.ordering.application.commons.AddressData;
import com.algaworks.algashop.ordering.application.customer.management.CustomerInput;
import com.algaworks.algashop.ordering.application.customer.management.CustomerManagementApplicationService;
import com.algaworks.algashop.ordering.application.customer.management.CustomerUpdateInput;
import com.algaworks.algashop.ordering.application.customer.notification.CustomerNotificationApplicationService;
import com.algaworks.algashop.ordering.application.customer.notification.CustomerNotificationApplicationService.NotifyNewRegistrationInput;
import com.algaworks.algashop.ordering.domain.entity.CustomerTestDataBuilder;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerArchivedException;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerEmailIsInUseException;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerId;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerNotFoundException;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerRegisteredEvent;
import com.algaworks.algashop.ordering.domain.model.customer.Customers;
import com.algaworks.algashop.ordering.infrastructure.listener.customer.CustomerEventListener;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

@IntegrationTest
class CustomerManagementApplicationServiceIntegrationTest {

  @Autowired
  private CustomerManagementApplicationService customerManagementApplicationService;
  @Autowired
  private Customers customers;
  @MockitoSpyBean
  private CustomerEventListener customerEventListener;
  @MockitoSpyBean
  private CustomerNotificationApplicationService customerNotificationApplicationService;

  @Test
  void shouldCreateCustomer() {
    var addressData = uniqueAddressData();
    var customerInput = createCustomerInput(false, addressData);

    var customerId = customerManagementApplicationService.create(customerInput);

    assertThat(customerId).isNotNull();
    assertThat(customers.existsById(new CustomerId(customerId))).isTrue();
    var saved = customers.findById(new CustomerId(customerId)).orElseThrow();
    assertThat(saved.fullName().firstName()).isEqualTo(customerInput.getFirstName());
    assertThat(saved.fullName().lastName()).isEqualTo(customerInput.getLastName());
    assertThat(saved.email().value()).isEqualTo(customerInput.getEmail());
    assertThat(saved.document()).isEqualTo(customerInput.getDocument());
    assertThat(saved.phone()).isEqualTo(customerInput.getPhone());
    assertThat(saved.archived()).isFalse();
    verify(customerEventListener).listen(any(CustomerRegisteredEvent.class));
    verify(customerNotificationApplicationService).notifyNewCustomerRegistration(any(NotifyNewRegistrationInput.class));
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
    assertThat(customerOutput.getRegisteredAt().toLocalDate()).isEqualTo(customer.registeredAt().toLocalDate());
    assertThat(customerOutput.isArchived()).isEqualTo(customer.archived());
    assertThat(customerOutput.getAddress().getStreet()).isEqualTo(customer.address().street());
    assertThat(customerOutput.getAddress().getNumber()).isEqualTo(customer.address().number());
    assertThat(customerOutput.getAddress().getComplement()).isEqualTo(customer.address().complement());
    assertThat(customerOutput.getAddress().getNeighbourhood()).isEqualTo(customer.address().neighborhood());
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
    assertThat(updatedCustomer.promotionNotificationsAllowed()).isEqualTo(
        customerUpdateInput.getPromotionNotificationsAllowed());
    assertThat(updatedCustomer.address().street()).isEqualTo(customerUpdateInput.getAddress().getStreet());
    assertThat(updatedCustomer.address().number()).isEqualTo(customerUpdateInput.getAddress().getNumber());
    assertThat(updatedCustomer.address().complement()).isEqualTo(customerUpdateInput.getAddress().getComplement());
    assertThat(updatedCustomer.address().neighborhood()).isEqualTo(customerUpdateInput.getAddress().getNeighbourhood());
    assertThat(updatedCustomer.address().city()).isEqualTo(customerUpdateInput.getAddress().getCity());
    assertThat(updatedCustomer.address().state()).isEqualTo(customerUpdateInput.getAddress().getState());
    assertThat(updatedCustomer.address().zipCode().value()).isEqualTo(customerUpdateInput.getAddress().getZipCode());
  }

  @Test
  void shouldThrowExceptionWhenUpdatingCustomerThatDoesNotExist() {
    var customerUpdateInput = createCustomerUpdateInput();

    assertThatThrownBy(() -> customerManagementApplicationService.update(UUID.randomUUID(), customerUpdateInput))
        .isInstanceOf(CustomerNotFoundException.class);
    verify(customerEventListener, never()).listen(any(CustomerRegisteredEvent.class));
  }

  @Test
  void shouldArchiveCustomer() {
    var customer = CustomerTestDataBuilder.existedCustomer().build();
    customers.add(customer);

    assertThatCode(() -> customerManagementApplicationService.archive(customer.id()))
        .doesNotThrowAnyException();

    var archivedCustomer = customers.findById(customer.id()).orElseThrow();
    assertThat(archivedCustomer.archived()).isTrue();
    assertThat(archivedCustomer.archivedAt()).isNotNull();
    assertThat(archivedCustomer.fullName().firstName()).isEqualTo("Anounymous");
    assertThat(archivedCustomer.fullName().lastName()).isEqualTo("Anounymous");
    assertThat(archivedCustomer.email().value()).endsWith("@anonymous.com");
    assertThat(archivedCustomer.phone()).isEqualTo("0000-0000");
    assertThat(archivedCustomer.document()).isEqualTo("00000000000");
    assertThat(archivedCustomer.birthDate()).isNull();
    assertThat(archivedCustomer.promotionNotificationsAllowed()).isFalse();
    assertThat(archivedCustomer.address().number()).isEqualTo("Anounymous");
    assertThat(archivedCustomer.address().complement()).isEqualTo("Anounymous");
    assertThatThrownBy(() -> customerManagementApplicationService.archive(customer.id()))
        .isInstanceOf(CustomerArchivedException.class);
  }

  @Test
  void shouldThrowExceptionWhenArchivingCustomerThatDoesNotExist() {
    assertThatThrownBy(() -> customerManagementApplicationService.archive(new CustomerId()))
        .isInstanceOf(CustomerNotFoundException.class);
  }

  @Test
  void shouldThrowExceptionWhenArchivingAlreadyArchivedCustomer() {
    var customer = CustomerTestDataBuilder.existedCustomer().build();
    customers.add(customer);

    customerManagementApplicationService.archive(customer.id());

    assertThatThrownBy(() -> customerManagementApplicationService.archive(customer.id()))
        .isInstanceOf(CustomerArchivedException.class);
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
            .neighbourhood("Centro")
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
        .neighbourhood(NORTH_VALLEY)
        .city(NEW_YORK)
        .state(NEW_YORK)
        .zipCode(ZIP_CODE)
        .build();
  }

}
