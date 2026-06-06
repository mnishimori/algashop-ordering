package com.algaworks.algashop.ordering.domain.entity;

import com.algaworks.algashop.ordering.domain.exception.ShoppingCartItemNotFoundException;
import com.algaworks.algashop.ordering.domain.valueobject.Money;
import com.algaworks.algashop.ordering.domain.valueobject.Product;
import com.algaworks.algashop.ordering.domain.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.valueobject.id.ProductId;
import com.algaworks.algashop.ordering.domain.valueobject.id.ShoppingCartId;
import com.algaworks.algashop.ordering.domain.valueobject.id.ShoppingCartItemId;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import lombok.Builder;

public class ShoppingCart {

  private ShoppingCartId shoppingCartId;
  private CustomerId customerId;
  private Money totalAmount;
  private Quantity totalItems;
  private OffsetDateTime createdAt;
  private Set<ShoppingCartItem> items;

  @Builder(builderClassName = "ShoppingCartBuilder", builderMethodName = "existingShoppingCartBuilder")
  public ShoppingCart(ShoppingCartId shoppingCartId, CustomerId customerId, Money totalAmount, Quantity totalItems,
      OffsetDateTime createdAt, Set<ShoppingCartItem> items) {
    setShoppingCartId(shoppingCartId);
    setCustomerId(customerId);
    setTotalAmount(totalAmount);
    setTotalItems(totalItems);
    setCreatedAt(createdAt);
    setItems(items);
  }

  public void addItem(Product product, Quantity quantity) {
    Objects.requireNonNull(product);
    Objects.requireNonNull(quantity);
    product.changeOutStock();

    var existingItem = findItemByProductId(product.id());
    if (existingItem != null) {
      updateExistingItem(existingItem, product, quantity);
    } else {
      addNewItem(product, quantity);
    }

    recalculateTotals();
  }

  private void updateExistingItem(ShoppingCartItem existingItem, Product product, Quantity quantity) {
    var newQuantity = existingItem.quantity().add(quantity);
    existingItem.updateQuantity(newQuantity);
    existingItem.updatePriceAndAvailability(product.price(), product.inStock());
  }

  private void addNewItem(Product product, Quantity quantity) {
    var shoppingCartItem = ShoppingCartItem.draftShoppingCartItemBuilder()
        .shoppingCartId(this.shoppingCartId)
        .product(product)
        .quantity(quantity)
        .build();
    this.items.add(shoppingCartItem);
  }

  private void recalculateTotals() {
    recalculateTotalItems();
    recalculateTotalAmount();
  }

  public void removeItem(ShoppingCartItemId shoppingCartItemId) {
    Objects.requireNonNull(shoppingCartItemId);
    var item = findItemById(shoppingCartItemId);
    this.items.remove(item);
    recalculateTotals();
  }

  public void setShoppingCartId(ShoppingCartId shoppingCartId) {
    Objects.requireNonNull(shoppingCartId);
    this.shoppingCartId = shoppingCartId;
  }

  public void refreshItem(Product product) {
    Objects.requireNonNull(product);
    var existingItem = findItemByProductId(product.id());
    if (existingItem == null) {
      throw new ShoppingCartItemNotFoundException(product.id().value());
    }
    existingItem.updatePriceAndAvailability(product.price(), product.inStock());
    existingItem.updateProductName(product.name());
    recalculateTotals();
  }

  public void changeQuantityitem(ShoppingCartItemId shoppingCartItemId, Quantity quantity) {
    Objects.requireNonNull(shoppingCartItemId);
    Objects.requireNonNull(quantity);
    if (quantity.equals(Quantity.ZERO) || quantity.value().intValue() <= 0) {
      throw new IllegalArgumentException("Quantity must be greater than 0");
    }
    var item = findItemById(shoppingCartItemId);
    item.updateQuantity(quantity);
    recalculateTotals();
  }

  public boolean containsUnavailableItems() {
    return this.items.stream()
        .anyMatch(item -> !item.available());
  }

  public boolean isEmpty() {
    return this.items.isEmpty();
  }

  public void empty() {
    this.items.clear();
    recalculateTotals();
  }

  public void setCustomerId(CustomerId customerId) {
    Objects.requireNonNull(customerId);
    this.customerId = customerId;
  }

  public void setTotalAmount(Money totalAmount) {
    Objects.requireNonNull(totalAmount);
    this.totalAmount = totalAmount;
  }

  public void setTotalItems(Quantity totalItems) {
    Objects.requireNonNull(totalItems);
    this.totalItems = totalItems;
  }

  public void setCreatedAt(OffsetDateTime createdAt) {
    Objects.requireNonNull(createdAt);
    this.createdAt = createdAt;
  }

  public void setItems(Set<ShoppingCartItem> items) {
    Objects.requireNonNull(items);
    this.items = items;
  }

  public ShoppingCartId shoppingCartId() {
    return shoppingCartId;
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

  public OffsetDateTime createdAt() {
    return createdAt;
  }

  public Set<ShoppingCartItem> items() {
    return Collections.unmodifiableSet(items);
  }

  private ShoppingCartItem findItemByProductId(ProductId productId) {
    return this.items.stream()
        .filter(item -> item.product().equals(productId))
        .findFirst()
        .orElse(null);
  }

  private ShoppingCartItem findItemById(ShoppingCartItemId shoppingCartItemId) {
    return this.items.stream()
        .filter(item -> item.id().equals(shoppingCartItemId))
        .findFirst()
        .orElseThrow(() -> new ShoppingCartItemNotFoundException(shoppingCartItemId.value()));
  }

  private void recalculateTotalItems() {
    var totalQuantity = this.items.stream()
        .map(ShoppingCartItem::quantity)
        .reduce(Quantity.ZERO, Quantity::add);
    this.setTotalItems(totalQuantity);
  }

  private void recalculateTotalAmount() {
    var totalAmount = this.items.stream()
        .map(ShoppingCartItem::totalAmount)
        .reduce(Money.ZERO, Money::add);
    this.setTotalAmount(totalAmount);
  }
}
