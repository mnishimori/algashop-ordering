package com.algaworks.algashop.ordering.domain.model.service;

import static com.algaworks.algashop.ordering.domain.entity.CustomerTestDataBuilder.BIRTH_DATE;
import static com.algaworks.algashop.ordering.domain.entity.CustomerTestDataBuilder.DOCUMENT;
import static com.algaworks.algashop.ordering.domain.entity.CustomerTestDataBuilder.EMAIL;
import static com.algaworks.algashop.ordering.domain.entity.CustomerTestDataBuilder.FIRST_NAME;
import static com.algaworks.algashop.ordering.domain.entity.CustomerTestDataBuilder.LAST_NAME;
import static com.algaworks.algashop.ordering.domain.entity.CustomerTestDataBuilder.PHONE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.algaworks.algashop.ordering.IntegrationTest;
import com.algaworks.algashop.ordering.domain.entity.CustomerTestDataBuilder;
import com.algaworks.algashop.ordering.domain.model.entity.Customer;
import com.algaworks.algashop.ordering.domain.model.exception.CustomerEmailIsInUseException;
import com.algaworks.algashop.ordering.domain.model.repository.Customers;
import com.algaworks.algashop.ordering.domain.model.valueobject.Email;
import com.algaworks.algashop.ordering.domain.model.valueobject.FullName;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
class CustomerRegistrationServiceIntegrationTest {

  @Autowired
  private CustomerRegistrationService customerRegistrationService;

  @Autowired
  private Customers customers;

  @Test
  void shouldRegister() {
    var address = CustomerTestDataBuilder.brandNewCustomer().build().address();

    Customer customer = customerRegistrationService.register(
        new FullName(FIRST_NAME, LAST_NAME),
        BIRTH_DATE,
        EMAIL,
        PHONE,
        DOCUMENT,
        false,
        address);

    assertThat(customer.fullName()).isEqualTo(new FullName(FIRST_NAME, LAST_NAME));
    assertThat(customer.birthDate()).isEqualTo(BIRTH_DATE);
    assertThat(customer.email()).isEqualTo(new Email(EMAIL));
    assertThat(customer.phone()).isEqualTo(PHONE);
    assertThat(customer.document()).isEqualTo(DOCUMENT);
    assertThat(customer.promotionNotificationsAllowed()).isFalse();
    assertThat(customer.address()).isEqualTo(address);
  }

  @Test
  void shouldThrowExceptionWhenRegisteringWithEmailAlreadyInUse() {
    var existingCustomer = CustomerTestDataBuilder.brandNewCustomer().build();
    customers.add(existingCustomer);

    var address = CustomerTestDataBuilder.brandNewCustomer().build().address();

    assertThatThrownBy(() -> customerRegistrationService.register(
        new FullName(FIRST_NAME, LAST_NAME),
        BIRTH_DATE,
        EMAIL,
        PHONE,
        DOCUMENT,
        false,
        address)).isInstanceOf(CustomerEmailIsInUseException.class);
  }

  @Test
  void shouldChangeEmail() {
    var customer = CustomerTestDataBuilder.existedCustomer()
        .customerId(new CustomerId()).build();
    var newEmail = new Email("maria.silva@example.com");

    customerRegistrationService.changeEmail(customer, newEmail);

    assertThat(customer.email()).isEqualTo(newEmail);
  }

  @Test
  void shouldThrowExceptionWhenChangingEmailToAlreadyUsedEmail() {
    var anotherCustomer = CustomerTestDataBuilder.brandNewCustomer()
        .email("maria.silva@example.com").build();
    customers.add(anotherCustomer);

    var customer = CustomerTestDataBuilder.existedCustomer()
        .customerId(new CustomerId()).build();
    var newEmail = new Email("maria.silva@example.com");

    assertThatThrownBy(() -> customerRegistrationService.changeEmail(customer, newEmail))
        .isInstanceOf(CustomerEmailIsInUseException.class);
  }

}