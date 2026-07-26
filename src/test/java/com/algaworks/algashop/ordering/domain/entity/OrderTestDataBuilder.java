package com.algaworks.algashop.ordering.domain.entity;

import com.algaworks.algashop.ordering.domain.model.entity.Order;
import com.algaworks.algashop.ordering.domain.model.entity.OrderItem;
import com.algaworks.algashop.ordering.domain.model.entity.OrderStatus;
import com.algaworks.algashop.ordering.domain.model.entity.PaymentMethod;
import com.algaworks.algashop.ordering.domain.model.valueobject.Billing;
import com.algaworks.algashop.ordering.domain.model.valueobject.Money;
import com.algaworks.algashop.ordering.domain.model.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.model.valueobject.Shipping;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderId;
import io.hypersistence.tsid.TSID;
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
  private Billing billing;
  private Shipping shipping;
  private OrderStatus status;
  private PaymentMethod paymentMethod;
  private Set<OrderItem> items;
  private Long version;
  private boolean withItems = true;

  private OrderTestDataBuilder() {
    this.id = new OrderId(TSID.from(123456789L));
    this.customerId = new CustomerId(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"));
    this.totalAmount = Money.ZERO;
    this.totalItems = Quantity.ZERO;
    this.placedAt = OffsetDateTime.now();
    this.paidAt = null;
    this.canceledAt = null;
    this.readyAt = null;
    this.billing = null;
    this.shipping = null;
    this.status = OrderStatus.DRAFT;
    this.paymentMethod = null;
    this.items = new LinkedHashSet<>();
    this.version = 0L;
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

  public OrderTestDataBuilder billingInfo(Billing billing) {
    this.billing = billing;
    return this;
  }

  public OrderTestDataBuilder shippingInfo(Shipping shipping) {
    this.shipping = shipping;
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

  public OrderTestDataBuilder items(Set<OrderItem> items) {
    this.items = items;
    return this;
  }

  public OrderTestDataBuilder withItems(boolean withItems) {
    this.withItems = withItems;
    return this;
  }

  public Order build() {
    return new Order(id, version, customerId, totalAmount, totalItems, placedAt, paidAt, canceledAt, readyAt,
        billing, shipping, status, paymentMethod, items);
  }
}
