package com.algaworks.algashop.ordering.domain.model.service;

import static com.algaworks.algashop.ordering.domain.entity.CustomerTestDataBuilder.BIRTH_DATE;
import static com.algaworks.algashop.ordering.domain.entity.CustomerTestDataBuilder.DOCUMENT;
import static com.algaworks.algashop.ordering.domain.entity.CustomerTestDataBuilder.EMAIL;
import static com.algaworks.algashop.ordering.domain.entity.CustomerTestDataBuilder.FIRST_NAME;
import static com.algaworks.algashop.ordering.domain.entity.CustomerTestDataBuilder.LAST_NAME;
import static com.algaworks.algashop.ordering.domain.entity.CustomerTestDataBuilder.PHONE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.algaworks.algashop.ordering.domain.entity.CustomerTestDataBuilder;
import com.algaworks.algashop.ordering.domain.model.entity.Customer;
import com.algaworks.algashop.ordering.domain.model.exception.CustomerEmailIsInUseException;
import com.algaworks.algashop.ordering.domain.model.repository.Customers;
import com.algaworks.algashop.ordering.domain.model.valueobject.Email;
import com.algaworks.algashop.ordering.domain.model.valueobject.FullName;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CustomerRegistrationServiceTest {

  @Mock
  private Customers customers;

  @InjectMocks
  private CustomerRegistrationService customerRegistrationService;

  @Test
  void shouldRegister() {
    var address = CustomerTestDataBuilder.brandNewCustomer().build().address();
    when(customers.isEmailUnique(eq(new Email(EMAIL)), any(CustomerId.class))).thenReturn(true);

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
    verify(customers).isEmailUnique(new Email(EMAIL), customer.id());
  }

  @Test
  void shouldThrowExceptionWhenRegisteringWithEmailAlreadyInUse() {
    var address = CustomerTestDataBuilder.brandNewCustomer().build().address();
    when(customers.isEmailUnique(eq(new Email(EMAIL)), any(CustomerId.class))).thenReturn(false);

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
    var customer = CustomerTestDataBuilder.existedCustomer().build();
    var newEmail = new Email("maria.silva@example.com");
    when(customers.isEmailUnique(eq(newEmail), eq(customer.id()))).thenReturn(true);

    customerRegistrationService.changeEmail(customer, newEmail);

    assertThat(customer.email()).isEqualTo(newEmail);
    verify(customers).isEmailUnique(newEmail, customer.id());
  }

  @Test
  void shouldThrowExceptionWhenChangingEmailToAlreadyUsedEmail() {
    var customer = CustomerTestDataBuilder.existedCustomer().build();
    var newEmail = new Email("maria.silva@example.com");
    when(customers.isEmailUnique(eq(newEmail), eq(customer.id()))).thenReturn(false);

    assertThatThrownBy(() -> customerRegistrationService.changeEmail(customer, newEmail))
        .isInstanceOf(CustomerEmailIsInUseException.class);
    verify(customers).isEmailUnique(newEmail, customer.id());
  }

}