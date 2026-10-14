package com.algaworks.algashop.ordering.application.checkout;

import com.algaworks.algashop.ordering.domain.model.commons.ZipCode;
import com.algaworks.algashop.ordering.domain.model.order.CheckoutService;
import com.algaworks.algashop.ordering.domain.model.order.Orders;
import com.algaworks.algashop.ordering.domain.model.order.PaymentMethod;
import com.algaworks.algashop.ordering.domain.model.order.shipping.OriginAddressService;
import com.algaworks.algashop.ordering.domain.model.order.shipping.ShippingCostService;
import com.algaworks.algashop.ordering.domain.model.shoppingcart.ShoppingCartId;
import com.algaworks.algashop.ordering.domain.model.shoppingcart.ShoppingCartNotFoundException;
import com.algaworks.algashop.ordering.domain.model.shoppingcart.ShoppingCarts;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CheckoutApplicationService {

  private final ShoppingCarts shoppingCarts;
  private final ShippingCostService shippingCostService;
  private final OriginAddressService originAddressService;
  private final CheckoutService checkoutService;
  private final BillingInputDisassembler billingInputDisassembler;
  private final ShippingInputDisassembler shippingInputDisassembler;
  private final Orders orders;

  @Transactional
  public String checkout(CheckoutInput checkoutInput) {
    Objects.requireNonNull(checkoutInput);
    var shoppingCartId = new ShoppingCartId(checkoutInput.getShoppingCartId());
    var shoppingCart = shoppingCarts.findById(shoppingCartId)
        .orElseThrow(() -> new ShoppingCartNotFoundException(shoppingCartId));
    var paymentMethod = PaymentMethod.valueOf(checkoutInput.getPaymentMethod());
    var originZipCodeAddress = originAddressService.originAddress().zipCode();
    var destinationZipCodeAddress = new ZipCode(checkoutInput.getShipping().getAddress().getZipCode());
    var calculationRequest = ShippingCostService.CalculationRequest.builder()
        .origin(originZipCodeAddress)
        .destination(destinationZipCodeAddress)
        .build();
    var calculationResult = shippingCostService.calculate(calculationRequest);
    var billingData = billingInputDisassembler.toDomainModel(checkoutInput.getBilling());
    var shippingData = shippingInputDisassembler.toDomainModel(checkoutInput.getShipping(), calculationResult);
    var order = checkoutService.checkout(shoppingCart, billingData, shippingData, paymentMethod);
    orders.add(order);
    shoppingCarts.add(shoppingCart);
    return order.id().toString();
  }

}
