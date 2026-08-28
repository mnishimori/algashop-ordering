package com.algaworks.algashop.ordering.infrastructure.persistence.provider;

import com.algaworks.algashop.ordering.domain.model.entity.Order;
import com.algaworks.algashop.ordering.domain.model.repository.Orders;
import com.algaworks.algashop.ordering.domain.model.valueobject.Money;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderId;
import com.algaworks.algashop.ordering.infrastructure.persistence.assembler.OrderPersistenceEntityAssembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.disassembler.OrderPersistenceEntityDisassembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.repository.OrderPersistenceEntityRepository;
import jakarta.persistence.EntityManager;
import java.time.Year;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

@Component
@RequiredArgsConstructor
public class OrdersPersistenceProvider implements Orders {

  private final OrderPersistenceEntityRepository repository;
  private final OrderPersistenceEntityDisassembler disassembler;
  private final OrderPersistenceEntityAssembler assembler;
  private final EntityManager entityManager;

  @EntityGraph(attributePaths = {"items"})
  @Override
  public Optional<Order> findById(OrderId orderId) {
    return repository.findById(orderId.value().toLong()).map(disassembler::toDomainEntity);
  }

  @Override
  public boolean existsById(OrderId orderId) {
    return repository.existsById(orderId.value().toLong());
  }

  @Override
  public void add(Order aggregateRoot) {
    var orderId = aggregateRoot.id();
    var persistenceEntity = repository.findById(orderId.value().toLong());
    if (persistenceEntity.isPresent()) {
      update(aggregateRoot, persistenceEntity.get());
    } else {
      insert(aggregateRoot);
    }
  }

  private void update(Order aggregateRoot, OrderPersistenceEntity persistenceEntity) {
    var persistenceEntityMerged = assembler.merge(persistenceEntity, aggregateRoot);
    entityManager.detach(persistenceEntity);
    persistenceEntity = repository.saveAndFlush(persistenceEntityMerged);
    updateVersion(aggregateRoot, persistenceEntity);
  }

  @SneakyThrows
  private void updateVersion(Order aggregateRoot, OrderPersistenceEntity persistenceEntity) {
    var version = aggregateRoot.getClass().getDeclaredField("version");
    version.setAccessible(true);
    ReflectionUtils.setField(version, aggregateRoot, persistenceEntity.getVersion());
    version.setAccessible(false);
  }

  private void insert(Order aggregateRoot) {
    var persistenceEntity = assembler.fromDomain(aggregateRoot);
    repository.saveAndFlush(persistenceEntity);
  }

  @Override
  public int count() {
    return (int) repository.count();
  }

  @Override
  public List<Order> placedByCustomerInYear(CustomerId customerId, Year year) {
    var orders = repository.placedByCustomerInYear(customerId.value(), year.getValue());
    return orders.stream().map(disassembler::toDomainEntity).toList();
  }

  @Override
  public long salesQuantityByCustomerInYear(CustomerId customerId, Year year) {
    return repository.salesQuantityByCustomerInYear(customerId.value(), year.getValue());
  }

  @Override
  public Money totalSoldForCustomer(CustomerId customerId) {
    return new Money(repository.totalSoldForCustomer(customerId.value()));
  }
}
