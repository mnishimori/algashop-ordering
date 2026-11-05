package com.algaworks.algashop.ordering.infrastructure.persistence.customer;

import com.algaworks.algashop.ordering.application.commons.AddressData;
import com.algaworks.algashop.ordering.application.customer.management.CustomerOutput;
import com.algaworks.algashop.ordering.application.customer.management.CustomerQueryService;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerId;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerNotFoundException;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CustomerQueryServiceImpl implements CustomerQueryService {

  private final CustomerPersistenceEntityRepository repository;

  @Override
  @Transactional(readOnly = true)
  public CustomerOutput findById(UUID id) {
    Objects.requireNonNull(id);
    var customer = repository.findById(id).orElseThrow(() -> new CustomerNotFoundException(new CustomerId(id)));
    return CustomerOutput.builder()
        .id(customer.getId())
        .firstName(customer.getFirstName())
        .lastName(customer.getLastName())
        .email(customer.getEmail())
        .phone(customer.getPhone())
        .document(customer.getDocument())
        .birthDate(customer.getBirthDate())
        .promotionNotificationsAllowed(customer.getPromotionNotificationsAllowed())
        .loyaltyPoints(customer.getLoyaltyPoints())
        .registeredAt(customer.getRegisteredAt())
        .archived(customer.getArchived())
        .address(AddressData.builder()
            .street(customer.getAddressEmbeddable().getStreet())
            .number(customer.getAddressEmbeddable().getNumber())
            .complement(customer.getAddressEmbeddable().getComplement())
            .neighbourhood(customer.getAddressEmbeddable().getNeighborhood())
            .city(customer.getAddressEmbeddable().getCity())
            .state(customer.getAddressEmbeddable().getState())
            .zipCode(customer.getAddressEmbeddable().getZipCode())
            .build())
        .build();
  }
}
