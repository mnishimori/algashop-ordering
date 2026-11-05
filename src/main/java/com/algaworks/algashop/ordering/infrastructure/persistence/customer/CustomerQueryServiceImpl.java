package com.algaworks.algashop.ordering.infrastructure.persistence.customer;

import com.algaworks.algashop.ordering.application.customer.query.CustomerOutput;
import com.algaworks.algashop.ordering.application.customer.query.CustomerQueryService;
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
    return repository.findByIdAsOutput(id).orElseThrow(() -> new CustomerNotFoundException(new CustomerId(id)));
  }
}
