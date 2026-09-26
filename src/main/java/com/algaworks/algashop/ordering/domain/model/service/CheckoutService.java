package com.algaworks.algashop.ordering.domain.model.service;

import static com.algaworks.algashop.ordering.domain.model.messages.ErrorMessages.SHOPPING_CART_CANT_PROCEED_TO_CHECKOUT;

import com.algaworks.algashop.ordering.domain.model.entity.Order;
import com.algaworks.algashop.ordering.domain.model.entity.PaymentMethod;
import com.algaworks.algashop.ordering.domain.model.entity.ShoppingCart;
import com.algaworks.algashop.ordering.domain.model.exception.ShoppingCartCantProceedToCheckoutException;
import com.algaworks.algashop.ordering.domain.model.utility.DomainService;
import com.algaworks.algashop.ordering.domain.model.valueobject.Billing;
import com.algaworks.algashop.ordering.domain.model.valueobject.Product;
import com.algaworks.algashop.ordering.domain.model.valueobject.Shipping;
import java.util.Objects;

@DomainService
public class CheckoutService {

  public Order checkout(ShoppingCart shoppingCart, Billing billing, Shipping shipping, PaymentMethod paymentMethod) {
    Objects.requireNonNull(shoppingCart);
    Objects.requireNonNull(billing);
    Objects.requireNonNull(shipping);
    Objects.requireNonNull(paymentMethod);

    if (shoppingCart.containsUnavailableItems()) {
      throw new ShoppingCartCantProceedToCheckoutException(SHOPPING_CART_CANT_PROCEED_TO_CHECKOUT);
    }

    var order = Order.createDraftOrder(shoppingCart.customerId());
    order.changeBilling(billing);
    order.changeShipping(shipping);
    order.changePaymentMethod(paymentMethod);
    shoppingCart.items().forEach(shoppingCartItem -> {
      var product = new Product(shoppingCartItem.product(), shoppingCartItem.productName(), shoppingCartItem.price(), shoppingCartItem.available());
      order.addOrderItem(product, shoppingCartItem.quantity());
    });
    order.place();
    shoppingCart.empty();

    return order;
  }

}
