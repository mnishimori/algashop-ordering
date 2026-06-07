package com.algaworks.algashop.ordering.domain.entity;

import com.algaworks.algashop.ordering.domain.model.entity.OrderItem;
import com.algaworks.algashop.ordering.domain.model.valueobject.Money;
import com.algaworks.algashop.ordering.domain.model.valueobject.ProductName;
import com.algaworks.algashop.ordering.domain.model.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderItemId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.ProductId;
import io.hypersistence.tsid.TSID;
import java.math.BigDecimal;
import java.util.UUID;

public class OrderItemTestDataBuilder {

  private OrderItemId id;
  private OrderId orderId;
  private ProductId productId;
  private ProductName productName;
  private Money price;
  private Quantity quantity;
  private Money totalAmount;

  private OrderItemTestDataBuilder() {
    this.id = new OrderItemId(TSID.from(123456789L));
    this.orderId = new OrderId(TSID.from(987654321L));
    this.productId = new ProductId(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"));
    this.productName = new ProductName("Product Name");
    this.price = new Money("50.00");
    this.quantity = new Quantity(new BigDecimal(2));
    this.totalAmount = Money.ZERO;
  }

  public static OrderItemTestDataBuilder anOrderItem() {
    return new OrderItemTestDataBuilder();
  }

  public OrderItemTestDataBuilder id(OrderItemId id) {
    this.id = id;
    return this;
  }

  public OrderItemTestDataBuilder orderId(OrderId orderId) {
    this.orderId = orderId;
    return this;
  }

  public OrderItemTestDataBuilder productId(ProductId productId) {
    this.productId = productId;
    return this;
  }

  public OrderItemTestDataBuilder productName(ProductName productName) {
    this.productName = productName;
    return this;
  }

  public OrderItemTestDataBuilder price(Money price) {
    this.price = price;
    return this;
  }

  public OrderItemTestDataBuilder quantity(Quantity quantity) {
    this.quantity = quantity;
    return this;
  }

  public OrderItemTestDataBuilder totalAmount(Money totalAmount) {
    this.totalAmount = totalAmount;
    return this;
  }

  public OrderItem build() {
    return new OrderItem(id, orderId, productId, productName, price, quantity, totalAmount);
  }
}
