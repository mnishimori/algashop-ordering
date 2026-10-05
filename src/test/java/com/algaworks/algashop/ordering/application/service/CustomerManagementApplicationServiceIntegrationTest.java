package com.algaworks.algashop.ordering.application.service;

import static com.algaworks.algashop.ordering.domain.entity.CustomerTestDataBuilder.BIRTH_DATE;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.algaworks.algashop.ordering.IntegrationTest;
import com.algaworks.algashop.ordering.application.model.AddressData;
import com.algaworks.algashop.ordering.application.model.CustomerInput;
import com.algaworks.algashop.ordering.domain.entity.CustomerTestDataBuilder;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerEmailIsInUseException;
import com.algaworks.algashop.ordering.domain.model.customer.Customers;
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

  private static CustomerInput createCustomerInput(boolean promotionNotificationsAllowed, AddressData addressData) {
    return CustomerInput.builder()
        .firstName(FIRST_NAME)
        .lastName(LAST_NAME)
        .email(EMAIL)
        .phone(PHONE)
        .document(DOCUMENT)
        .birthDate(BIRTH_DATE)
        .promotionNotificatiionsAllowed(promotionNotificationsAllowed)
        .address(addressData)
        .build();
  }

  private static AddressData uniqueAddressData() {
    return AddressData.builder()
        .street("Bourbon Street")
        .number(NUMBER)
        .neighboorhood(NORTH_VALLEY)
        .city(NEW_YORK)
        .state(NEW_YORK)
        .zipCode(ZIP_CODE)
        .build();
  }

}
