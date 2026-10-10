package com.algaworks.algashop.ordering.application.checkout;

import com.algaworks.algashop.ordering.domain.model.commons.Quantity;
import com.algaworks.algashop.ordering.domain.model.commons.ZipCode;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerId;
import com.algaworks.algashop.ordering.domain.model.order.BuyNowService;
import com.algaworks.algashop.ordering.domain.model.order.Orders;
import com.algaworks.algashop.ordering.domain.model.order.PaymentMethod;
import com.algaworks.algashop.ordering.domain.model.order.shipping.OriginAddressService;
import com.algaworks.algashop.ordering.domain.model.order.shipping.ShippingCostService;
import com.algaworks.algashop.ordering.domain.model.order.shipping.ShippingCostService.CalculationResult;
import com.algaworks.algashop.ordering.domain.model.product.ProductCatalogService;
import com.algaworks.algashop.ordering.domain.model.product.ProductId;
import java.math.BigDecimal;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BuyNowApplicationService {

  private final BuyNowService buyNowService;
  private final ProductCatalogService productCatalogService;
  private final ShippingCostService shippingCostService;
  private final OriginAddressService originAddressService;
  private final Orders orders;
  private final ShippingInputDisassembler shippingInputDisassembler;
  private final BillingInputDisassembler billingInputDisassembler;

  @Transactional
  public String buyNow(BuyNowInput input) {
    Objects.requireNonNull(input);

    var customerId = new CustomerId(input.getCustomerId());
    var productId = new ProductId(input.getProductId());
    var quantity = new Quantity(BigDecimal.valueOf(input.getQuantity()));
    var product = productCatalogService.ofId(productId).orElseThrow();
    var shippingCalculationResult = calculateShippingCost(input.getShipping());
    var shipping = shippingInputDisassembler.toDomainModel(input.getShipping(), shippingCalculationResult);
    var billing = billingInputDisassembler.toDomainModel(input.getBilling());
    var paymentMethod = PaymentMethod.valueOf(input.getPaymentMethod());

    var order = buyNowService.buyNow(product, customerId, billing, shipping, quantity, paymentMethod);
    return order.id().toString();
  }

  private CalculationResult calculateShippingCost(ShippingInput shipping) {
    var originZipCode = originAddressService.originAddress().zipCode();
    var destinationZipCode = new ZipCode(shipping.getAddress().getZipCode());
    return shippingCostService.calculate(new ShippingCostService.CalculationRequest(originZipCode, destinationZipCode));
  }
}
