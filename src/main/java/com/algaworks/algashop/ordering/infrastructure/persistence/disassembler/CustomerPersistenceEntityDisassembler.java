package com.algaworks.algashop.ordering.infrastructure.persistence.disassembler;

import com.algaworks.algashop.ordering.domain.model.entity.Customer;
import com.algaworks.algashop.ordering.domain.model.valueobject.Address;
import com.algaworks.algashop.ordering.domain.model.valueobject.FullName;
import com.algaworks.algashop.ordering.domain.model.valueobject.LoyaltyPoints;
import com.algaworks.algashop.ordering.domain.model.valueobject.ZipCode;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.CustomerPersistenceEntity;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Component;

@Component
public class CustomerPersistenceEntityDisassembler {

  public Customer toDomainEntity(CustomerPersistenceEntity customerPersistenceEntity) {
    return Customer.existed()
        .customerId(new CustomerId(customerPersistenceEntity.getId()))
        .fullName(new FullName(customerPersistenceEntity.getFirstName(), customerPersistenceEntity.getLastName()))
        .birthDate(customerPersistenceEntity.getBirthDate())
        .email(customerPersistenceEntity.getEmail())
        .phone(customerPersistenceEntity.getPhone())
        .document(customerPersistenceEntity.getDocument())
        .promotionNotificationsAllowed(customerPersistenceEntity.getPromotionNotificationsAllowed())
        .archived(customerPersistenceEntity.getArchived())
        .archivedAt(customerPersistenceEntity.getArchivedAt())
        .registeredAt(customerPersistenceEntity.getRegisteredAt())
        .loyaltyPoints(getLoyaltyPoints(customerPersistenceEntity))
        .address(getAddress(customerPersistenceEntity))
        .build();
  }

  @Nullable
  private LoyaltyPoints getLoyaltyPoints(CustomerPersistenceEntity customerPersistenceEntity) {
    if (customerPersistenceEntity.getLoyaltyPoints() == null) {
      return null;
    }
    return new LoyaltyPoints(customerPersistenceEntity.getLoyaltyPoints());
  }

  private Address getAddress(CustomerPersistenceEntity customerPersistenceEntity) {
    if (customerPersistenceEntity.getAddressEmbeddable() == null) {
      return null;
    }
    var addressEmbeddable = customerPersistenceEntity.getAddressEmbeddable();
    var address = Address.builder();
    if (addressEmbeddable.getStreet() != null && !addressEmbeddable.getStreet().isBlank()) {
      address.street(addressEmbeddable.getStreet());
    }
    if (addressEmbeddable.getNumber() != null && !addressEmbeddable.getNumber().isBlank()) {
      address.number(addressEmbeddable.getNumber());
    }
    if (addressEmbeddable.getComplement() != null && !addressEmbeddable.getComplement().isBlank()) {
      address.complement(addressEmbeddable.getComplement());
    }
    if (addressEmbeddable.getNeighborhood() != null && !addressEmbeddable.getNeighborhood().isBlank()) {
      address.neighborhood(addressEmbeddable.getNeighborhood());
    }
    if (addressEmbeddable.getCity() != null && !addressEmbeddable.getCity().isBlank()) {
      address.city(addressEmbeddable.getCity());
    }
    if (addressEmbeddable.getState() != null && !addressEmbeddable.getState().isBlank()) {
      address.state(addressEmbeddable.getState());
    }
    if (addressEmbeddable.getZipCode() != null && !addressEmbeddable.getZipCode().isBlank()) {
      address.zipCode(new ZipCode(addressEmbeddable.getZipCode()));
    }
    return address.build();
  }
}
