package com.algaworks.algashop.ordering.application.checkout;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BuyNowInput {
  private ShippingInput shipping;
  private BillingData billing;
  private UUID customerId;
  private UUID productId;
  private Integer quantity;
  private String paymentMethod;
}
