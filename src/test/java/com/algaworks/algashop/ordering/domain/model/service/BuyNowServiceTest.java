package com.algaworks.algashop.ordering.domain.model.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.algaworks.algashop.ordering.domain.entity.CustomerTestDataBuilder;
import com.algaworks.algashop.ordering.domain.entity.ProductTestDataBuilder;
import com.algaworks.algashop.ordering.domain.model.order.BuyNowService;
import com.algaworks.algashop.ordering.domain.model.order.PaymentMethod;
import com.algaworks.algashop.ordering.domain.model.product.ProductOutOfStockException;
import com.algaworks.algashop.ordering.domain.model.commons.Address;
import com.algaworks.algashop.ordering.domain.model.order.Billing;
import com.algaworks.algashop.ordering.domain.model.commons.Document;
import com.algaworks.algashop.ordering.domain.model.commons.Email;
import com.algaworks.algashop.ordering.domain.model.commons.FullName;
import com.algaworks.algashop.ordering.domain.model.commons.Money;
import com.algaworks.algashop.ordering.domain.model.commons.Phone;
import com.algaworks.algashop.ordering.domain.model.product.ProductName;
import com.algaworks.algashop.ordering.domain.model.commons.Quantity;
import com.algaworks.algashop.ordering.domain.model.order.Recipient;
import com.algaworks.algashop.ordering.domain.model.order.Shipping;
import com.algaworks.algashop.ordering.domain.model.commons.ZipCode;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerId;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("BuyNowService")
class BuyNowServiceTest {

  private final BuyNowService buyNowService = new BuyNowService();

  @Test
  @DisplayName("Should buy a valid product and place the order")
  void shouldBuyValidProductAndPlaceOrder() {
    var customerId = aCustomerId();
    var product = ProductTestDataBuilder.createProduct(new ProductName("Notebook Pro"), new Money("1000.00"), true)
        .build();
    var quantity = new Quantity(new BigDecimal("2"));
    var billing = billingInfo();
    var shipping = shippingInfo(new Money("15.00"));
    var paymentMethod = PaymentMethod.CREDIT_CARD;

    var order = buyNowService.buyNow(product, customerId, billing, shipping, quantity, paymentMethod);

    assertThat(order).isNotNull();
    assertThat(order.customerId()).isEqualTo(customerId);
    assertThat(order.paymentMethod()).isEqualTo(paymentMethod);
    assertThat(order.billing()).isEqualTo(billing);
    assertThat(order.shipping()).isEqualTo(shipping);
    assertThat(order.isPlaced()).isTrue();
    assertThat(order.items()).hasSize(1);

    var addedItem = order.items().iterator().next();
    assertThat(addedItem).isNotNull();
    assertThat(addedItem.productId()).isEqualTo(product.id());
    assertThat(addedItem.productName()).isEqualTo(product.name());
    assertThat(addedItem.price()).isEqualTo(product.price());
    assertThat(addedItem.quantity()).isEqualTo(quantity);

    assertThat(order.totalItems()).isEqualTo(quantity);
    assertThat(order.totalAmount()).isEqualTo(product.price().multiply(quantity).add(shipping.shippingCost()));
  }

  @Test
  @DisplayName("Should throw ProductOutOfStockException when product is unavailable")
  void shouldThrowWhenProductIsOutOfStock() {
    var customerId = aCustomerId();
    var product = ProductTestDataBuilder.aProductUnavailable().build();

    assertThatThrownBy(() -> buyNowService.buyNow(product, customerId, billingInfo(), shippingInfo(new Money("15.00")),
        new Quantity(new BigDecimal("1")), PaymentMethod.CREDIT_CARD))
        .isInstanceOf(ProductOutOfStockException.class);
  }

  @Test
  @DisplayName("Should throw IllegalArgumentException when quantity is zero")
  void shouldThrowWhenQuantityIsZero() {
    var customerId = aCustomerId();
    var product = ProductTestDataBuilder.createProduct(new ProductName("Notebook Pro"), new Money("1000.00"), true)
        .build();

    assertThatThrownBy(() -> buyNowService.buyNow(product, customerId, billingInfo(), shippingInfo(new Money("15.00")),
        Quantity.ZERO, PaymentMethod.CREDIT_CARD))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("greater than zero");
  }

  private CustomerId aCustomerId() {
    return CustomerTestDataBuilder.existedCustomer().build().id();
  }

  private Billing billingInfo() {
    var address = Address.builder()
        .street(CustomerTestDataBuilder.BOURBON_STREET)
        .number(CustomerTestDataBuilder.NUMBER)
        .neighborhood(CustomerTestDataBuilder.NORTH_VALLEY)
        .city(CustomerTestDataBuilder.NEW_YORK)
        .state(CustomerTestDataBuilder.NEW_YORK)
        .zipCode(new ZipCode(CustomerTestDataBuilder.ZIP_CODE))
        .build();

    return Billing.builder()
        .fullName(new FullName(CustomerTestDataBuilder.FIRST_NAME, CustomerTestDataBuilder.LAST_NAME))
        .document(new Document(CustomerTestDataBuilder.DOCUMENT))
        .phone(new Phone(CustomerTestDataBuilder.PHONE))
        .address(address)
        .email(new Email(CustomerTestDataBuilder.EMAIL))
        .build();
  }

  private Shipping shippingInfo(Money shippingCost) {
    var address = Address.builder()
        .street(CustomerTestDataBuilder.BOURBON_STREET)
        .number(CustomerTestDataBuilder.NUMBER)
        .neighborhood(CustomerTestDataBuilder.NORTH_VALLEY)
        .city(CustomerTestDataBuilder.NEW_YORK)
        .state(CustomerTestDataBuilder.NEW_YORK)
        .zipCode(new ZipCode(CustomerTestDataBuilder.ZIP_CODE))
        .build();

    var recipient = Recipient.builder()
        .fullName(new FullName(CustomerTestDataBuilder.FIRST_NAME, CustomerTestDataBuilder.LAST_NAME))
        .document(new Document(CustomerTestDataBuilder.DOCUMENT))
        .phone(new Phone(CustomerTestDataBuilder.PHONE))
        .build();

    return Shipping.builder()
        .shippingCost(shippingCost)
        .expectedDeliveryDate(LocalDate.now().plusDays(5))
        .recipient(recipient)
        .address(address)
        .build();
  }
}
