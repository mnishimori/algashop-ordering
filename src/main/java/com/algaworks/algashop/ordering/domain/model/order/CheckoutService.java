package com.algaworks.algashop.ordering.domain.model.order;

import static com.algaworks.algashop.ordering.domain.model.ErrorMessages.SHOPPING_CART_CANT_PROCEED_TO_CHECKOUT;

import com.algaworks.algashop.ordering.domain.model.shoppingcart.ShoppingCart;
import com.algaworks.algashop.ordering.domain.model.shoppingcart.ShoppingCartCantProceedToCheckoutException;
import com.algaworks.algashop.ordering.domain.model.DomainService;
import com.algaworks.algashop.ordering.domain.model.product.Product;
import java.util.Objects;

@DomainService
public class CheckoutService {

  public Order checkout(ShoppingCart shoppingCart, Billing billing, Shipping shipping, PaymentMethod paymentMethod) {
    validateRequiredFields(shoppingCart, billing, shipping, paymentMethod);
    validateRequiredItems(shoppingCart);

    var order = Order.createDraftOrder(shoppingCart.customerId());
    order.changeBilling(billing);
    order.changeShipping(shipping);
    order.changePaymentMethod(paymentMethod);
    shoppingCart.items().forEach(shoppingCartItem -> {
      var product = new Product(shoppingCartItem.product(), shoppingCartItem.productName(), shoppingCartItem.price(),
          shoppingCartItem.available());
      order.addOrderItem(product, shoppingCartItem.quantity());
    });
    order.place();
    shoppingCart.empty();

    return order;
  }

  private void validateRequiredItems(ShoppingCart shoppingCart) {
    if (shoppingCart.containsUnavailableItems()) {
      throw new ShoppingCartCantProceedToCheckoutException(SHOPPING_CART_CANT_PROCEED_TO_CHECKOUT);
    }
  }

  private void validateRequiredFields(ShoppingCart shoppingCart, Billing billing, Shipping shipping,
      PaymentMethod paymentMethod) {
    Objects.requireNonNull(shoppingCart);
    Objects.requireNonNull(billing);
    Objects.requireNonNull(shipping);
    Objects.requireNonNull(paymentMethod);
  }

}
