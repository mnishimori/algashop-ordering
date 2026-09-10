package com.algaworks.algashop.ordering.domain.model.service;

import com.algaworks.algashop.ordering.domain.model.entity.Customer;
import com.algaworks.algashop.ordering.domain.model.exception.CustomerEmailIsInUseException;
import com.algaworks.algashop.ordering.domain.model.repository.Customers;
import com.algaworks.algashop.ordering.domain.model.valueobject.Address;
import com.algaworks.algashop.ordering.domain.model.valueobject.Email;
import com.algaworks.algashop.ordering.domain.model.valueobject.FullName;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomerRegistrationService {

  private final Customers customers;

  public Customer register(FullName fullName, LocalDate birthDate, String email, String phone, String document,
      Boolean promotionNotificationsAllowed, Address address) {
    var customer = Customer.brandnew().fullName(fullName).birthDate(birthDate).email(email).phone(phone)
        .document(document).promotionNotificationsAllowed(promotionNotificationsAllowed).address(address).build();
    verifyEmailUniqueness(customer.email(), customer.id());
    return customer;
  }

  public void changeEmail(Customer customer, Email email) {
    verifyEmailUniqueness(email, customer.id());
    customer.changeEmail(email);
  }

  private void verifyEmailUniqueness(Email email, CustomerId customerId) {
    if (!customers.isEmailUnique(email, customerId)) {
      throw new CustomerEmailIsInUseException();
    }
  }
}
