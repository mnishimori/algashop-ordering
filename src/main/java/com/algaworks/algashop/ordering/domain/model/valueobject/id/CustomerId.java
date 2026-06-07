package com.algaworks.algashop.ordering.domain.model.valueobject.id;

import com.algaworks.algashop.ordering.domain.model.utility.IdGenerator;
import java.util.Objects;
import java.util.UUID;

public record CustomerId(UUID value) {

  public CustomerId(){
    var customerId = IdGenerator.generateTimeBasedUuid();
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
