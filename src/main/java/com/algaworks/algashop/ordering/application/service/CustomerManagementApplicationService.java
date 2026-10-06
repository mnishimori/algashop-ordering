package com.algaworks.algashop.ordering.application.service;

import com.algaworks.algashop.ordering.application.model.AddressData;
import com.algaworks.algashop.ordering.application.model.CustomerInput;
import com.algaworks.algashop.ordering.application.model.CustomerOutput;
import com.algaworks.algashop.ordering.domain.model.commons.Address;
import com.algaworks.algashop.ordering.domain.model.commons.Document;
import com.algaworks.algashop.ordering.domain.model.commons.Email;
import com.algaworks.algashop.ordering.domain.model.commons.FullName;
import com.algaworks.algashop.ordering.domain.model.commons.Phone;
import com.algaworks.algashop.ordering.domain.model.commons.ZipCode;
import com.algaworks.algashop.ordering.domain.model.customer.BirthDate;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerId;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerNotFoundException;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerRegistrationService;
import com.algaworks.algashop.ordering.domain.model.customer.Customers;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomerManagementApplicationService {

  private final CustomerRegistrationService customerRegistrationService;
  private final Customers customers;

  @Transactional
  public UUID create(CustomerInput customerInput) {
    Objects.requireNonNull(customerInput);

    var fullName = new FullName(customerInput.getFirstName(), customerInput.getLastName());
    var birthDate = new BirthDate(customerInput.getBirthDate());
    var email = new Email(customerInput.getEmail());
    var phone = new Phone(customerInput.getPhone());
    var document = new Document(customerInput.getDocument());
    var address = Address.builder().zipCode(new ZipCode(customerInput.getAddress().getZipCode()))
        .state(customerInput.getAddress().getState()).city(customerInput.getAddress().getCity())
        .neighborhood(customerInput.getAddress().getNeighboorhood())
        .street(customerInput.getAddress().getStreet()).number(customerInput.getAddress().getNumber())
        .complement(customerInput.getAddress().getComplement())
        .build();

    var customer = customerRegistrationService.register(fullName, birthDate.value(), email.value(), phone.value(),
        document.value(), customerInput.getPromotionNotificatiionsAllowed(), address);
    return customer.id().value();
  }

  @Transactional(readOnly = true)
  public CustomerOutput findById(UUID id) {
    Objects.requireNonNull(id);
    var customer = customers.findById(new CustomerId(id)).orElseThrow(() -> new CustomerNotFoundException(new CustomerId(id)));
    return CustomerOutput.builder()
        .id(customer.id().value())
        .firstName(customer.fullName().firstName())
        .lastName(customer.fullName().lastName())
        .email(customer.email().value())
        .phone(customer.phone())
        .document(customer.document())
        .birthDate(customer.birthDate())
        .promotionNotificationsAllowed(customer.promotionNotificationsAllowed())
        .loyaltyPoints(customer.loyaltyPoints().value())
        .registeredAt(customer.registeredAt())
        .archived(customer.archived())
        .address(AddressData.builder()
            .street(customer.address().street())
            .number(customer.address().number())
            .complement(customer.address().complement())
            .neighboorhood(customer.address().neighborhood())
            .city(customer.address().city())
            .state(customer.address().state())
            .zipCode(customer.address().zipCode().value())
            .build())
        .build();
  }
}
