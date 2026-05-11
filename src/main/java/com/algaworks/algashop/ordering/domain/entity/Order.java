package com.algaworks.algashop.ordering.domain.entity;

import com.algaworks.algashop.ordering.domain.valueobject.BillingInfo;
import com.algaworks.algashop.ordering.domain.valueobject.Money;
import com.algaworks.algashop.ordering.domain.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.valueobject.ShippingInfo;
import com.algaworks.algashop.ordering.domain.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.valueobject.id.OrderId;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Set;

public class Order {

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

  public Order(OrderId id, CustomerId customerId, Money totalAmount, Quantity totalItems, OffsetDateTime placedAt,
      OffsetDateTime paidAt, OffsetDateTime canceledAt, OffsetDateTime readyAt, BillingInfo billingInfo,
      ShippingInfo shippingInfo, OrderStatus status, PaymentMethod paymentMethod, Money shippingCost,
      LocalDate expectedDeliveryDate, Set<OrderItem> items) {
    setId(id);
    setCustomerId(customerId);
    setTotalAmount(totalAmount);
    setTotalItems(totalItems);
    setPlacedAt(placedAt);
    setPaidAt(paidAt);
    setCanceledAt(canceledAt);
    setReadyAt(readyAt);
    setBillingInfo(billingInfo);
    setShippingInfo(shippingInfo);
    setStatus(status);
    setPaymentMethod(paymentMethod);
    setShippingCost(shippingCost);
    setExpectedDeliveryDate(expectedDeliveryDate);
    setItems(items);
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

  public BillingInfo billingInfo() {
    return billingInfo;
  }

  public ShippingInfo shippingInfo() {
    return shippingInfo;
  }

  public OrderStatus status() {
    return status;
  }

  public PaymentMethod paymentMethod() {
    return paymentMethod;
  }

  public Money shippingCost() {
    return shippingCost;
  }

  public LocalDate expectedDeliveryDate() {
    return expectedDeliveryDate;
  }

  public Set<OrderItem> items() {
    return items;
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

  private void setBillingInfo(BillingInfo billingInfo) {
    this.billingInfo = billingInfo;
  }

  private void setShippingInfo(ShippingInfo shippingInfo) {
    this.shippingInfo = shippingInfo;
  }

  private void setStatus(OrderStatus status) {
    Objects.requireNonNull(status);
    this.status = status;
  }

  private void setPaymentMethod(PaymentMethod paymentMethod) {
    this.paymentMethod = paymentMethod;
  }

  private void setShippingCost(Money shippingCost) {
    this.shippingCost = shippingCost;
  }

  private void setExpectedDeliveryDate(LocalDate expectedDeliveryDate) {
    this.expectedDeliveryDate = expectedDeliveryDate;
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
