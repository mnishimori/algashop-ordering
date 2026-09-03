package com.algaworks.algashop.ordering.infrastructure.persistence.provider;

import com.algaworks.algashop.ordering.domain.model.entity.ShoppingCart;
import com.algaworks.algashop.ordering.domain.model.repository.ShoppingCarts;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.ShoppingCartId;
import com.algaworks.algashop.ordering.infrastructure.persistence.assembler.ShoppingCartPersistenceEntityAssembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.disassembler.ShoppingCartPersistenceEntityDisassembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.repository.ShoppingCartPersistenceEntityRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ShoppingCartPersistenceProvider implements ShoppingCarts {

  private final ShoppingCartPersistenceEntityRepository repository;
  private final ShoppingCartPersistenceEntityDisassembler disassembler;
  private final ShoppingCartPersistenceEntityAssembler assembler;

  @Override
  public Optional<ShoppingCart> ofCustomer(CustomerId customerId) {
    return repository.findAll().stream()
        .filter(shoppingCartPersistenceEntity -> shoppingCartPersistenceEntity.getCustomer() != null)
        .filter(shoppingCartPersistenceEntity -> customerId.value().equals(shoppingCartPersistenceEntity.getCustomer().getId()))
        .findFirst()
        .map(disassembler::toDomainEntity);
  }

  @Override
  public void remove(ShoppingCart aggregateRoot) {
    repository.deleteById(aggregateRoot.id().value());
  }

  @Override
  public void removeById(ShoppingCartId shoppingCartId) {
    repository.deleteById(shoppingCartId.value());
  }

  @Override
  public Optional<ShoppingCart> findById(ShoppingCartId shoppingCartId) {
    return repository.findById(shoppingCartId.value()).map(disassembler::toDomainEntity);
  }

  @Override
  public boolean existsById(ShoppingCartId shoppingCartId) {
    return repository.existsById(shoppingCartId.value());
  }

  @Override
  public void add(ShoppingCart aggregateRoot) {
    var shoppingCartId = aggregateRoot.id();
    var persistenceEntity = repository.findById(shoppingCartId.value());
    if (persistenceEntity.isPresent()) {
      repository.saveAndFlush(assembler.merge(persistenceEntity.get(), aggregateRoot));
    } else {
      repository.saveAndFlush(assembler.fromDomain(aggregateRoot));
    }
  }

  @Override
  public int count() {
    return (int) repository.count();
  }
}
