package com.algaworks.algashop.ordering.infrastructure.persistence.provider;

import com.algaworks.algashop.ordering.domain.model.entity.Order;
import com.algaworks.algashop.ordering.domain.model.repository.Orders;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderId;
import com.algaworks.algashop.ordering.infrastructure.persistence.assembler.OrderPersistenceEntityAssembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.disassembler.OrderPersistenceEntityDisassembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.repository.OrderPersistenceEntityRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrdersPersistenceProvider implements Orders {

  private final OrderPersistenceEntityRepository repository;
  private final OrderPersistenceEntityDisassembler disassembler;
  private final OrderPersistenceEntityAssembler assembler;

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
    var entity = assembler.fromDomain(aggregateRoot);
    repository.save(entity);
  }

  @Override
  public int count() {
    return (int) repository.count();
  }

}
