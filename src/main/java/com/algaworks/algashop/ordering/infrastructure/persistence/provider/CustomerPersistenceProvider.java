package com.algaworks.algashop.ordering.infrastructure.persistence.provider;

import com.algaworks.algashop.ordering.domain.model.entity.Customer;
import com.algaworks.algashop.ordering.domain.model.repository.Customers;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.infrastructure.persistence.disassembler.CustomerPersistenceEntityDisassembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.repository.CustomerPersistenceEntityRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerPersistenceProvider implements Customers {

  private CustomerPersistenceEntityRepository customerPersistenceEntityRepository;
  private CustomerPersistenceEntityDisassembler disassembler;

  @Override
  public Optional<Customer> findById(CustomerId customerId) {
    return customerPersistenceEntityRepository.findById(customerId.value()).map(disassembler::toDomainEntity);
  }

  @Override
  public boolean existsById(CustomerId customerId) {
    return false;
  }

  @Override
  public void add(Customer aggregateRoot) {

  }

  @Override
  public int count() {
    return 0;
  }
}
