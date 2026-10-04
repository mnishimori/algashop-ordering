package com.algaworks.algashop.ordering.domain.model.order;

import com.algaworks.algashop.ordering.domain.model.commons.Address;
import com.algaworks.algashop.ordering.domain.model.commons.Document;
import com.algaworks.algashop.ordering.domain.model.commons.Email;
import com.algaworks.algashop.ordering.domain.model.commons.FullName;
import com.algaworks.algashop.ordering.domain.model.commons.Phone;
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
