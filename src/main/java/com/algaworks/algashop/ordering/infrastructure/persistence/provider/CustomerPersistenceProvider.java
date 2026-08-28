package com.algaworks.algashop.ordering.infrastructure.persistence.provider;

import com.algaworks.algashop.ordering.domain.model.entity.Customer;
import com.algaworks.algashop.ordering.domain.model.repository.Customers;
import com.algaworks.algashop.ordering.domain.model.valueobject.Email;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.infrastructure.persistence.assembler.CustomerPersistenceEntityAssembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.disassembler.CustomerPersistenceEntityDisassembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.CustomerPersistenceEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.repository.CustomerPersistenceEntityRepository;
import jakarta.persistence.EntityManager;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

@Component
@RequiredArgsConstructor
public class CustomerPersistenceProvider implements Customers {

  private final CustomerPersistenceEntityRepository customerPersistenceEntityRepository;
  private final CustomerPersistenceEntityDisassembler disassembler;
  private final CustomerPersistenceEntityAssembler assembler;
  private final EntityManager entityManager;

  @Override
  public Optional<Customer> findById(CustomerId customerId) {
    return customerPersistenceEntityRepository.findById(customerId.value()).map(disassembler::toDomainEntity);
  }

  @Override
  public boolean existsById(CustomerId customerId) {
    return customerPersistenceEntityRepository.existsById(customerId.value());
  }

  @Override
  public void add(Customer aggregateRoot) {
    var customerId = aggregateRoot.id();
    var persistenceEntity = customerPersistenceEntityRepository.findById(customerId.value());
    if (persistenceEntity.isPresent()) {
      update(aggregateRoot, persistenceEntity.get());
    } else {
      insert(aggregateRoot);
    }
  }

  private void update(Customer aggregateRoot, CustomerPersistenceEntity persistenceEntity) {
    var persistenceEntityMerged = assembler.merge(persistenceEntity, aggregateRoot);
    entityManager.detach(persistenceEntity);
    persistenceEntity = customerPersistenceEntityRepository.saveAndFlush(persistenceEntityMerged);
    updateVersion(aggregateRoot, persistenceEntity);
  }

  @SneakyThrows
  private void updateVersion(Customer aggregateRoot, CustomerPersistenceEntity persistenceEntity) {
    var version = aggregateRoot.getClass().getDeclaredField("version");
    version.setAccessible(true);
    ReflectionUtils.setField(version, aggregateRoot, persistenceEntity.getVersion());
    version.setAccessible(false);
  }

  private void insert(Customer aggregateRoot) {
    var persistenceEntity = assembler.fromDomain(aggregateRoot);
    customerPersistenceEntityRepository.saveAndFlush(persistenceEntity);
  }

  @Override
  public int count() {
    return (int) customerPersistenceEntityRepository.count();
  }

  @Override
  public Optional<Customer> ofEmail(Email email) {
    return customerPersistenceEntityRepository.findByEmail(email.value()).map(disassembler::toDomainEntity);
  }

  @Override
  public boolean isEmailUnique(Email email, CustomerId customerId) {
    return !customerPersistenceEntityRepository.existsByEmailAndIdNot(email.value(), customerId.value());
  }
}
