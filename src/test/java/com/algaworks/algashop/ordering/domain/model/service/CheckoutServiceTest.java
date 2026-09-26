package com.algaworks.algashop.ordering.domain.model.service;

import static com.algaworks.algashop.ordering.domain.model.messages.ErrorMessages.SHOPPING_CART_CANT_PROCEED_TO_CHECKOUT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.algaworks.algashop.ordering.domain.entity.CustomerTestDataBuilder;
import com.algaworks.algashop.ordering.domain.entity.ProductTestDataBuilder;
import com.algaworks.algashop.ordering.domain.entity.ShoppingCartTestDataBuilder;
import com.algaworks.algashop.ordering.domain.model.entity.OrderItem;
import com.algaworks.algashop.ordering.domain.model.entity.PaymentMethod;
import com.algaworks.algashop.ordering.domain.model.entity.ShoppingCart;
import com.algaworks.algashop.ordering.domain.model.exception.OrderCannotBePlacedException;
import com.algaworks.algashop.ordering.domain.model.exception.ShoppingCartCantProceedToCheckoutException;
import com.algaworks.algashop.ordering.domain.model.valueobject.Address;
import com.algaworks.algashop.ordering.domain.model.valueobject.Billing;
import com.algaworks.algashop.ordering.domain.model.valueobject.Document;
import com.algaworks.algashop.ordering.domain.model.valueobject.Email;
import com.algaworks.algashop.ordering.domain.model.valueobject.FullName;
import com.algaworks.algashop.ordering.domain.model.valueobject.Money;
import com.algaworks.algashop.ordering.domain.model.valueobject.Phone;
import com.algaworks.algashop.ordering.domain.model.valueobject.ProductName;
import com.algaworks.algashop.ordering.domain.model.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.model.valueobject.Recipient;
import com.algaworks.algashop.ordering.domain.model.valueobject.Shipping;
import com.algaworks.algashop.ordering.domain.model.valueobject.ZipCode;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CheckoutServiceTest {

  private final CheckoutService checkoutService = new CheckoutService();

  @Test
  @DisplayName("Should checkout a valid shopping cart and empty it afterwards")
  void shouldCheckoutValidShoppingCart() {
    var shoppingCart = shoppingCartWithAvailableItems();
    var billing = billingInfo();
    var shipping = shippingInfo();
    var paymentMethod = PaymentMethod.CREDIT_CARD;

    var order = checkoutService.checkout(shoppingCart, billing, shipping, paymentMethod);

    assertThat(order).isNotNull();
    assertThat(order.customerId()).isEqualTo(shoppingCart.customerId());
    assertThat(order.paymentMethod()).isEqualTo(paymentMethod);
    assertThat(order.billing()).isEqualTo(billing);
    assertThat(order.shipping()).isEqualTo(shipping);
    assertThat(order.isPlaced()).isTrue();
    assertThat(order.items()).hasSize(2);
    assertThat(order.totalItems()).isEqualTo(new Quantity(BigDecimal.valueOf(3)));
    assertThat(order.totalAmount()).isEqualTo(new Money("95.00"));
    assertThat(new ArrayList<>(order.items()))
        .extracting(OrderItem::productName)
        .containsExactlyInAnyOrder(new ProductName("Keyboard"), new ProductName("Mouse"));
    assertThat(order.items())
        .extracting(OrderItem::quantity)
        .containsExactlyInAnyOrder(new Quantity(BigDecimal.ONE), new Quantity(BigDecimal.valueOf(2)));
    assertThat(order.items())
        .extracting(OrderItem::price)
        .containsExactlyInAnyOrder(new Money("20.00"), new Money("30.00"));
    assertThat(shoppingCart.isEmpty()).isTrue();
  }

  @Test
  @DisplayName("Should not checkout shopping cart with unavailable items")
  void shouldNotCheckoutShoppingCartWithUnavailableItems() {
    var shoppingCart = shoppingCartWithUnavailableItem();
    var originalItems = new ArrayList<>(shoppingCart.items());

    assertThatThrownBy(
        () -> checkoutService.checkout(shoppingCart, billingInfo(), shippingInfo(), PaymentMethod.CREDIT_CARD))
        .isInstanceOf(ShoppingCartCantProceedToCheckoutException.class)
        .hasMessage(SHOPPING_CART_CANT_PROCEED_TO_CHECKOUT);

    assertThat(new ArrayList<>(shoppingCart.items())).containsExactlyInAnyOrderElementsOf(originalItems);
    assertThat(shoppingCart.isEmpty()).isFalse();
  }

  @Test
  @DisplayName("Should not checkout empty shopping cart")
  void shouldNotCheckoutEmptyShoppingCart() {
    var shoppingCart = emptyShoppingCart();

    assertThatThrownBy(
        () -> checkoutService.checkout(shoppingCart, billingInfo(), shippingInfo(), PaymentMethod.CREDIT_CARD))
        .isInstanceOf(OrderCannotBePlacedException.class)
        .hasMessageContaining("Order").hasMessageContaining("cannot be placed, it has no items");

    assertThat(shoppingCart.isEmpty()).isTrue();
    assertThat(shoppingCart.items()).isEmpty();
  }

  private ShoppingCart shoppingCartWithAvailableItems() {
    var product1 = ProductTestDataBuilder.createProduct(new ProductName("Keyboard"), new Money("20.00"), true).build();
    var product2 = ProductTestDataBuilder.createProduct(new ProductName("Mouse"), new Money("30.00"), true).build();

    var shoppingCart = ShoppingCartTestDataBuilder.aShoppingCart()
        .customerId(CustomerTestDataBuilder.existedCustomer().build().id())
        .build();

    shoppingCart.addItem(product1, new Quantity(BigDecimal.ONE));
    shoppingCart.addItem(product2, new Quantity(BigDecimal.valueOf(2)));

    return shoppingCart;
  }

  private ShoppingCart shoppingCartWithUnavailableItem() {
    var productBuilder = ProductTestDataBuilder.createProduct(new ProductName("Monitor"), new Money("1000.00"), true);
    var product = productBuilder.build();
    var shoppingCart = ShoppingCartTestDataBuilder.aShoppingCart()
        .customerId(CustomerTestDataBuilder.existedCustomer().build().id())
        .build();

    shoppingCart.addItem(product, new Quantity(BigDecimal.ONE));
    shoppingCart.refreshItem(productBuilder.inStock(false).build());

    return shoppingCart;
  }

  private ShoppingCart emptyShoppingCart() {
    return ShoppingCartTestDataBuilder.aShoppingCart()
        .customerId(CustomerTestDataBuilder.existedCustomer().build().id())
        .build();
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

  private Shipping shippingInfo() {
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
        .shippingCost(new Money("15.00"))
        .expectedDeliveryDate(LocalDate.now().plusDays(5))
        .recipient(recipient)
        .address(address)
        .build();
  }
}