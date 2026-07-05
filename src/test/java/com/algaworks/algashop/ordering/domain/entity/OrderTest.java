package com.algaworks.algashop.ordering.domain.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.algaworks.algashop.ordering.domain.model.exception.OrderCannotBeCanceledException;
import com.algaworks.algashop.ordering.domain.model.exception.OrderCannotBePlacedException;
import com.algaworks.algashop.ordering.domain.model.exception.OrderInvalidShippingDeliveryDateException;
import com.algaworks.algashop.ordering.domain.model.entity.Order;
import com.algaworks.algashop.ordering.domain.model.entity.OrderItem;
import com.algaworks.algashop.ordering.domain.model.entity.OrderStatus;
import com.algaworks.algashop.ordering.domain.model.entity.PaymentMethod;
import com.algaworks.algashop.ordering.domain.model.exception.OrderCannotBeEditedException;
import com.algaworks.algashop.ordering.domain.model.exception.OrderCannotBeReadyException;
import com.algaworks.algashop.ordering.domain.model.exception.OrderItemNotFoundException;
import com.algaworks.algashop.ordering.domain.model.valueobject.Address;
import com.algaworks.algashop.ordering.domain.model.valueobject.Billing;
import com.algaworks.algashop.ordering.domain.model.valueobject.Document;
import com.algaworks.algashop.ordering.domain.model.valueobject.Email;
import com.algaworks.algashop.ordering.domain.model.valueobject.FullName;
import com.algaworks.algashop.ordering.domain.model.valueobject.Money;
import com.algaworks.algashop.ordering.domain.model.valueobject.Phone;
import com.algaworks.algashop.ordering.domain.model.valueobject.ProductName;
import com.algaworks.algashop.ordering.domain.model.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.model.valueobject.Recepient;
import com.algaworks.algashop.ordering.domain.model.valueobject.Shipping;
import com.algaworks.algashop.ordering.domain.model.valueobject.ZipCode;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderItemId;
import io.hypersistence.tsid.TSID;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

  @Test
  @DisplayName("Should create order with all parameters")
  void shouldCreateOrderWithAllParameters() {
    OrderId orderId = new OrderId(TSID.from(123456789L));
    CustomerId customerId = new CustomerId(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"));
    Money totalAmount = new Money("100.00");
    Quantity totalItems = new Quantity(new BigDecimal(5));
    OffsetDateTime placedAt = OffsetDateTime.now();
    OffsetDateTime paidAt = OffsetDateTime.now();
    OffsetDateTime canceledAt = null;
    OffsetDateTime readyAt = null;
    OrderStatus status = OrderStatus.PLACED;
    PaymentMethod paymentMethod = PaymentMethod.CREDIT_CARD;
    Set<OrderItem> items = new HashSet<>();

    Order order = new Order(orderId, 0L, customerId, totalAmount, totalItems, placedAt, paidAt, canceledAt,
        readyAt, null, null, status, paymentMethod, items);

    assertThat(order.id()).isEqualTo(orderId);
    assertThat(order.customerId()).isEqualTo(customerId);
    assertThat(order.totalAmount()).isEqualTo(totalAmount);
    assertThat(order.totalItems()).isEqualTo(totalItems);
    assertThat(order.placedAt()).isEqualTo(placedAt);
    assertThat(order.paidAt()).isEqualTo(paidAt);
    assertThat(order.canceledAt()).isNull();
    assertThat(order.readyAt()).isNull();
    assertThat(order.status()).isEqualTo(status);
    assertThat(order.paymentMethod()).isEqualTo(paymentMethod);
    assertThat(order.items()).isEqualTo(items);
  }

  @Test
  @DisplayName("Should create order with test data builder")
  void shouldCreateOrderWithTestDataBuilder() {
    Order order = OrderTestDataBuilder.anOrder().build();

    assertThat(order).isNotNull();
    assertThat(order.id()).isNotNull();
    assertThat(order.customerId()).isNotNull();
    assertThat(order.totalAmount()).isNotNull();
    assertThat(order.totalItems()).isNotNull();
    assertThat(order.status()).isEqualTo(OrderStatus.DRAFT);
  }

  @Test
  @DisplayName("Should create order with no-arg OrderId constructor")
  void shouldCreateOrderWithNoArgOrderIdConstructor() {
    OrderId orderId = new OrderId();
    CustomerId customerId = new CustomerId(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"));
    Money totalAmount = Money.ZERO;
    Quantity totalItems = Quantity.ZERO;
    Set<OrderItem> items = new HashSet<>();

    Order order = new Order(orderId, 0L, customerId, totalAmount, totalItems, null, null, null, null, null, null,
        OrderStatus.DRAFT, null, items);

    assertThat(order.id()).isNotNull();
    assertThat(order.id().value()).isNotNull();
  }

  @Test
  @DisplayName("Should throw exception when id is null")
  void shouldThrowExceptionWhenIdIsNull() {
    assertThatThrownBy(() -> OrderTestDataBuilder.anOrder().id(null).build())
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should throw exception when customerId is null")
  void shouldThrowExceptionWhenCustomerIdIsNull() {
    assertThatThrownBy(() -> OrderTestDataBuilder.anOrder().customerId(null).build())
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should throw exception when totalAmount is null")
  void shouldThrowExceptionWhenTotalAmountIsNull() {
    assertThatThrownBy(() -> OrderTestDataBuilder.anOrder().totalAmount(null).build())
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should throw exception when totalItems is null")
  void shouldThrowExceptionWhenTotalItemsIsNull() {
    assertThatThrownBy(() -> OrderTestDataBuilder.anOrder().totalItems(null).build())
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should throw exception when status is null")
  void shouldThrowExceptionWhenStatusIsNull() {
    assertThatThrownBy(() -> OrderTestDataBuilder.anOrder().status(null).build())
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should throw exception when items is null")
  void shouldThrowExceptionWhenItemsIsNull() {
    assertThatThrownBy(() -> OrderTestDataBuilder.anOrder().items(null).build())
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should allow null placedAt")
  void shouldAllowNullPlacedAt() {
    Order order = OrderTestDataBuilder.anOrder().placedAt(null).build();

    assertThat(order.placedAt()).isNull();
  }

  @Test
  @DisplayName("Should allow null paidAt")
  void shouldAllowNullPaidAt() {
    Order order = OrderTestDataBuilder.anOrder().paidAt(null).build();

    assertThat(order.paidAt()).isNull();
  }

  @Test
  @DisplayName("Should allow null canceledAt")
  void shouldAllowNullCanceledAt() {
    Order order = OrderTestDataBuilder.anOrder().canceledAt(null).build();

    assertThat(order.canceledAt()).isNull();
  }

  @Test
  @DisplayName("Should allow null readyAt")
  void shouldAllowNullReadyAt() {
    Order order = OrderTestDataBuilder.anOrder().readyAt(null).build();

    assertThat(order.readyAt()).isNull();
  }

  @Test
  @DisplayName("Should allow null billingInfo")
  void shouldAllowNullBillingInfo() {
    Order order = OrderTestDataBuilder.anOrder().billingInfo(null).build();

    assertThat(order.billingInfo()).isNull();
  }

  @Test
  @DisplayName("Should allow null shippingInfo")
  void shouldAllowNullShippingInfo() {
    Order order = OrderTestDataBuilder.anOrder().shippingInfo(null).build();

    assertThat(order.shippingInfo()).isNull();
  }

  @Test
  @DisplayName("Should allow null paymentMethod")
  void shouldAllowNullPaymentMethod() {
    Order order = OrderTestDataBuilder.anOrder().paymentMethod(null).build();

    assertThat(order.paymentMethod()).isNull();
  }

  @Test
  @DisplayName("Should be equal when same id")
  void shouldBeEqualWhenSameId() {
    OrderId orderId = new OrderId(TSID.from(123456789L));
    Order order1 = OrderTestDataBuilder.anOrder().id(orderId).build();
    Order order2 = OrderTestDataBuilder.anOrder().id(orderId).build();

    assertThat(order1).isEqualTo(order2);
    assertThat(order1.hashCode()).isEqualTo(order2.hashCode());
  }

  @Test
  @DisplayName("Should not be equal when different id")
  void shouldNotBeEqualWhenDifferentId() {
    Order order1 = OrderTestDataBuilder.anOrder()
        .id(new OrderId(TSID.from(123456789L)))
        .build();
    Order order2 = OrderTestDataBuilder.anOrder()
        .id(new OrderId(TSID.from(987654321L)))
        .build();

    assertThat(order1).isNotEqualTo(order2);
  }

  @Test
  @DisplayName("Should not be equal to null")
  void shouldNotBeEqualToNull() {
    Order order = OrderTestDataBuilder.anOrder().build();

    assertThat(order).isNotEqualTo(null);
  }

  @Test
  @DisplayName("Should return id")
  void shouldReturnId() {
    OrderId orderId = new OrderId(TSID.from(123456789L));
    Order order = OrderTestDataBuilder.anOrder().id(orderId).build();

    assertThat(order.id()).isEqualTo(orderId);
  }

  @Test
  @DisplayName("Should return customerId")
  void shouldReturnCustomerId() {
    CustomerId customerId = new CustomerId(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"));
    Order order = OrderTestDataBuilder.anOrder().customerId(customerId).build();

    assertThat(order.customerId()).isEqualTo(customerId);
  }

  @Test
  @DisplayName("Should return totalAmount")
  void shouldReturnTotalAmount() {
    Money totalAmount = new Money("150.00");
    Order order = OrderTestDataBuilder.anOrder().totalAmount(totalAmount).build();

    assertThat(order.totalAmount()).isEqualTo(totalAmount);
  }

  @Test
  @DisplayName("Should return totalItems")
  void shouldReturnTotalItems() {
    Quantity totalItems = new Quantity(new BigDecimal(10));
    Order order = OrderTestDataBuilder.anOrder().totalItems(totalItems).build();

    assertThat(order.totalItems()).isEqualTo(totalItems);
  }

  @Test
  @DisplayName("Should return placedAt")
  void shouldReturnPlacedAt() {
    OffsetDateTime placedAt = OffsetDateTime.now();
    Order order = OrderTestDataBuilder.anOrder().placedAt(placedAt).build();

    assertThat(order.placedAt()).isEqualTo(placedAt);
  }

  @Test
  @DisplayName("Should return paidAt")
  void shouldReturnPaidAt() {
    OffsetDateTime paidAt = OffsetDateTime.now();
    Order order = OrderTestDataBuilder.anOrder().paidAt(paidAt).build();

    assertThat(order.paidAt()).isEqualTo(paidAt);
  }

  @Test
  @DisplayName("Should return canceledAt")
  void shouldReturnCanceledAt() {
    OffsetDateTime canceledAt = OffsetDateTime.now();
    Order order = OrderTestDataBuilder.anOrder().canceledAt(canceledAt).build();

    assertThat(order.canceledAt()).isEqualTo(canceledAt);
  }

  @Test
  @DisplayName("Should return readyAt")
  void shouldReturnReadyAt() {
    OffsetDateTime readyAt = OffsetDateTime.now();
    Order order = OrderTestDataBuilder.anOrder().readyAt(readyAt).build();

    assertThat(order.readyAt()).isEqualTo(readyAt);
  }

  @Test
  @DisplayName("Should return status")
  void shouldReturnStatus() {
    Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PAID).build();

    assertThat(order.status()).isEqualTo(OrderStatus.PAID);
  }

  @Test
  @DisplayName("Should return paymentMethod")
  void shouldReturnPaymentMethod() {
    Order order = OrderTestDataBuilder.anOrder().paymentMethod(PaymentMethod.GATEWAY_BALANCE).build();

    assertThat(order.paymentMethod()).isEqualTo(PaymentMethod.GATEWAY_BALANCE);
  }

  @Test
  @DisplayName("Should return items")
  void shouldReturnItems() {
    Set<OrderItem> items = new HashSet<>();
    Order order = OrderTestDataBuilder.anOrder().items(items).build();

    assertThat(order.items()).isEqualTo(items);
  }

  @Test
  @DisplayName("Should add order item successfully")
  void shouldAddOrderItemSuccessfully() {
    var order = OrderTestDataBuilder.anOrder().customerId(new CustomerId(UUID.randomUUID())).build();
    var product = ProductTestDataBuilder.createProduct()
        .name(new ProductName("Notebook Pro"))
        .price(new Money("100.00"))
        .inStock(true)
        .build();
    var quantity = new Quantity(new BigDecimal("2"));

    order.addOrderItem(product, quantity);

    assertThat(order.items()).hasSize(1);
    var orderItem = order.items().iterator().next();
    assertThat(orderItem.id()).isNotNull();
    assertThat(orderItem.orderId()).isEqualTo(order.id());
    assertThat(orderItem.productId()).isEqualTo(product.id());
    assertThat(orderItem.productName()).isEqualTo(product.name());
    assertThat(orderItem.price()).isEqualTo(product.price());
    assertThat(orderItem.quantity()).isEqualTo(quantity);
    assertThat(orderItem.totalAmount()).isEqualTo(new Money("200.00"));
  }

  @Test
  void shouldThrowExceptionWhenTryToChangeOrderItem() {
    var order = OrderTestDataBuilder.anOrder().customerId(new CustomerId(UUID.randomUUID())).build();
    var product = ProductTestDataBuilder.createProduct()
        .name(new ProductName("Notebook Pro"))
        .price(new Money("100.00"))
        .inStock(true)
        .build();
    var quantity = new Quantity(new BigDecimal("2"));
    order.addOrderItem(product, quantity);

    assertThatThrownBy(() -> order.items().clear())
        .isInstanceOf(UnsupportedOperationException.class);
  }

  @Test
  @DisplayName("Should create draft order using draftOrderBuilder")
  void shouldCreateDraftOrderUsingDraftOrderBuilder() {
    var customerId = new CustomerId(UUID.randomUUID());

    Order order = Order.createDraftOrder(customerId);

    assertThat(order).isNotNull();
    assertThat(order.id()).isNotNull();
    assertThat(order.customerId()).isEqualTo(customerId);
    assertThat(order.totalAmount()).isEqualTo(Money.ZERO);
    assertThat(order.totalItems()).isEqualTo(Quantity.ZERO);
    assertThat(order.status()).isEqualTo(OrderStatus.DRAFT);
    assertThat(order.items()).isEmpty();
    assertThat(order.placedAt()).isNull();
    assertThat(order.paidAt()).isNull();
    assertThat(order.canceledAt()).isNull();
    assertThat(order.readyAt()).isNull();
  }

  @Test
  @DisplayName("Should add multiple order items and recalculate totals")
  void shouldAddMultipleOrderItemsAndRecalculateTotals() {
    var order = Order.createDraftOrder(new CustomerId(UUID.randomUUID()));

    order.addOrderItem(
        ProductTestDataBuilder.createProduct()
            .name(new ProductName("Notebook Pro"))
            .price(new Money("100.00"))
            .inStock(true)
            .build(),
        new Quantity(new BigDecimal("2"))
    );

    order.addOrderItem(
        ProductTestDataBuilder.createProduct()
            .name(new ProductName("Mouse Wireless"))
            .price(new Money("50.00"))
            .inStock(true)
            .build(),
        new Quantity(new BigDecimal("3"))
    );

    assertThat(order.items()).hasSize(2);
    assertThat(order.totalAmount()).isEqualTo(new Money("350.00"));
    assertThat(order.totalItems()).isEqualTo(new Quantity(new BigDecimal("5")));
  }

  @Test
  @DisplayName("Should recalculate total amount including shipping cost")
  void shouldRecalculateTotalAmountIncludingShippingCost() {
    var order = Order.createDraftOrder(new CustomerId(UUID.randomUUID()));

    order.addOrderItem(
        ProductTestDataBuilder.createProduct()
            .name(new ProductName("Product A"))
            .price(new Money("100.00"))
            .inStock(true)
            .build(),
        new Quantity(new BigDecimal("1"))
    );

    order.addOrderItem(
        ProductTestDataBuilder.createProduct()
            .name(new ProductName("Product B"))
            .price(new Money("50.00"))
            .inStock(true)
            .build(),
        new Quantity(new BigDecimal("1"))
    );

    assertThat(order.totalAmount()).isEqualTo(new Money("150.00"));
  }

  @Test
  @DisplayName("Should recalculate total amount with shipping cost when items exist")
  void shouldRecalculateTotalAmountWithShippingCostWhenItemsExist() {
    var order = Order.existingOrderBuilder()
        .id(new OrderId())
        .customerId(new CustomerId(UUID.randomUUID()))
        .totalAmount(Money.ZERO)
        .totalItems(Quantity.ZERO)
        .status(OrderStatus.DRAFT)
        .items(new HashSet<>())
        .build();

    order.addOrderItem(
        ProductTestDataBuilder.createProduct()
            .name(new ProductName("Product A"))
            .price(new Money("100.00"))
            .inStock(true)
            .build(),
        new Quantity(new BigDecimal("1"))
    );

    assertThat(order.totalAmount()).isEqualTo(new Money("100.00"));
  }

  @Test
  @DisplayName("Should update total items when adding order item")
  void shouldUpdateTotalItemsWhenAddingOrderItem() {
    var order = Order.createDraftOrder(new CustomerId(UUID.randomUUID()));

    assertThat(order.totalItems()).isEqualTo(Quantity.ZERO);

    order.addOrderItem(
        ProductTestDataBuilder.createProduct()
            .name(new ProductName("Product A"))
            .price(new Money("100.00"))
            .inStock(true)
            .build(),
        new Quantity(new BigDecimal("3"))
    );

    assertThat(order.totalItems()).isEqualTo(new Quantity(new BigDecimal("3")));
  }

  @Test
  @DisplayName("Should use shipping cost as ZERO when null during recalculation")
  void shouldUseShippingCostAsZeroWhenNullDuringRecalculation() {
    var order = Order.existingOrderBuilder()
        .id(new OrderId())
        .customerId(new CustomerId(UUID.randomUUID()))
        .totalAmount(Money.ZERO)
        .totalItems(Quantity.ZERO)
        .status(OrderStatus.DRAFT)
        .items(new HashSet<>())
        .build();

    order.addOrderItem(
        ProductTestDataBuilder.createProduct()
            .name(new ProductName("Product A"))
            .price(new Money("75.00"))
            .inStock(true)
            .build(),
        new Quantity(new BigDecimal("2"))
    );

    assertThat(order.totalAmount()).isEqualTo(new Money("150.00"));
  }

  @Test
  @DisplayName("Should return billingInfo")
  void shouldReturnBillingInfo() {
    var fullName = new FullName("John", "Doe");
    var document = new Document("12345678900");
    var phone = new Phone("11999999999");
    var zipCode = new ZipCode("12345-678");
    var address = new Address("Main Street", "123", "Apt 1",
        "Downtown", "New York", "NY", zipCode);
    var email = new Email("john.doe@example.com");
    var billingInfo = Billing.builder()
        .fullName(fullName)
        .document(document)
        .phone(phone)
        .address(address)
        .email(email)
        .build();
    var order = OrderTestDataBuilder.anOrder().billingInfo(billingInfo).build();

    assertThat(order.billingInfo()).isEqualTo(billingInfo);
  }

  @Test
  @DisplayName("Should return shippingInfo")
  void shouldReturnShippingInfo() {
    var fullName = new FullName("John", "Doe");
    var document = new Document("12345678900");
    var phone = new Phone("11999999999");
    var zipCode = new ZipCode("12345-678");
    var address = new Address("Main Street", "123", "Apt 1",
        "Downtown", "New York", "NY", zipCode);
    var recepient = new Recepient(fullName, document, phone);
    var shippingInfo = Shipping.builder()
        .shippingCost(new Money("25.00"))
        .expectedDeliveryDate(LocalDate.now().plusDays(10))
        .recepient(recepient)
        .address(address)
        .build();
    var order = OrderTestDataBuilder.anOrder().shippingInfo(shippingInfo).build();

    assertThat(order.shippingInfo()).isEqualTo(shippingInfo);
  }

  @Test
  @DisplayName("Should recalculate total amount when items is empty")
  void shouldRecalculateTotalAmountWhenItemsIsEmpty() {
    var order = Order.existingOrderBuilder()
        .id(new OrderId())
        .customerId(new CustomerId(UUID.randomUUID()))
        .totalAmount(Money.ZERO)
        .totalItems(Quantity.ZERO)
        .status(OrderStatus.DRAFT)
        .items(new HashSet<>())
        .build();

    order.addOrderItem(
        ProductTestDataBuilder.createProduct()
            .name(new ProductName("Product A"))
            .price(new Money("75.00"))
            .inStock(true)
            .build(),
        new Quantity(new BigDecimal("2"))
    );

    assertThat(order.totalAmount()).isEqualTo(new Money("150.00"));
  }

  @Test
  @DisplayName("Should recalculate total items when items is empty")
  void shouldRecalculateTotalItemsWhenItemsIsEmpty() {
    var order = Order.existingOrderBuilder()
        .id(new OrderId())
        .customerId(new CustomerId(UUID.randomUUID()))
        .totalAmount(Money.ZERO)
        .totalItems(Quantity.ZERO)
        .status(OrderStatus.DRAFT)
        .items(new HashSet<>())
        .build();

    order.addOrderItem(
        ProductTestDataBuilder.createProduct()
            .name(new ProductName("Product A"))
            .price(new Money("100.00"))
            .inStock(true)
            .build(),
        new Quantity(new BigDecimal("5"))
    );

    assertThat(order.totalItems()).isEqualTo(new Quantity(new BigDecimal("5")));
  }

  @Test
  @DisplayName("Should add order item to existing order with items")
  void shouldAddOrderItemToExistingOrderWithItems() {
    var order = Order.createDraftOrder(new CustomerId(UUID.randomUUID()));

    order.addOrderItem(
        ProductTestDataBuilder.createProduct()
            .name(new ProductName("Product A"))
            .price(new Money("100.00"))
            .inStock(true)
            .build(),
        new Quantity(new BigDecimal("2"))
    );

    order.addOrderItem(
        ProductTestDataBuilder.createProduct()
            .name(new ProductName("Product B"))
            .price(new Money("50.00"))
            .inStock(true)
            .build(),
        new Quantity(new BigDecimal("3"))
    );

    assertThat(order.items()).hasSize(2);
    assertThat(order.totalAmount()).isEqualTo(new Money("350.00"));
    assertThat(order.totalItems()).isEqualTo(new Quantity(new BigDecimal("5")));
  }

  @Test
  @DisplayName("Should have consistent hashCode")
  void shouldHaveConsistentHashCode() {
    var order = OrderTestDataBuilder.anOrder().build();

    var hashCode1 = order.hashCode();
    var hashCode2 = order.hashCode();

    assertThat(hashCode1).isEqualTo(hashCode2);
  }

  @Test
  @DisplayName("Should recalculate total amount when shipping cost is set after items")
  void shouldRecalculateTotalAmountWhenShippingCostIsSetAfterItems() {
    var order = Order.createDraftOrder(new CustomerId(UUID.randomUUID()));

    order.addOrderItem(
        ProductTestDataBuilder.createProduct()
            .name(new ProductName("Product A"))
            .price(new Money("100.00"))
            .inStock(true)
            .build(),
        new Quantity(new BigDecimal("1"))
    );

    var orderWithShipping = Order.existingOrderBuilder()
        .id(order.id())
        .customerId(order.customerId())
        .totalAmount(order.totalAmount())
        .totalItems(order.totalItems())
        .placedAt(order.placedAt())
        .paidAt(order.paidAt())
        .canceledAt(order.canceledAt())
        .readyAt(order.readyAt())
        .billing(order.billingInfo())
        .shipping(order.shippingInfo())
        .status(order.status())
        .paymentMethod(order.paymentMethod())
        .items(order.items())
        .build();

    orderWithShipping.recalculateTotalAmount();

    assertThat(orderWithShipping.totalAmount()).isEqualTo(new Money("100.00"));
  }

  @Test
  @DisplayName("Should throw exception when createDraftOrder customerId is null")
  void shouldThrowExceptionWhenCreateDraftOrderCustomerIdIsNull() {
    assertThatThrownBy(() -> Order.createDraftOrder(null))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should change order status to placed")
  void shouldChangeOrderStatusToPlaced() {
    var order = Order.createDraftOrder(new CustomerId(UUID.randomUUID()));

    order.addOrderItem(
        ProductTestDataBuilder.createProduct()
            .name(new ProductName("Product A"))
            .price(new Money("100.00"))
            .inStock(true)
            .build(),
        new Quantity(new BigDecimal("1"))
    );
    order.changePaymentMethod(PaymentMethod.CREDIT_CARD);
    order.changeBilling(Billing.builder()
        .fullName(new FullName("John", "Doe"))
        .document(new Document("12345678900"))
        .phone(new Phone("11999999999"))
        .address(Address.builder()
            .street("Rua Teste")
            .number("123")
            .neighborhood("Centro")
            .city("São Paulo")
            .state("SP")
            .zipCode(new ZipCode("01234-567"))
            .build())
        .email(new Email("john.doe@example.com"))
        .build());
    order.changeShipping(Shipping.builder()
        .shippingCost(new Money("10.00"))
        .expectedDeliveryDate(LocalDate.now().plusDays(7))
        .recepient(new Recepient(
            new FullName("John", "Doe"),
            new Document("12345678900"),
            new Phone("11999999999")))
        .address(Address.builder()
            .street("Rua Teste")
            .number("123")
            .neighborhood("Centro")
            .city("São Paulo")
            .state("SP")
            .zipCode(new ZipCode("01234-567"))
            .build())
        .build());

    order.place();

    assertThat(order.status()).isEqualTo(OrderStatus.PLACED);
  }

  @Test
  @DisplayName("Should throw exception when change order status to placed is not allowed")
  void shouldThrowExceptionWhenChangeOrderStatusToPlacedIsNotAllowed() {
    var order = Order.createDraftOrder(new CustomerId(UUID.randomUUID()));
    var existingOrder = Order.existingOrderBuilder()
        .id(order.id())
        .customerId(order.customerId())
        .totalAmount(order.totalAmount())
        .totalItems(order.totalItems())
        .placedAt(order.placedAt())
        .paidAt(order.paidAt())
        .canceledAt(order.canceledAt())
        .readyAt(order.readyAt())
        .billing(order.billingInfo())
        .shipping(order.shippingInfo())
        .status(OrderStatus.PLACED)
        .paymentMethod(order.paymentMethod())
        .items(order.items())
        .build();

    assertThatThrownBy(existingOrder::place).isInstanceOf(OrderCannotBePlacedException.class);
  }

  @Test
  @DisplayName("Should change payment method successfully")
  void shouldChangePaymentMethodSuccessfully() {
    var order = Order.createDraftOrder(new CustomerId(UUID.randomUUID()));
    var newPaymentMethod = PaymentMethod.CREDIT_CARD;

    order.changePaymentMethod(newPaymentMethod);

    assertThat(order.paymentMethod()).isEqualTo(newPaymentMethod);
  }

  @Test
  @DisplayName("Should throw exception when change payment method with null")
  void shouldThrowExceptionWhenChangePaymentMethodWithNull() {
    var order = Order.createDraftOrder(new CustomerId(UUID.randomUUID()));

    assertThatThrownBy(() -> order.changePaymentMethod(null))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should change billing info successfully")
  void shouldChangeBillingInfoSuccessfully() {
    var order = Order.createDraftOrder(new CustomerId(UUID.randomUUID()));
    var fullName = new FullName("John", "Doe");
    var document = new Document("12345678900");
    var phone = new Phone("11999999999");
    var zipCode = new ZipCode("12345-678");
    var address = new Address("Main Street", "123", "Apt 1",
        "Downtown", "New York", "NY", zipCode);
    var email = new Email("john.doe@example.com");
    var billingInfo = Billing.builder()
        .fullName(fullName)
        .document(document)
        .phone(phone)
        .address(address)
        .email(email)
        .build();

    order.changeBilling(billingInfo);

    assertThat(order.billingInfo()).isEqualTo(billingInfo);
  }

  @Test
  @DisplayName("Should throw exception when change billing info with null")
  void shouldThrowExceptionWhenChangeBillingInfoWithNull() {
    var order = Order.createDraftOrder(new CustomerId(UUID.randomUUID()));

    assertThatThrownBy(() -> order.changeBilling(null))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should change shipping info successfully")
  void shouldChangeShippingInfoSuccessfully() {
    var order = Order.createDraftOrder(new CustomerId(UUID.randomUUID()));
    var fullName = new FullName("Jane", "Smith");
    var document = new Document("98765432100");
    var phone = new Phone("11888888888");
    var zipCode = new ZipCode("12345-678");
    var address = new Address("Main Street", "123", "Apt 1",
        "Downtown", "New York", "NY", zipCode);
    var recepient = new Recepient(fullName, document, phone);
    var shippingInfo = Shipping.builder()
        .shippingCost(new Money("25.00"))
        .expectedDeliveryDate(LocalDate.now().plusDays(10))
        .recepient(recepient)
        .address(address)
        .build();

    order.changeShipping(shippingInfo);

    assertThat(order.shippingInfo()).isEqualTo(shippingInfo);
  }

  @Test
  @DisplayName("Should throw exception when change shipping with null shipping info")
  void shouldThrowExceptionWhenChangeShippingWithNullShippingInfo() {
    var order = Order.createDraftOrder(new CustomerId(UUID.randomUUID()));

    assertThatThrownBy(() -> order.changeShipping(null))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should throw exception when change shipping with null shipping cost")
  void shouldThrowExceptionWhenChangeShippingWithNullShippingCost() {
    var order = Order.createDraftOrder(new CustomerId(UUID.randomUUID()));
    var fullName = new FullName("Jane", "Smith");
    var document = new Document("98765432100");
    var phone = new Phone("11888888888");
    var recepient = new Recepient(fullName, document, phone);

    assertThatThrownBy(() -> {
      var zipCode = new ZipCode("12345-678");
      var address = new Address("Main Street", "123", "Apt 1",
          "Downtown", "New York", "NY", zipCode);
      var shippingInfo = Shipping.builder()
          .shippingCost(null)
          .expectedDeliveryDate(LocalDate.now().plusDays(10))
          .recepient(recepient)
          .address(address)
          .build();
      order.changeShipping(shippingInfo);
    }).isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should throw exception when change shipping with null expected delivery date")
  void shouldThrowExceptionWhenChangeShippingWithNullExpectedDeliveryDate() {
    var order = Order.createDraftOrder(new CustomerId(UUID.randomUUID()));
    var fullName = new FullName("Jane", "Smith");
    var document = new Document("98765432100");
    var phone = new Phone("11888888888");
    var recepient = new Recepient(fullName, document, phone);

    assertThatThrownBy(() -> {
      var zipCode = new ZipCode("12345-678");
      var address = new Address("Main Street", "123", "Apt 1",
          "Downtown", "New York", "NY", zipCode);
      var shippingInfo = Shipping.builder()
          .shippingCost(new Money("25.00"))
          .expectedDeliveryDate(null)
          .recepient(recepient)
          .address(address)
          .build();
      order.changeShipping(shippingInfo);
    }).isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should throw exception when change shipping with past delivery date")
  void shouldThrowExceptionWhenChangeShippingWithPastDeliveryDate() {
    var order = Order.createDraftOrder(new CustomerId(UUID.randomUUID()));
    var fullName = new FullName("Jane", "Smith");
    var document = new Document("98765432100");
    var phone = new Phone("11888888888");
    var recepient = new Recepient(fullName, document, phone);
    var zipCode = new ZipCode("12345-678");
    var address = new Address("Main Street", "123", "Apt 1",
        "Downtown", "New York", "NY", zipCode);
    var shippingInfo = Shipping.builder()
        .shippingCost(new Money("25.00"))
        .expectedDeliveryDate(LocalDate.now().minusDays(1))
        .recepient(recepient)
        .address(address)
        .build();

    assertThatThrownBy(() -> order.changeShipping(shippingInfo))
        .isInstanceOf(OrderInvalidShippingDeliveryDateException.class);
  }

  @Test
  @DisplayName("Should change order item quantity successfully")
  void shouldChangeOrderItemQuantitySuccessfully() {
    var order = Order.createDraftOrder(new CustomerId(UUID.randomUUID()));
    order.addOrderItem(
        ProductTestDataBuilder.createProduct()
            .name(new ProductName("Product A"))
            .price(new Money("100.00"))
            .inStock(true)
            .build(),
        new Quantity(new BigDecimal("2"))
    );

    var orderItem = order.items().iterator().next();
    var newQuantity = new Quantity(new BigDecimal("5"));

    order.changeItemQuantity(orderItem.id(), newQuantity);

    assertThat(orderItem.quantity()).isEqualTo(newQuantity);
  }

  @Test
  @DisplayName("Should throw exception when change item quantity with null orderItemId")
  void shouldThrowExceptionWhenChangeItemQuantityWithNullOrderItemId() {
    var order = Order.createDraftOrder(new CustomerId(UUID.randomUUID()));
    var quantity = new Quantity(new BigDecimal("3"));

    assertThatThrownBy(() -> order.changeItemQuantity(null, quantity))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should throw exception when change item quantity with null quantity")
  void shouldThrowExceptionWhenChangeItemQuantityWithNullQuantity() {
    var order = Order.createDraftOrder(new CustomerId(UUID.randomUUID()));
    var orderItemId = new OrderItemId();

    assertThatThrownBy(() -> order.changeItemQuantity(orderItemId, null))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should throw exception when change item quantity with non-existent order item")
  void shouldThrowExceptionWhenChangeItemQuantityWithNonExistentOrderItem() {
    var order = Order.createDraftOrder(new CustomerId(UUID.randomUUID()));
    var nonExistentOrderItemId = new OrderItemId();
    var quantity = new Quantity(new BigDecimal("3"));

    assertThatThrownBy(() -> order.changeItemQuantity(nonExistentOrderItemId, quantity))
        .isInstanceOf(OrderItemNotFoundException.class);
  }

  @Test
  @DisplayName("Should recalculate total amount after changing item quantity")
  void shouldRecalculateTotalAmountAfterChangingItemQuantity() {
    var order = Order.createDraftOrder(new CustomerId(UUID.randomUUID()));
    order.addOrderItem(
        ProductTestDataBuilder.createProduct()
            .name(new ProductName("Product A"))
            .price(new Money("100.00"))
            .inStock(true)
            .build(),
        new Quantity(new BigDecimal("2"))
    );

    var orderItem = order.items().iterator().next();
    var newQuantity = new Quantity(new BigDecimal("4"));

    order.changeItemQuantity(orderItem.id(), newQuantity);

    assertThat(order.totalAmount()).isEqualTo(new Money("400.00"));
  }

  @Test
  @DisplayName("Should recalculate total items after changing item quantity")
  void shouldRecalculateTotalItemsAfterChangingItemQuantity() {
    var order = Order.createDraftOrder(new CustomerId(UUID.randomUUID()));
    order.addOrderItem(
        ProductTestDataBuilder.createProduct()
            .name(new ProductName("Product A"))
            .price(new Money("100.00"))
            .inStock(true)
            .build(),
        new Quantity(new BigDecimal("2"))
    );

    var orderItem = order.items().iterator().next();
    var newQuantity = new Quantity(new BigDecimal("5"));

    order.changeItemQuantity(orderItem.id(), newQuantity);

    assertThat(order.totalItems()).isEqualTo(new Quantity(new BigDecimal("5")));
  }

  @Test
  @DisplayName("Should allow adding order item when order is in DRAFT status")
  void shouldAllowAddingOrderItemWhenOrderIsInDraftStatus() {
    var order = Order.createDraftOrder(new CustomerId(UUID.randomUUID()));
    var product = ProductTestDataBuilder.createProduct()
        .name(new ProductName("Product A"))
        .price(new Money("100.00"))
        .inStock(true)
        .build();
    var quantity = new Quantity(new BigDecimal("2"));

    order.addOrderItem(product, quantity);

    assertThat(order.items()).hasSize(1);
  }

  @Test
  @DisplayName("Should throw exception when adding order item to PLACED order")
  void shouldThrowExceptionWhenAddingOrderItemToPlacedOrder() {
    var order = Order.existingOrderBuilder()
        .id(new OrderId())
        .customerId(new CustomerId(UUID.randomUUID()))
        .totalAmount(Money.ZERO)
        .totalItems(Quantity.ZERO)
        .status(OrderStatus.PLACED)
        .items(new HashSet<>())
        .build();
    var product = ProductTestDataBuilder.createProduct()
        .name(new ProductName("Product A"))
        .price(new Money("100.00"))
        .inStock(true)
        .build();
    var quantity = new Quantity(new BigDecimal("2"));

    assertThatThrownBy(() -> order.addOrderItem(product, quantity))
        .isInstanceOf(OrderCannotBeEditedException.class);
  }

  @Test
  @DisplayName("Should throw exception when adding order item to PAID order")
  void shouldThrowExceptionWhenAddingOrderItemToPaidOrder() {
    var order = Order.existingOrderBuilder()
        .id(new OrderId())
        .customerId(new CustomerId(UUID.randomUUID()))
        .totalAmount(Money.ZERO)
        .totalItems(Quantity.ZERO)
        .status(OrderStatus.PAID)
        .items(new HashSet<>())
        .build();
    var product = ProductTestDataBuilder.createProduct()
        .name(new ProductName("Product A"))
        .price(new Money("100.00"))
        .inStock(true)
        .build();
    var quantity = new Quantity(new BigDecimal("2"));

    assertThatThrownBy(() -> order.addOrderItem(product, quantity))
        .isInstanceOf(OrderCannotBeEditedException.class);
  }

  @Test
  @DisplayName("Should throw exception when adding order item to READY order")
  void shouldThrowExceptionWhenAddingOrderItemToReadyOrder() {
    var order = Order.existingOrderBuilder()
        .id(new OrderId())
        .customerId(new CustomerId(UUID.randomUUID()))
        .totalAmount(Money.ZERO)
        .totalItems(Quantity.ZERO)
        .status(OrderStatus.READY)
        .items(new HashSet<>())
        .build();
    var product = ProductTestDataBuilder.createProduct()
        .name(new ProductName("Product A"))
        .price(new Money("100.00"))
        .inStock(true)
        .build();
    var quantity = new Quantity(new BigDecimal("2"));

    assertThatThrownBy(() -> order.addOrderItem(product, quantity))
        .isInstanceOf(OrderCannotBeEditedException.class);
  }

  @Test
  @DisplayName("Should throw exception when adding order item to CANCELED order")
  void shouldThrowExceptionWhenAddingOrderItemToCanceledOrder() {
    var order = Order.existingOrderBuilder()
        .id(new OrderId())
        .customerId(new CustomerId(UUID.randomUUID()))
        .totalAmount(Money.ZERO)
        .totalItems(Quantity.ZERO)
        .status(OrderStatus.CANCELED)
        .items(new HashSet<>())
        .build();
    var product = ProductTestDataBuilder.createProduct()
        .name(new ProductName("Product A"))
        .price(new Money("100.00"))
        .inStock(true)
        .build();
    var quantity = new Quantity(new BigDecimal("2"));

    assertThatThrownBy(() -> order.addOrderItem(product, quantity))
        .isInstanceOf(OrderCannotBeEditedException.class);
  }

  @Test
  @DisplayName("Should allow changing payment method when order is in DRAFT status")
  void shouldAllowChangingPaymentMethodWhenOrderIsInDraftStatus() {
    var order = Order.createDraftOrder(new CustomerId(UUID.randomUUID()));
    var newPaymentMethod = PaymentMethod.CREDIT_CARD;

    order.changePaymentMethod(newPaymentMethod);

    assertThat(order.paymentMethod()).isEqualTo(newPaymentMethod);
  }

  @Test
  @DisplayName("Should throw exception when changing payment method on PLACED order")
  void shouldThrowExceptionWhenChangingPaymentMethodOnPlacedOrder() {
    var order = Order.existingOrderBuilder()
        .id(new OrderId())
        .customerId(new CustomerId(UUID.randomUUID()))
        .totalAmount(Money.ZERO)
        .totalItems(Quantity.ZERO)
        .status(OrderStatus.PLACED)
        .items(new HashSet<>())
        .build();

    assertThatThrownBy(() -> order.changePaymentMethod(PaymentMethod.CREDIT_CARD))
        .isInstanceOf(OrderCannotBeEditedException.class);
  }

  @Test
  @DisplayName("Should allow changing billing info when order is in DRAFT status")
  void shouldAllowChangingBillingInfoWhenOrderIsInDraftStatus() {
    var order = Order.createDraftOrder(new CustomerId(UUID.randomUUID()));
    var billingInfo = Billing.builder()
        .fullName(new FullName("John", "Doe"))
        .document(new Document("12345678900"))
        .phone(new Phone("11999999999"))
        .address(Address.builder()
            .street("Main Street")
            .number("123")
            .neighborhood("Downtown")
            .city("New York")
            .state("NY")
            .zipCode(new ZipCode("12345-678"))
            .build())
        .email(new Email("john.doe@example.com"))
        .build();

    order.changeBilling(billingInfo);

    assertThat(order.billingInfo()).isEqualTo(billingInfo);
  }

  @Test
  @DisplayName("Should throw exception when changing billing info on PAID order")
  void shouldThrowExceptionWhenChangingBillingInfoOnPaidOrder() {
    var order = Order.existingOrderBuilder()
        .id(new OrderId())
        .customerId(new CustomerId(UUID.randomUUID()))
        .totalAmount(Money.ZERO)
        .totalItems(Quantity.ZERO)
        .status(OrderStatus.PAID)
        .items(new HashSet<>())
        .build();
    var billingInfo = Billing.builder()
        .fullName(new FullName("John", "Doe"))
        .document(new Document("12345678900"))
        .phone(new Phone("11999999999"))
        .address(Address.builder()
            .street("Main Street")
            .number("123")
            .neighborhood("Downtown")
            .city("New York")
            .state("NY")
            .zipCode(new ZipCode("12345-678"))
            .build())
        .email(new Email("john.doe@example.com"))
        .build();

    assertThatThrownBy(() -> order.changeBilling(billingInfo))
        .isInstanceOf(OrderCannotBeEditedException.class);
  }

  @Test
  @DisplayName("Should allow changing shipping info when order is in DRAFT status")
  void shouldAllowChangingShippingInfoWhenOrderIsInDraftStatus() {
    var order = Order.createDraftOrder(new CustomerId(UUID.randomUUID()));
    var shippingInfo = Shipping.builder()
        .shippingCost(new Money("25.00"))
        .expectedDeliveryDate(LocalDate.now().plusDays(10))
        .recepient(new Recepient(
            new FullName("Jane", "Smith"),
            new Document("98765432100"),
            new Phone("11888888888")))
        .address(Address.builder()
            .street("Main Street")
            .number("123")
            .neighborhood("Downtown")
            .city("New York")
            .state("NY")
            .zipCode(new ZipCode("12345-678"))
            .build())
        .build();

    order.changeShipping(shippingInfo);

    assertThat(order.shippingInfo()).isEqualTo(shippingInfo);
  }

  @Test
  @DisplayName("Should throw exception when changing shipping info on READY order")
  void shouldThrowExceptionWhenChangingShippingInfoOnReadyOrder() {
    var order = Order.existingOrderBuilder()
        .id(new OrderId())
        .customerId(new CustomerId(UUID.randomUUID()))
        .totalAmount(Money.ZERO)
        .totalItems(Quantity.ZERO)
        .status(OrderStatus.READY)
        .items(new HashSet<>())
        .build();
    var shippingInfo = Shipping.builder()
        .shippingCost(new Money("25.00"))
        .expectedDeliveryDate(LocalDate.now().plusDays(10))
        .recepient(new Recepient(
            new FullName("Jane", "Smith"),
            new Document("98765432100"),
            new Phone("11888888888")))
        .address(Address.builder()
            .street("Main Street")
            .number("123")
            .neighborhood("Downtown")
            .city("New York")
            .state("NY")
            .zipCode(new ZipCode("12345-678"))
            .build())
        .build();

    assertThatThrownBy(() -> order.changeShipping(shippingInfo))
        .isInstanceOf(OrderCannotBeEditedException.class);
  }

  @Test
  @DisplayName("Should allow changing item quantity when order is in DRAFT status")
  void shouldAllowChangingItemQuantityWhenOrderIsInDraftStatus() {
    var order = Order.createDraftOrder(new CustomerId(UUID.randomUUID()));
    order.addOrderItem(
        ProductTestDataBuilder.createProduct()
            .name(new ProductName("Product A"))
            .price(new Money("100.00"))
            .inStock(true)
            .build(),
        new Quantity(new BigDecimal("2"))
    );

    var orderItem = order.items().iterator().next();
    var newQuantity = new Quantity(new BigDecimal("5"));

    order.changeItemQuantity(orderItem.id(), newQuantity);

    assertThat(orderItem.quantity()).isEqualTo(newQuantity);
  }

  @Test
  @DisplayName("Should throw exception when changing item quantity on CANCELED order")
  void shouldThrowExceptionWhenChangingItemQuantityOnCanceledOrder() {
    var order = Order.existingOrderBuilder()
        .id(new OrderId())
        .customerId(new CustomerId(UUID.randomUUID()))
        .totalAmount(Money.ZERO)
        .totalItems(Quantity.ZERO)
        .status(OrderStatus.CANCELED)
        .items(new HashSet<>())
        .build();
    var orderItemId = new OrderItemId();
    var quantity = new Quantity(new BigDecimal("3"));

    assertThatThrownBy(() -> order.changeItemQuantity(orderItemId, quantity))
        .isInstanceOf(OrderCannotBeEditedException.class);
  }

  @Test
  @DisplayName("Should remove order item successfully when order is in DRAFT status")
  void shouldRemoveOrderItemSuccessfullyWhenOrderIsInDraftStatus() {
    var order = Order.createDraftOrder(new CustomerId(UUID.randomUUID()));
    order.addOrderItem(
        ProductTestDataBuilder.createProduct()
            .name(new ProductName("Product A"))
            .price(new Money("100.00"))
            .inStock(true)
            .build(),
        new Quantity(new BigDecimal("2"))
    );
    order.addOrderItem(
        ProductTestDataBuilder.createProduct()
            .name(new ProductName("Product B"))
            .price(new Money("50.00"))
            .inStock(true)
            .build(),
        new Quantity(new BigDecimal("3"))
    );

    var orderItem = order.items().iterator().next();
    var orderItemId = orderItem.id();

    order.removeItem(orderItemId);

    assertThat(order.items()).hasSize(1);
    assertThat(order.items().stream().noneMatch(item -> item.id().equals(orderItemId))).isTrue();
  }

  @Test
  @DisplayName("Should recalculate total amount after removing order item")
  void shouldRecalculateTotalAmountAfterRemovingOrderItem() {
    var order = Order.createDraftOrder(new CustomerId(UUID.randomUUID()));
    order.addOrderItem(
        ProductTestDataBuilder.createProduct()
            .name(new ProductName("Product A"))
            .price(new Money("100.00"))
            .inStock(true)
            .build(),
        new Quantity(new BigDecimal("2"))
    );
    order.addOrderItem(
        ProductTestDataBuilder.createProduct()
            .name(new ProductName("Product B"))
            .price(new Money("50.00"))
            .inStock(true)
            .build(),
        new Quantity(new BigDecimal("3"))
    );

    var orderItem = order.items().iterator().next();
    var orderItemId = orderItem.id();

    order.removeItem(orderItemId);

    assertThat(order.totalAmount()).isEqualTo(new Money("150.00"));
  }

  @Test
  @DisplayName("Should recalculate total items after removing order item")
  void shouldRecalculateTotalItemsAfterRemovingOrderItem() {
    var order = Order.createDraftOrder(new CustomerId(UUID.randomUUID()));
    order.addOrderItem(
        ProductTestDataBuilder.createProduct()
            .name(new ProductName("Product A"))
            .price(new Money("100.00"))
            .inStock(true)
            .build(),
        new Quantity(new BigDecimal("2"))
    );
    order.addOrderItem(
        ProductTestDataBuilder.createProduct()
            .name(new ProductName("Product B"))
            .price(new Money("50.00"))
            .inStock(true)
            .build(),
        new Quantity(new BigDecimal("3"))
    );

    var orderItem = order.items().iterator().next();
    var orderItemId = orderItem.id();

    order.removeItem(orderItemId);

    assertThat(order.totalItems()).isEqualTo(new Quantity(new BigDecimal("3")));
  }

  @Test
  @DisplayName("Should throw exception when removing item with null orderItemId")
  void shouldThrowExceptionWhenRemovingItemWithNullOrderItemId() {
    var order = Order.createDraftOrder(new CustomerId(UUID.randomUUID()));

    assertThatThrownBy(() -> order.removeItem(null))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should throw exception when removing item from PLACED order")
  void shouldThrowExceptionWhenRemovingItemFromPlacedOrder() {
    var order = Order.existingOrderBuilder()
        .id(new OrderId())
        .customerId(new CustomerId(UUID.randomUUID()))
        .totalAmount(Money.ZERO)
        .totalItems(Quantity.ZERO)
        .status(OrderStatus.PLACED)
        .items(new HashSet<>())
        .build();
    var orderItemId = new OrderItemId();

    assertThatThrownBy(() -> order.removeItem(orderItemId))
        .isInstanceOf(OrderCannotBeEditedException.class);
  }

  @Test
  @DisplayName("Should throw exception when removing item from PAID order")
  void shouldThrowExceptionWhenRemovingItemFromPaidOrder() {
    var order = Order.existingOrderBuilder()
        .id(new OrderId())
        .customerId(new CustomerId(UUID.randomUUID()))
        .totalAmount(Money.ZERO)
        .totalItems(Quantity.ZERO)
        .status(OrderStatus.PAID)
        .items(new HashSet<>())
        .build();
    var orderItemId = new OrderItemId();

    assertThatThrownBy(() -> order.removeItem(orderItemId))
        .isInstanceOf(OrderCannotBeEditedException.class);
  }

  @Test
  @DisplayName("Should throw exception when removing item from READY order")
  void shouldThrowExceptionWhenRemovingItemFromReadyOrder() {
    var order = Order.existingOrderBuilder()
        .id(new OrderId())
        .customerId(new CustomerId(UUID.randomUUID()))
        .totalAmount(Money.ZERO)
        .totalItems(Quantity.ZERO)
        .status(OrderStatus.READY)
        .items(new HashSet<>())
        .build();
    var orderItemId = new OrderItemId();

    assertThatThrownBy(() -> order.removeItem(orderItemId))
        .isInstanceOf(OrderCannotBeEditedException.class);
  }

  @Test
  @DisplayName("Should throw exception when removing item from CANCELED order")
  void shouldThrowExceptionWhenRemovingItemFromCanceledOrder() {
    var order = Order.existingOrderBuilder()
        .id(new OrderId())
        .customerId(new CustomerId(UUID.randomUUID()))
        .totalAmount(Money.ZERO)
        .totalItems(Quantity.ZERO)
        .status(OrderStatus.CANCELED)
        .items(new HashSet<>())
        .build();
    var orderItemId = new OrderItemId();

    assertThatThrownBy(() -> order.removeItem(orderItemId))
        .isInstanceOf(OrderCannotBeEditedException.class);
  }

  @Test
  @DisplayName("Should throw exception when removing non-existent order item")
  void shouldThrowExceptionWhenRemovingNonExistentOrderItem() {
    var order = Order.createDraftOrder(new CustomerId(UUID.randomUUID()));
    var nonExistentOrderItemId = new OrderItemId();

    assertThatThrownBy(() -> order.removeItem(nonExistentOrderItemId))
        .isInstanceOf(OrderItemNotFoundException.class);
  }

  @Test
  @DisplayName("Should mark order as ready when status is PAID")
  void shouldMarkOrderAsReadyWhenStatusIsPaid() {
    var order = Order.existingOrderBuilder()
        .id(new OrderId())
        .customerId(new CustomerId(UUID.randomUUID()))
        .totalAmount(new Money("100.00"))
        .totalItems(new Quantity(new BigDecimal("2")))
        .status(OrderStatus.PAID)
        .items(new HashSet<>())
        .build();

    order.markAsReady();

    assertThat(order.status()).isEqualTo(OrderStatus.READY);
    assertThat(order.readyAt()).isNotNull();
  }

  @Test
  @DisplayName("Should throw exception when mark as ready from DRAFT status")
  void shouldThrowExceptionWhenMarkAsReadyFromDraftStatus() {
    var order = Order.existingOrderBuilder()
        .id(new OrderId())
        .customerId(new CustomerId(UUID.randomUUID()))
        .totalAmount(Money.ZERO)
        .totalItems(Quantity.ZERO)
        .status(OrderStatus.DRAFT)
        .items(new HashSet<>())
        .build();

    assertThatThrownBy(order::markAsReady)
        .isInstanceOf(OrderCannotBeReadyException.class);
  }

  @Test
  @DisplayName("Should throw exception when mark as ready from PLACED status")
  void shouldThrowExceptionWhenMarkAsReadyFromPlacedStatus() {
    var order = Order.existingOrderBuilder()
        .id(new OrderId())
        .customerId(new CustomerId(UUID.randomUUID()))
        .totalAmount(new Money("100.00"))
        .totalItems(new Quantity(new BigDecimal("2")))
        .status(OrderStatus.PLACED)
        .items(new HashSet<>())
        .build();

    assertThatThrownBy(order::markAsReady)
        .isInstanceOf(OrderCannotBeReadyException.class);
  }

  @Test
  @DisplayName("Should throw exception when mark as ready from READY status")
  void shouldThrowExceptionWhenMarkAsReadyFromReadyStatus() {
    var order = Order.existingOrderBuilder()
        .id(new OrderId())
        .customerId(new CustomerId(UUID.randomUUID()))
        .totalAmount(new Money("100.00"))
        .totalItems(new Quantity(new BigDecimal("2")))
        .status(OrderStatus.READY)
        .items(new HashSet<>())
        .build();

    assertThatThrownBy(order::markAsReady)
        .isInstanceOf(OrderCannotBeReadyException.class);
  }

  @Test
  @DisplayName("Should throw exception when mark as ready from CANCELED status")
  void shouldThrowExceptionWhenMarkAsReadyFromCanceledStatus() {
    var order = Order.existingOrderBuilder()
        .id(new OrderId())
        .customerId(new CustomerId(UUID.randomUUID()))
        .totalAmount(new Money("100.00"))
        .totalItems(new Quantity(new BigDecimal("2")))
        .status(OrderStatus.CANCELED)
        .items(new HashSet<>())
        .build();

    assertThatThrownBy(order::markAsReady)
        .isInstanceOf(OrderCannotBeReadyException.class);
  }

  @Test
  @DisplayName("Should set readyAt timestamp when marking as ready")
  void shouldSetReadyAtTimestampWhenMarkingAsReady() {
    var order = Order.existingOrderBuilder()
        .id(new OrderId())
        .customerId(new CustomerId(UUID.randomUUID()))
        .totalAmount(new Money("100.00"))
        .totalItems(new Quantity(new BigDecimal("2")))
        .status(OrderStatus.PAID)
        .items(new HashSet<>())
        .readyAt(null)
        .build();

    var beforeReady = OffsetDateTime.now();
    order.markAsReady();
    var afterReady = OffsetDateTime.now();

    assertThat(order.readyAt()).isNotNull();
    assertThat(order.readyAt()).isBetween(beforeReady, afterReady);
  }

  @Test
  @DisplayName("Should update readyAt when marking as ready multiple times")
  void shouldUpdateReadyAtWhenMarkingAsReadyMultipleTimes() {
    var order = Order.existingOrderBuilder()
        .id(new OrderId())
        .customerId(new CustomerId(UUID.randomUUID()))
        .totalAmount(new Money("100.00"))
        .totalItems(new Quantity(new BigDecimal("2")))
        .status(OrderStatus.PAID)
        .items(new HashSet<>())
        .readyAt(null)
        .build();

    order.markAsReady();
    var firstReadyAt = order.readyAt();

    // Reset status to PAID to allow marking as ready again
    var orderForSecondReady = Order.existingOrderBuilder()
        .id(order.id())
        .customerId(order.customerId())
        .totalAmount(order.totalAmount())
        .totalItems(order.totalItems())
        .placedAt(order.placedAt())
        .paidAt(order.paidAt())
        .canceledAt(order.canceledAt())
        .readyAt(order.readyAt())
        .billing(order.billingInfo())
        .shipping(order.shippingInfo())
        .status(OrderStatus.PAID)
        .paymentMethod(order.paymentMethod())
        .items(order.items())
        .build();

    orderForSecondReady.markAsReady();
    var secondReadyAt = orderForSecondReady.readyAt();

    assertThat(secondReadyAt).isNotNull();
    assertThat(secondReadyAt).isAfter(firstReadyAt);
  }

  @Test
  @DisplayName("Should cancel order from DRAFT status")
  void shouldCancelOrderFromDraftStatus() {
    var order = Order.createDraftOrder(new CustomerId(UUID.randomUUID()));

    order.cancel();

    assertThat(order.status()).isEqualTo(OrderStatus.CANCELED);
    assertThat(order.canceledAt()).isNotNull();
    assertThat(order.canceledAt()).isBeforeOrEqualTo(OffsetDateTime.now());
  }

  @Test
  @DisplayName("Should cancel order from PLACED status")
  void shouldCancelOrderFromPlacedStatus() {
    var order = Order.createDraftOrder(new CustomerId(UUID.randomUUID()));
    order.addOrderItem(
        ProductTestDataBuilder.createProduct()
            .name(new ProductName("Product A"))
            .price(new Money("100.00"))
            .inStock(true)
            .build(),
        new Quantity(new BigDecimal("1"))
    );
    order.changePaymentMethod(PaymentMethod.CREDIT_CARD);
    order.changeBilling(Billing.builder()
        .fullName(new FullName("John", "Doe"))
        .document(new Document("12345678900"))
        .phone(new Phone("11999999999"))
        .address(Address.builder()
            .street("Rua Teste")
            .number("123")
            .neighborhood("Centro")
            .city("São Paulo")
            .state("SP")
            .zipCode(new ZipCode("01234-567"))
            .build())
        .email(new Email("john.doe@example.com"))
        .build());
    order.changeShipping(Shipping.builder()
        .shippingCost(new Money("10.00"))
        .expectedDeliveryDate(LocalDate.now().plusDays(7))
        .recepient(new Recepient(
            new FullName("John", "Doe"),
            new Document("12345678900"),
            new Phone("11999999999")))
        .address(Address.builder()
            .street("Rua Teste")
            .number("123")
            .neighborhood("Centro")
            .city("São Paulo")
            .state("SP")
            .zipCode(new ZipCode("01234-567"))
            .build())
        .build());
    order.place();

    order.cancel();

    assertThat(order.status()).isEqualTo(OrderStatus.CANCELED);
    assertThat(order.canceledAt()).isNotNull();
    assertThat(order.canceledAt()).isBeforeOrEqualTo(OffsetDateTime.now());
  }

  @Test
  @DisplayName("Should cancel order from PAID status")
  void shouldCancelOrderFromPaidStatus() {
    var order = OrderTestDataBuilder.anOrder()
        .status(OrderStatus.PAID)
        .build();

    order.cancel();

    assertThat(order.status()).isEqualTo(OrderStatus.CANCELED);
    assertThat(order.canceledAt()).isNotNull();
    assertThat(order.canceledAt()).isBeforeOrEqualTo(OffsetDateTime.now());
  }

  @Test
  @DisplayName("Should cancel order from READY status")
  void shouldCancelOrderFromReadyStatus() {
    var order = OrderTestDataBuilder.anOrder()
        .status(OrderStatus.READY)
        .build();

    order.cancel();

    assertThat(order.status()).isEqualTo(OrderStatus.CANCELED);
    assertThat(order.canceledAt()).isNotNull();
    assertThat(order.canceledAt()).isBeforeOrEqualTo(OffsetDateTime.now());
  }

  @Test
  @DisplayName("Should throw exception when canceling from CANCELED status")
  void shouldThrowExceptionWhenCancelingFromCanceledStatus() {
    var order = OrderTestDataBuilder.anOrder()
        .status(OrderStatus.CANCELED)
        .build();

    assertThatThrownBy(order::cancel)
        .isInstanceOf(OrderCannotBeCanceledException.class);
  }
}
