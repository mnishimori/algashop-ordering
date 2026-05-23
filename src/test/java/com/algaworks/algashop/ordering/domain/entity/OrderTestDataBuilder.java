package com.algaworks.algashop.ordering.domain.entity;

import com.algaworks.algashop.ordering.domain.valueobject.BillingInfo;
import com.algaworks.algashop.ordering.domain.valueobject.Money;
import com.algaworks.algashop.ordering.domain.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.valueobject.ShippingInfo;
import com.algaworks.algashop.ordering.domain.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.valueobject.id.OrderId;
import io.hypersistence.tsid.TSID;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

public class OrderTestDataBuilder {

  private OrderId id;
  private CustomerId customerId;
  private Money totalAmount;
  private Quantity totalItems;
  private OffsetDateTime placedAt;
  private OffsetDateTime paidAt;
  private OffsetDateTime canceledAt;
  private OffsetDateTime readyAt;
  private BillingInfo billingInfo;
  private ShippingInfo shippingInfo;
  private OrderStatus status;
  private PaymentMethod paymentMethod;
  private Money shippingCost;
  private LocalDate expectedDeliveryDate;
  private Set<OrderItem> items;

  private OrderTestDataBuilder() {
    this.id = new OrderId(TSID.from(123456789L));
    this.customerId = new CustomerId(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"));
    this.totalAmount = Money.ZERO;
    this.totalItems = Quantity.ZERO;
    this.placedAt = OffsetDateTime.now();
    this.paidAt = null;
    this.canceledAt = null;
    this.readyAt = null;
    this.billingInfo = null;
    this.shippingInfo = null;
    this.status = OrderStatus.DRAFT;
    this.paymentMethod = null;
    this.shippingCost = Money.ZERO;
    this.expectedDeliveryDate = LocalDate.now().plusDays(7);
    this.items = new LinkedHashSet<>();
  }

  public static OrderTestDataBuilder anOrder() {
    return new OrderTestDataBuilder();
  }

  public OrderTestDataBuilder id(OrderId id) {
    this.id = id;
    return this;
  }

  public OrderTestDataBuilder customerId(CustomerId customerId) {
    this.customerId = customerId;
    return this;
  }

  public OrderTestDataBuilder totalAmount(Money totalAmount) {
    this.totalAmount = totalAmount;
    return this;
  }

  public OrderTestDataBuilder totalItems(Quantity totalItems) {
    this.totalItems = totalItems;
    return this;
  }

  public OrderTestDataBuilder placedAt(OffsetDateTime placedAt) {
    this.placedAt = placedAt;
    return this;
  }

  public OrderTestDataBuilder paidAt(OffsetDateTime paidAt) {
    this.paidAt = paidAt;
    return this;
  }

  public OrderTestDataBuilder canceledAt(OffsetDateTime canceledAt) {
    this.canceledAt = canceledAt;
    return this;
  }

  public OrderTestDataBuilder readyAt(OffsetDateTime readyAt) {
    this.readyAt = readyAt;
    return this;
  }

  public OrderTestDataBuilder billingInfo(BillingInfo billingInfo) {
    this.billingInfo = billingInfo;
    return this;
  }

  public OrderTestDataBuilder shippingInfo(ShippingInfo shippingInfo) {
    this.shippingInfo = shippingInfo;
    return this;
  }

  public OrderTestDataBuilder status(OrderStatus status) {
    this.status = status;
    return this;
  }

  public OrderTestDataBuilder paymentMethod(PaymentMethod paymentMethod) {
    this.paymentMethod = paymentMethod;
    return this;
  }

  public OrderTestDataBuilder shippingCost(Money shippingCost) {
    this.shippingCost = shippingCost;
    return this;
  }

  public OrderTestDataBuilder expectedDeliveryDate(LocalDate expectedDeliveryDate) {
    this.expectedDeliveryDate = expectedDeliveryDate;
    return this;
  }

  public OrderTestDataBuilder items(Set<OrderItem> items) {
    this.items = items;
    return this;
  }

  public Order build() {
    return new Order(id, customerId, totalAmount, totalItems, placedAt, paidAt, canceledAt, readyAt,
        billingInfo, shippingInfo, status, paymentMethod, shippingCost, expectedDeliveryDate, items);
  }
}
