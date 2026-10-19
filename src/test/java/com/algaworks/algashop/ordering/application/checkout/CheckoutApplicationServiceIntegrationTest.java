package com.algaworks.algashop.ordering.application.checkout;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.algaworks.algashop.ordering.IntegrationTest;
import com.algaworks.algashop.ordering.application.commons.AddressData;
import com.algaworks.algashop.ordering.domain.entity.CustomerTestDataBuilder;
import com.algaworks.algashop.ordering.domain.entity.ProductTestDataBuilder;
import com.algaworks.algashop.ordering.domain.entity.ShoppingCartTestDataBuilder;
import com.algaworks.algashop.ordering.domain.model.commons.Address;
import com.algaworks.algashop.ordering.domain.model.commons.Money;
import com.algaworks.algashop.ordering.domain.model.commons.Quantity;
import com.algaworks.algashop.ordering.domain.model.commons.ZipCode;
import com.algaworks.algashop.ordering.domain.model.order.Order;
import com.algaworks.algashop.ordering.domain.model.order.OrderCannotBePlacedException;
import com.algaworks.algashop.ordering.domain.model.order.OrderPlacedEvent;
import com.algaworks.algashop.ordering.domain.model.order.Orders;
import com.algaworks.algashop.ordering.domain.model.order.shipping.OriginAddressService;
import com.algaworks.algashop.ordering.domain.model.order.shipping.ShippingCostService;
import com.algaworks.algashop.ordering.domain.model.order.shipping.ShippingCostService.CalculationResult;
import com.algaworks.algashop.ordering.domain.model.product.ProductName;
import com.algaworks.algashop.ordering.domain.model.shoppingcart.ShoppingCart;
import com.algaworks.algashop.ordering.domain.model.shoppingcart.ShoppingCartCantProceedToCheckoutException;
import com.algaworks.algashop.ordering.domain.model.shoppingcart.ShoppingCartId;
import com.algaworks.algashop.ordering.domain.model.shoppingcart.ShoppingCartNotFoundException;
import com.algaworks.algashop.ordering.domain.model.shoppingcart.ShoppingCarts;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@IntegrationTest
class CheckoutApplicationServiceIntegrationTest {

  @Autowired
  private CheckoutApplicationService checkoutApplicationService;

  @MockitoBean
  private ShoppingCarts shoppingCarts;

  @MockitoBean
  private Orders orders;

  @MockitoBean
  private ShippingCostService shippingCostService;

  @MockitoBean
  private OriginAddressService originAddressService;

  @Test
  void shouldCheckoutShoppingCartSuccessfully() {
    var shoppingCart = shoppingCartWithAvailableItems();
    var input = aCheckoutInput(shoppingCart.id().value());
    when(shoppingCarts.findById(shoppingCart.id())).thenReturn(Optional.of(shoppingCart));
    when(originAddressService.originAddress()).thenReturn(anOriginAddress());
    when(shippingCostService.calculate(any())).thenReturn(
        new CalculationResult(new Money("15.00"), LocalDate.now().plusDays(5)));

    var orderId = checkoutApplicationService.checkout(input);

    assertThat(orderId).isNotBlank();
    var orderCaptor = ArgumentCaptor.forClass(Order.class);
    verify(orders).add(orderCaptor.capture());
    var order = orderCaptor.getValue();
    assertThat(order.id().toString()).isEqualTo(orderId);
    assertThat(order.customerId()).isEqualTo(shoppingCart.customerId());
    assertThat(order.isPlaced()).isTrue();
    assertThat(order.paymentMethod().name()).isEqualTo("CREDIT_CARD");
    assertThat(order.items()).hasSize(2);
    assertThat(order.totalItems()).isEqualTo(new Quantity(BigDecimal.valueOf(3)));
    assertThat(order.totalAmount()).isEqualTo(new Money("95.00"));
    assertThat(order.shipping().shippingCost()).isEqualTo(new Money("15.00"));
    assertThat(order.billing().fullName().firstName()).isEqualTo(CustomerTestDataBuilder.FIRST_NAME);
    assertThat(order.shipping().address().zipCode()).isEqualTo(new ZipCode(CustomerTestDataBuilder.ZIP_CODE));
    assertThat(order.domainEvents())
        .singleElement()
        .isInstanceOf(OrderPlacedEvent.class)
        .satisfies(event -> {
          var placedEvent = (OrderPlacedEvent) event;
          assertThat(placedEvent.orderId()).isEqualTo(order.id());
          assertThat(placedEvent.customerId()).isEqualTo(order.customerId());
          assertThat(placedEvent.placedAt()).isNotNull();
        });
    verify(shoppingCarts).add(shoppingCart);
    assertThat(shoppingCart.isEmpty()).isTrue();
    verify(shippingCostService).calculate(any());
  }

  @Test
  void shouldFailToCheckoutWhenShoppingCartDoesNotExist() {
    var input = aCheckoutInput(UUID.randomUUID());
    when(shoppingCarts.findById(any(ShoppingCartId.class))).thenReturn(Optional.empty());

    assertThatThrownBy(() -> checkoutApplicationService.checkout(input))
        .isInstanceOf(ShoppingCartNotFoundException.class);

    verify(orders, never()).add(any());
    verify(shoppingCarts, never()).add(any(ShoppingCart.class));
    verify(shippingCostService, never()).calculate(any());
  }

  @Test
  void shouldFailToCheckoutWhenShoppingCartHasUnavailableItems() {
    var shoppingCart = shoppingCartWithUnavailableItem();
    var input = aCheckoutInput(shoppingCart.id().value());
    when(shoppingCarts.findById(shoppingCart.id())).thenReturn(Optional.of(shoppingCart));
    when(originAddressService.originAddress()).thenReturn(anOriginAddress());
    when(shippingCostService.calculate(any())).thenReturn(
        new CalculationResult(new Money("15.00"), LocalDate.now().plusDays(5)));

    assertThatThrownBy(() -> checkoutApplicationService.checkout(input))
        .isInstanceOf(ShoppingCartCantProceedToCheckoutException.class);

    verify(orders, never()).add(any());
    verify(shoppingCarts, never()).add(any(ShoppingCart.class));
    assertThat(shoppingCart.isEmpty()).isFalse();
  }

  @Test
  void shouldFailToCheckoutWhenShoppingCartIsEmpty() {
    var shoppingCart = ShoppingCartTestDataBuilder.aShoppingCart()
        .customerId(CustomerTestDataBuilder.existedCustomer().build().id())
        .build();
    var input = aCheckoutInput(shoppingCart.id().value());
    when(shoppingCarts.findById(shoppingCart.id())).thenReturn(Optional.of(shoppingCart));
    when(originAddressService.originAddress()).thenReturn(anOriginAddress());
    when(shippingCostService.calculate(any())).thenReturn(
        new CalculationResult(new Money("15.00"), LocalDate.now().plusDays(5)));

    assertThatThrownBy(() -> checkoutApplicationService.checkout(input))
        .isInstanceOf(OrderCannotBePlacedException.class);

    verify(orders, never()).add(any());
    verify(shoppingCarts, never()).add(any(ShoppingCart.class));
  }

  @Test
  void shouldFailToCheckoutWhenPaymentMethodIsInvalid() {
    var shoppingCart = shoppingCartWithAvailableItems();
    var input = aCheckoutInput(shoppingCart.id().value());
    input.setPaymentMethod("BITCOIN");
    when(shoppingCarts.findById(shoppingCart.id())).thenReturn(Optional.of(shoppingCart));

    assertThatThrownBy(() -> checkoutApplicationService.checkout(input))
        .isInstanceOf(IllegalArgumentException.class);

    verify(orders, never()).add(any());
    verify(shoppingCarts, never()).add(any(ShoppingCart.class));
    assertThat(shoppingCart.isEmpty()).isFalse();
  }

  @Test
  void shouldFailToCheckoutWhenInputIsNull() {
    assertThatThrownBy(() -> checkoutApplicationService.checkout(null))
        .isInstanceOf(NullPointerException.class);

    verify(orders, never()).add(any());
    verify(shoppingCarts, never()).add(any(ShoppingCart.class));
  }

  private static ShoppingCart shoppingCartWithAvailableItems() {
    var keyboard = ProductTestDataBuilder.createProduct(new ProductName("Keyboard"), new Money("20.00"), true).build();
    var mouse = ProductTestDataBuilder.createProduct(new ProductName("Mouse"), new Money("30.00"), true).build();
    var shoppingCart = ShoppingCartTestDataBuilder.aShoppingCart()
        .customerId(CustomerTestDataBuilder.existedCustomer().build().id())
        .build();
    shoppingCart.addItem(keyboard, new Quantity(BigDecimal.ONE));
    shoppingCart.addItem(mouse, new Quantity(BigDecimal.valueOf(2)));
    return shoppingCart;
  }

  private static ShoppingCart shoppingCartWithUnavailableItem() {
    var productBuilder = ProductTestDataBuilder.createProduct(new ProductName("Monitor"), new Money("1000.00"), true);
    var shoppingCart = ShoppingCartTestDataBuilder.aShoppingCart()
        .customerId(CustomerTestDataBuilder.existedCustomer().build().id())
        .build();
    shoppingCart.addItem(productBuilder.build(), new Quantity(BigDecimal.ONE));
    shoppingCart.refreshItem(productBuilder.inStock(false).build());
    return shoppingCart;
  }

  private static CheckoutInput aCheckoutInput(UUID shoppingCartId) {
    return CheckoutInput.builder()
        .shoppingCartId(shoppingCartId)
        .paymentMethod("CREDIT_CARD")
        .billing(BillingData.builder()
            .firstName(CustomerTestDataBuilder.FIRST_NAME)
            .lastName(CustomerTestDataBuilder.LAST_NAME)
            .document(CustomerTestDataBuilder.DOCUMENT)
            .email(CustomerTestDataBuilder.EMAIL)
            .phone(CustomerTestDataBuilder.PHONE)
            .address(anAddressData())
            .build())
        .shipping(ShippingInput.builder()
            .recipient(RecipientData.builder()
                .firstName(CustomerTestDataBuilder.FIRST_NAME)
                .lastName(CustomerTestDataBuilder.LAST_NAME)
                .document(CustomerTestDataBuilder.DOCUMENT)
                .phone(CustomerTestDataBuilder.PHONE)
                .build())
            .address(anAddressData())
            .build())
        .build();
  }

  private static AddressData anAddressData() {
    return AddressData.builder()
        .street(CustomerTestDataBuilder.BOURBON_STREET)
        .number(CustomerTestDataBuilder.NUMBER)
        .neighbourhood(CustomerTestDataBuilder.NORTH_VALLEY)
        .city(CustomerTestDataBuilder.NEW_YORK)
        .state(CustomerTestDataBuilder.NEW_YORK)
        .zipCode(CustomerTestDataBuilder.ZIP_CODE)
        .build();
  }

  private static Address anOriginAddress() {
    return Address.builder()
        .street("Rua de Origem")
        .number("100")
        .neighborhood("Centro")
        .city("São Paulo")
        .state("SP")
        .zipCode(new ZipCode("01000-000"))
        .build();
  }
}
