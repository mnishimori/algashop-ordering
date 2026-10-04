package com.algaworks.algashop.ordering.domain.model.order;

import com.algaworks.algashop.ordering.domain.model.commons.Address;
import com.algaworks.algashop.ordering.domain.model.commons.Money;
import java.time.LocalDate;
import java.util.Objects;
import lombok.Builder;

@Builder(toBuilder = true)
public record Shipping(Money shippingCost, LocalDate expectedDeliveryDate, Recipient recipient, Address address) {

  public Shipping {
    Objects.requireNonNull(shippingCost);
    Objects.requireNonNull(expectedDeliveryDate);
    Objects.requireNonNull(recipient);
    Objects.requireNonNull(address);
  }
}
