package com.algaworks.algashop.ordering.domain.model.valueobject;

import java.util.Objects;
import lombok.Builder;

@Builder
public record Billing(FullName fullName, Document document, Phone phone, Address address, Email email) {

  public Billing {
    Objects.requireNonNull(fullName);
    Objects.requireNonNull(document);
    Objects.requireNonNull(phone);
    Objects.requireNonNull(address);
    Objects.requireNonNull(email);
  }
}
