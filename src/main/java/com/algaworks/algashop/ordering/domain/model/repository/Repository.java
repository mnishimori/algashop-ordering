package com.algaworks.algashop.ordering.domain.model.repository;

import com.algaworks.algashop.ordering.domain.model.entity.AggregateRoot;
import java.util.Optional;

public interface Repository<T extends AggregateRoot<ID>, ID> {

  Optional<T> findById(ID id);
  boolean existsById(ID id);
  void add(T aggregateRoot);
  int count();
}
