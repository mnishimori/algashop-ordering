package com.algaworks.algashop.ordering.domain.model.order;

import com.algaworks.algashop.ordering.domain.model.DomainService;
import com.algaworks.algashop.ordering.domain.model.commons.Money;
import com.algaworks.algashop.ordering.domain.model.customer.Customer;
import com.algaworks.algashop.ordering.domain.model.product.Product;
import com.algaworks.algashop.ordering.domain.model.commons.Quantity;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerId;
import java.util.Objects;
import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class BuyNowService {

  private final CustomerHaveFreeShippingSpecification customerHaveFreeShippingSpecification;

  public Order buyNow(Product product, Customer customer, Billing billing, Shipping shipping, Quantity quantity,
      PaymentMethod paymentMethod) {
    Objects.requireNonNull(product);
    Objects.requireNonNull(customer);
    Objects.requireNonNull(billing);
    Objects.requireNonNull(shipping);
    Objects.requireNonNull(quantity);
    Objects.requireNonNull(paymentMethod);

    product.changeOutStock();

    var order = Order.createDraftOrder(customer.id());
    order.changeBilling(billing);
    order.changeShipping(shipping);
    order.changePaymentMethod(paymentMethod);
    order.addOrderItem(product, quantity);
    if (haveFreeShipping(customer)) {
      var freeShipping = shipping.toBuilder().shippingCost(Money.ZERO).build();
      order.changeShipping(freeShipping);
    } else {
      order.changeShipping(shipping);
    }
    order.place();
    return order;
  }

  private boolean haveFreeShipping(Customer customer) {
    return customerHaveFreeShippingSpecification.isSatisfiedBy(customer);
  }
}
