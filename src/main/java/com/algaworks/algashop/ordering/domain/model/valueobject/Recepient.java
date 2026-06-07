package com.algaworks.algashop.ordering.domain.model.valueobject;

import java.util.Objects;
import lombok.Builder;

@Builder
public record Recepient(FullName fullName, Document document, Phone phone) {

  public Recepient {
    Objects.requireNonNull(fullName);
    Objects.requireNonNull(document);
    Objects.requireNonNull(phone);
  }
}
