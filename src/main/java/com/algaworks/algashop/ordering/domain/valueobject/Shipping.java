package com.algaworks.algashop.ordering.domain.valueobject;

import java.time.LocalDate;
import java.util.Objects;
import lombok.Builder;

@Builder(toBuilder = true)
public record Shipping(Money shippingCost, LocalDate expectedDeliveryDate, Recepient recepient, Address address) {

  public Shipping {
    Objects.requireNonNull(shippingCost);
    Objects.requireNonNull(expectedDeliveryDate);
    Objects.requireNonNull(recepient);
    Objects.requireNonNull(address);
  }
}
