package com.algaworks.algashop.ordering.domain.model.entity;

import com.algaworks.algashop.ordering.domain.model.exception.OrderCannotBeCanceledException;
import com.algaworks.algashop.ordering.domain.model.exception.OrderCannotBeEditedException;
import com.algaworks.algashop.ordering.domain.model.exception.OrderCannotBePlacedException;
import com.algaworks.algashop.ordering.domain.model.exception.OrderCannotBeReadyException;
import com.algaworks.algashop.ordering.domain.model.exception.OrderInvalidShippingDeliveryDateException;
import com.algaworks.algashop.ordering.domain.model.exception.OrderItemNotFoundException;
import com.algaworks.algashop.ordering.domain.model.exception.OrderStatusCannotBeChangedException;
import com.algaworks.algashop.ordering.domain.model.valueobject.Billing;
import com.algaworks.algashop.ordering.domain.model.valueobject.Money;
import com.algaworks.algashop.ordering.domain.model.valueobject.Product;
import com.algaworks.algashop.ordering.domain.model.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.model.valueobject.Shipping;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderItemId;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import lombok.Builder;

public class Order {

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
  private PaymentMethod paymentMethod;
  private Set<OrderItem> items;
  private OrderStatus status;

  @Builder(builderClassName = "OrderBuilder", builderMethodName = "existingOrderBuilder")
  public Order(OrderId id, CustomerId customerId, Money totalAmount, Quantity totalItems, OffsetDateTime placedAt,
      OffsetDateTime paidAt, OffsetDateTime canceledAt, OffsetDateTime readyAt, Billing billing,
      Shipping shipping, OrderStatus status, PaymentMethod paymentMethod, Set<OrderItem> items) {
    setId(id);
    setCustomerId(customerId);
    setTotalAmount(totalAmount);
    setTotalItems(totalItems);
    setPlacedAt(placedAt);
    setPaidAt(paidAt);
    setCanceledAt(canceledAt);
    setReadyAt(readyAt);
    setBilling(billing);
    setShipping(shipping);
    setStatus(status);
    setPaymentMethod(paymentMethod);
    setItems(items);
  }

  public static Order createDraftOrder(CustomerId customerId) {
    return new Order(new OrderId(), customerId, Money.ZERO, Quantity.ZERO, null, null, null, null, null, null,
        OrderStatus.DRAFT, null, new LinkedHashSet<>());
  }

  public void addOrderItem(Product product, Quantity quantity) {
    Objects.requireNonNull(product);
    Objects.requireNonNull(quantity);
    this.verifyIfOrderChangeable();
    product.changeOutStock();
    var orderItem = OrderItem.draftOrderItemBuilder()
        .orderId(this.id())
        .product(product)
        .quantity(quantity)
        .build();
    this.items.add(orderItem);
    this.recalculateTotalItems();
    this.recalculateTotalAmount();
  }

  public void place() {
    this.verifyIfCanChangeToPlaced();
    changeStatus(OrderStatus.PLACED);
    this.setPlacedAt(OffsetDateTime.now());
  }

  private void verifyIfCanChangeToPlaced() {
    if (this.shippingInfo() == null) {
      throw OrderCannotBePlacedException.noShippingInfo(this.id());
    }
    if (this.billingInfo() == null) {
      throw OrderCannotBePlacedException.noBillingInfo(this.id());
    }
    if (this.paymentMethod() == null) {
      throw OrderCannotBePlacedException.noPaymentMethod(this.id());
    }
    if (this.items() == null || this.items().isEmpty()) {
      throw OrderCannotBePlacedException.noItems(this.id());
    }
  }

  public void changePaymentMethod(PaymentMethod paymentMethod) {
    Objects.requireNonNull(paymentMethod);
    this.verifyIfOrderChangeable();
    this.setPaymentMethod(paymentMethod);
  }

  public void changeBilling(Billing billing) {
    Objects.requireNonNull(billing);
    this.verifyIfOrderChangeable();
    this.setBilling(billing);
  }

  public void changeShipping(Shipping shipping) {
    Objects.requireNonNull(shipping);
    this.verifyIfOrderChangeable();
    if (shipping.expectedDeliveryDate().isBefore(LocalDate.now())) {
      throw new OrderInvalidShippingDeliveryDateException(this.id(), shipping.expectedDeliveryDate());
    }
    this.setShipping(shipping);
  }

  public void changeItemQuantity(OrderItemId orderItemId, Quantity quantity) {
    Objects.requireNonNull(orderItemId);
    Objects.requireNonNull(quantity);
    this.verifyIfOrderChangeable();
    var orderItem = this.findOrderItem(orderItemId);
    orderItem.changeQuantity(quantity);
    this.recalculateTotalAmount();
    this.recalculateTotalItems();
  }

  public boolean isDraft() {
    return OrderStatus.DRAFT.equals(this.status);
  }

  public boolean isPlaced() {
    return OrderStatus.PLACED.equals(this.status);
  }

  public boolean isCanceled() { return OrderStatus.CANCELED.equals(this.status);}

  public void removeItem(OrderItemId orderItemId) {
    Objects.requireNonNull(orderItemId);
    this.verifyIfOrderChangeable();
    var orderItem = this.findOrderItem(orderItemId);
    this.items.remove(orderItem);
    this.recalculateTotalAmount();
    this.recalculateTotalItems();
  }

  public void markAsReady() {
    this.verifyIfCanChangeToReady();
    this.changeStatus(OrderStatus.READY);
    this.setReadyAt(OffsetDateTime.now());
  }

  private void verifyIfCanChangeToReady() {
    if (!this.status.canChangeTo(OrderStatus.READY)) {
      throw new OrderCannotBeReadyException(this.id(), this.status);
    }
  }

  public void cancel() {
    this.verifyIfCanCancel();
    this.changeStatus(OrderStatus.CANCELED);
    this.setCanceledAt(OffsetDateTime.now());
  }

  private void verifyIfCanCancel() {
    if (!this.status.canChangeTo(OrderStatus.CANCELED)) {
      throw new OrderCannotBeCanceledException(this.id(), this.status);
    }
  }

  public void recalculateTotalAmount() {
    var totalAmount = this.items.stream()
        .map(OrderItem::totalAmount)
        .reduce(Money.ZERO, Money::add);
    var shipping = this.shippingInfo() != null && this.shippingInfo().shippingCost() != null 
        ? this.shippingInfo().shippingCost() : Money.ZERO;
    this.setTotalAmount(totalAmount.add(shipping));
  }

  private OrderItem findOrderItem(OrderItemId orderItemId) {
    return this.items.stream()
        .filter(orderItem -> orderItem.id().equals(orderItemId))
        .findFirst()
        .orElseThrow(() -> new OrderItemNotFoundException(orderItemId));
  }

  private void changeStatus(OrderStatus orderStatus) {
    if (this.status().canNotChangeTo(orderStatus)) {
      throw new OrderStatusCannotBeChangedException(this.id(), this.status(), orderStatus);
    }
    this.setStatus(orderStatus);
  }

  private void recalculateTotalItems() {
    var totalItems = this.items().stream()
        .map(OrderItem::quantity)
        .reduce(Quantity.ZERO, Quantity::add);
    this.setTotalItems(totalItems);
  }

  private void verifyIfOrderChangeable() {
    if (!this.isDraft()) {
      throw new OrderCannotBeEditedException(this.id(), this.status());
    }
  }

  public OrderId id() {
    return id;
  }

  public CustomerId customerId() {
    return customerId;
  }

  public Money totalAmount() {
    return totalAmount;
  }

  public Quantity totalItems() {
    return totalItems;
  }

  public OffsetDateTime placedAt() {
    return placedAt;
  }

  public OffsetDateTime paidAt() {
    return paidAt;
  }

  public OffsetDateTime canceledAt() {
    return canceledAt;
  }

  public OffsetDateTime readyAt() {
    return readyAt;
  }

  public Billing billingInfo() {
    return billing;
  }

  public Shipping shippingInfo() {
    return shipping;
  }

  public OrderStatus status() {
    return status;
  }

  public PaymentMethod paymentMethod() {
    return paymentMethod;
  }

  public Set<OrderItem> items() {
    return Collections.unmodifiableSet(items);
  }

  private void setId(OrderId id) {
    Objects.requireNonNull(id);
    this.id = id;
  }

  private void setCustomerId(CustomerId customerId) {
    Objects.requireNonNull(customerId);
    this.customerId = customerId;
  }

  private void setTotalAmount(Money totalAmount) {
    Objects.requireNonNull(totalAmount);
    this.totalAmount = totalAmount;
  }

  private void setTotalItems(Quantity totalItems) {
    Objects.requireNonNull(totalItems);
    this.totalItems = totalItems;
  }

  private void setPlacedAt(OffsetDateTime placedAt) {
    this.placedAt = placedAt;
  }

  private void setPaidAt(OffsetDateTime paidAt) {
    this.paidAt = paidAt;
  }

  private void setCanceledAt(OffsetDateTime canceledAt) {
    this.canceledAt = canceledAt;
  }

  private void setReadyAt(OffsetDateTime readyAt) {
    this.readyAt = readyAt;
  }

  private void setBilling(Billing billing) {
    this.billing = billing;
  }

  private void setShipping(Shipping shipping) {
    this.shipping = shipping;
  }

  private void setStatus(OrderStatus status) {
    Objects.requireNonNull(status);
    this.status = status;
  }

  private void setPaymentMethod(PaymentMethod paymentMethod) {
    this.paymentMethod = paymentMethod;
  }

  private void setItems(Set<OrderItem> items) {
    Objects.requireNonNull(items);
    this.items = items;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Order order = (Order) o;
    return Objects.equals(id, order.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
