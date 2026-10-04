package com.algaworks.algashop.ordering.domain.model.order;

import com.algaworks.algashop.ordering.domain.model.product.Product;
import com.algaworks.algashop.ordering.domain.model.commons.Quantity;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerId;
import java.util.Objects;

public class OrderFactory {

  private OrderFactory() {
  }

  public static Order filledOrder(CustomerId customerId, Shipping shipping, Billing billing,
      PaymentMethod paymentMethod, Product product, Quantity quantity) {
    Objects.requireNonNull(customerId);
    Objects.requireNonNull(shipping);
    Objects.requireNonNull(billing);
    Objects.requireNonNull(paymentMethod);
    Objects.requireNonNull(product);
    Objects.requireNonNull(quantity);
    var order = Order.createDraftOrder(customerId);
    order.changeShipping(shipping);
    order.changeBilling(billing);
    order.changePaymentMethod(paymentMethod);
    order.addOrderItem(product, quantity);
    return order;
  }
}
