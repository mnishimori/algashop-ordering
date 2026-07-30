package com.algaworks.algashop.ordering.infrastructure.persistence.assembler;

import com.algaworks.algashop.ordering.domain.model.entity.Customer;
import com.algaworks.algashop.ordering.infrastructure.persistence.embeddable.AddressEmbeddable;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.CustomerPersistenceEntity;
import org.springframework.stereotype.Component;

@Component
public class CustomerPersistenceEntityAssembler {

  public CustomerPersistenceEntity fromDomain(Customer customer) {
    return merge(new CustomerPersistenceEntity(), customer);
  }

  public CustomerPersistenceEntity merge(CustomerPersistenceEntity customerPersistenceEntity, Customer customer) {
    customerPersistenceEntity.setId(customer.id().value());
    setFullName(customerPersistenceEntity, customer);
    customerPersistenceEntity.setBirthDate(customer.birthDate());
    customerPersistenceEntity.setEmail(customer.email());
    customerPersistenceEntity.setPhone(customer.phone());
    customerPersistenceEntity.setDocument(customer.document());
    customerPersistenceEntity.setPromotionNotificationsAllowed(customer.promotionNotificationsAllowed());
    customerPersistenceEntity.setArchived(customer.archived());
    customerPersistenceEntity.setArchivedAt(customer.archivedAt());
    customerPersistenceEntity.setLoyaltyPoints(getLoyaltyPointsValue(customer));
    setAddressInformation(customerPersistenceEntity, customer);
    return customerPersistenceEntity;
  }

  private void setFullName(CustomerPersistenceEntity customerPersistenceEntity, Customer customer) {
    if (customer.fullName() == null) {
      return;
    }
    customerPersistenceEntity.setFirstName(customer.fullName().firstName());
    customerPersistenceEntity.setLastName(customer.fullName().lastName());
  }

  private Integer getLoyaltyPointsValue(Customer customer) {
    return customer.loyaltyPoints() != null ? customer.loyaltyPoints().value() : null;
  }

  private void setAddressInformation(CustomerPersistenceEntity customerPersistenceEntity, Customer customer) {
    var address = customer.address();
    if (address == null) {
      return;
    }
    var addressEmbeddable = customerPersistenceEntity.getAddressEmbeddable();
    if (addressEmbeddable == null) {
      addressEmbeddable = new AddressEmbeddable();
      customerPersistenceEntity.setAddressEmbeddable(addressEmbeddable);
    }
    addressEmbeddable.setStreet(address.street());
    addressEmbeddable.setNumber(address.number());
    addressEmbeddable.setComplement(address.complement());
    addressEmbeddable.setNeighborhood(address.neighborhood());
    addressEmbeddable.setCity(address.city());
    addressEmbeddable.setState(address.state());
    if (address.zipCode() != null) {
      addressEmbeddable.setZipCode(address.zipCode().toString());
    }
  }
}
