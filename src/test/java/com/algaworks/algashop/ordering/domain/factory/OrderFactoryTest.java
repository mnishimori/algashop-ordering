package com.algaworks.algashop.ordering.domain.factory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.algaworks.algashop.ordering.domain.model.entity.Order;
import com.algaworks.algashop.ordering.domain.model.entity.PaymentMethod;
import com.algaworks.algashop.ordering.domain.model.factory.OrderFactory;
import com.algaworks.algashop.ordering.domain.model.valueobject.Address;
import com.algaworks.algashop.ordering.domain.model.valueobject.Billing;
import com.algaworks.algashop.ordering.domain.model.valueobject.Document;
import com.algaworks.algashop.ordering.domain.model.valueobject.Email;
import com.algaworks.algashop.ordering.domain.model.valueobject.FullName;
import com.algaworks.algashop.ordering.domain.model.valueobject.Money;
import com.algaworks.algashop.ordering.domain.model.valueobject.Phone;
import com.algaworks.algashop.ordering.domain.model.valueobject.Product;
import com.algaworks.algashop.ordering.domain.model.valueobject.ProductName;
import com.algaworks.algashop.ordering.domain.model.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.model.valueobject.Recipient;
import com.algaworks.algashop.ordering.domain.model.valueobject.Shipping;
import com.algaworks.algashop.ordering.domain.model.valueobject.ZipCode;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.ProductId;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderFactoryTest {

  @Test
  @DisplayName("Should create filled order with all valid parameters")
  void shouldCreateFilledOrderWithAllValidParameters() {
    CustomerId customerId = new CustomerId(UUID.randomUUID());
    Shipping shipping = createValidShipping();
    Billing billing = createValidBilling();
    PaymentMethod paymentMethod = PaymentMethod.CREDIT_CARD;
    Product product = createValidProduct();
    Quantity quantity = new Quantity(new BigDecimal("2"));

    Order order = OrderFactory.filledOrder(customerId, shipping, billing, paymentMethod, product, quantity);

    assertThat(order).isNotNull();
    assertThat(order.customerId()).isEqualTo(customerId);
    assertThat(order.shipping()).isEqualTo(shipping);
    assertThat(order.billing()).isEqualTo(billing);
    assertThat(order.paymentMethod()).isEqualTo(paymentMethod);
  }

  @Test
  @DisplayName("Should throw NullPointerException when customerId is null")
  void shouldThrowNullPointerExceptionWhenCustomerIdIsNull() {
    Shipping shipping = createValidShipping();
    Billing billing = createValidBilling();
    PaymentMethod paymentMethod = PaymentMethod.CREDIT_CARD;
    Product product = createValidProduct();
    Quantity quantity = new Quantity(new BigDecimal("2"));

    assertThatThrownBy(() -> OrderFactory.filledOrder(null, shipping, billing, paymentMethod, product, quantity))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should throw NullPointerException when shipping is null")
  void shouldThrowNullPointerExceptionWhenShippingIsNull() {
    CustomerId customerId = new CustomerId(UUID.randomUUID());
    Billing billing = createValidBilling();
    PaymentMethod paymentMethod = PaymentMethod.CREDIT_CARD;
    Product product = createValidProduct();
    Quantity quantity = new Quantity(new BigDecimal("2"));

    assertThatThrownBy(() -> OrderFactory.filledOrder(customerId, null, billing, paymentMethod, product, quantity))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should throw NullPointerException when billing is null")
  void shouldThrowNullPointerExceptionWhenBillingIsNull() {
    CustomerId customerId = new CustomerId(UUID.randomUUID());
    Shipping shipping = createValidShipping();
    PaymentMethod paymentMethod = PaymentMethod.CREDIT_CARD;
    Product product = createValidProduct();
    Quantity quantity = new Quantity(new BigDecimal("2"));

    assertThatThrownBy(() -> OrderFactory.filledOrder(customerId, shipping, null, paymentMethod, product, quantity))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should throw NullPointerException when paymentMethod is null")
  void shouldThrowNullPointerExceptionWhenPaymentMethodIsNull() {
    CustomerId customerId = new CustomerId(UUID.randomUUID());
    Shipping shipping = createValidShipping();
    Billing billing = createValidBilling();
    Product product = createValidProduct();
    Quantity quantity = new Quantity(new BigDecimal("2"));

    assertThatThrownBy(() -> OrderFactory.filledOrder(customerId, shipping, billing, null, product, quantity))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should throw NullPointerException when product is null")
  void shouldThrowNullPointerExceptionWhenProductIsNull() {
    CustomerId customerId = new CustomerId(UUID.randomUUID());
    Shipping shipping = createValidShipping();
    Billing billing = createValidBilling();
    PaymentMethod paymentMethod = PaymentMethod.CREDIT_CARD;
    Quantity quantity = new Quantity(new BigDecimal("2"));

    assertThatThrownBy(() -> OrderFactory.filledOrder(customerId, shipping, billing, paymentMethod, null, quantity))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should throw NullPointerException when quantity is null")
  void shouldThrowNullPointerExceptionWhenQuantityIsNull() {
    CustomerId customerId = new CustomerId(UUID.randomUUID());
    Shipping shipping = createValidShipping();
    Billing billing = createValidBilling();
    PaymentMethod paymentMethod = PaymentMethod.CREDIT_CARD;
    Product product = createValidProduct();

    assertThatThrownBy(() -> OrderFactory.filledOrder(customerId, shipping, billing, paymentMethod, product, null))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should throw NullPointerException when all parameters are null")
  void shouldThrowNullPointerExceptionWhenAllParametersAreNull() {
    assertThatThrownBy(() -> OrderFactory.filledOrder(null, null, null, null, null, null))
        .isInstanceOf(NullPointerException.class);
  }

  private Shipping createValidShipping() {
    var fullName = new FullName("John", "Doe");
    var document = new Document("12345678900");
    var phone = new Phone("11999999999");
    var zipCode = new ZipCode("12345-678");
    var address = new Address("Main Street", "123", "Apt 1",
        "Downtown", "New York", "NY", zipCode);
    var recepient = new Recipient(fullName, document, phone);
    return Shipping.builder()
        .shippingCost(new Money("25.00"))
        .expectedDeliveryDate(LocalDate.now().plusDays(10))
        .recipient(recepient)
        .address(address)
        .build();
  }

  private Billing createValidBilling() {
    var fullName = new FullName("John", "Doe");
    var document = new Document("12345678900");
    var phone = new Phone("11999999999");
    var zipCode = new ZipCode("12345-678");
    var address = new Address("Main Street", "123", "Apt 1",
        "Downtown", "New York", "NY", zipCode);
    var email = new Email("john.doe@example.com");
    return Billing.builder()
        .fullName(fullName)
        .document(document)
        .phone(phone)
        .address(address)
        .email(email)
        .build();
  }

  private Product createValidProduct() {
    return Product.builder()
        .id(new ProductId())
        .name(new ProductName("Notebook Pro"))
        .price(new Money("100.00"))
        .inStock(true)
        .build();
  }
}
