package com.algaworks.algashop.ordering.domain.model.customer;

import com.algaworks.algashop.ordering.domain.model.IdGenerator;
import java.util.Objects;
import java.util.UUID;

public record CustomerId(UUID value) {

  public CustomerId(){
    var customerId = IdGenerator.generateTimeBasedUUID();
    this(customerId);
  }

  public CustomerId(UUID value) {
    this.value = Objects.requireNonNull(value);
  }

  @Override
  public String toString() {
    return value.toString();
  }
}
