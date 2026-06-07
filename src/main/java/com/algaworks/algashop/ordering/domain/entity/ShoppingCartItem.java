package com.algaworks.algashop.ordering.domain.entity;

import com.algaworks.algashop.ordering.domain.exception.ShoppingCartItemIncompatibleProductException;
import com.algaworks.algashop.ordering.domain.valueobject.Money;
import com.algaworks.algashop.ordering.domain.valueobject.Product;
import com.algaworks.algashop.ordering.domain.valueobject.ProductName;
import com.algaworks.algashop.ordering.domain.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.valueobject.id.ProductId;
import com.algaworks.algashop.ordering.domain.valueobject.id.ShoppingCartId;
import com.algaworks.algashop.ordering.domain.valueobject.id.ShoppingCartItemId;
import java.util.Objects;
import lombok.Builder;

public class ShoppingCartItem {

  private ShoppingCartItemId id;
  private ShoppingCartId shoppingCartId;
  private ProductId product;
  private ProductName productName;
  private Money price;
  private Quantity quantity;
  private Money totalAmount;
  private Boolean available;

  @Builder(builderClassName = "ShoppingCartItemBuilder", builderMethodName = "existingShoppingCartItemBuilder")
  public ShoppingCartItem(ShoppingCartItemId id, ShoppingCartId shoppingCartId, ProductId product,
      ProductName productName, Money price, Quantity quantity, Money totalAmount, Boolean available) {
    setId(id);
    setShoppingCartId(shoppingCartId);
    setProduct(product);
    setProductName(productName);
    setPrice(price);
    setQuantity(quantity);
    setTotalAmount(totalAmount);
    setAvailable(available);
  }

  public static ShoppingCartItem brandNew(ShoppingCartId shoppingCartId, Product product,
      Quantity quantity) {
    Objects.requireNonNull(shoppingCartId);
    Objects.requireNonNull(product);
    Objects.requireNonNull(quantity);
    var shoppingCartItem = new ShoppingCartItem(new ShoppingCartItemId(), shoppingCartId, product.id(),
        product.name(), product.price(), quantity, Money.ZERO, product.inStock());
    shoppingCartItem.recalculateTotalItemAmount();
    return shoppingCartItem;
  }

  public ShoppingCartItemId id() {
    return id;
  }

  public ShoppingCartId shoppingCartId() {
    return shoppingCartId;
  }

  public ProductId product() {
    return product;
  }

  public ProductName productName() {
    return productName;
  }

  public Money price() {
    return price;
  }

  public Quantity quantity() {
    return quantity;
  }

  public Money totalAmount() {
    return totalAmount;
  }

  public Boolean available() {
    return available;
  }

  public void recalculateTotals() {
    this.totalAmount = this.price().multiply(this.quantity());
  }

  private void recalculateTotalItemAmount() {
    this.totalAmount = this.price().multiply(this.quantity());
  }

  void changeQuantity(Quantity quantity) {
    Objects.requireNonNull(quantity);
    if (quantity.value().compareTo(java.math.BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Quantity must be greater than 0");
    }
    this.setQuantity(quantity);
    this.recalculateTotalItemAmount();
  }

  void updateQuantity(Quantity quantity) {
    Objects.requireNonNull(quantity);
    this.setQuantity(quantity);
    this.recalculateTotalItemAmount();
  }

  void refresh(Product product) {
    Objects.requireNonNull(product);
    if (!this.product.equals(product.id())) {
      throw new ShoppingCartItemIncompatibleProductException("Product ID mismatch");
    }
    this.setPrice(product.price());
    this.setProductName(product.name());
    this.setAvailable(product.inStock());
    this.recalculateTotalItemAmount();
  }

  void updatePriceAndAvailability(Money price, Boolean available) {
    Objects.requireNonNull(price);
    Objects.requireNonNull(available);
    this.setPrice(price);
    this.setAvailable(available);
    this.recalculateTotalItemAmount();
  }

  void updateProductName(ProductName productName) {
    Objects.requireNonNull(productName);
    this.setProductName(productName);
  }

  private void setId(ShoppingCartItemId id) {
    Objects.requireNonNull(id);
    this.id = id;
  }

  private void setShoppingCartId(ShoppingCartId shoppingCartId) {
    Objects.requireNonNull(shoppingCartId);
    this.shoppingCartId = shoppingCartId;
  }

  private void setProduct(ProductId product) {
    Objects.requireNonNull(product);
    this.product = product;
  }

  private void setProductName(ProductName productName) {
    Objects.requireNonNull(productName);
    this.productName = productName;
  }

  private void setPrice(Money price) {
    Objects.requireNonNull(price);
    this.price = price;
  }

  private void setQuantity(Quantity quantity) {
    Objects.requireNonNull(quantity);
    this.quantity = quantity;
  }

  private void setTotalAmount(Money totalAmount) {
    Objects.requireNonNull(totalAmount);
    this.totalAmount = totalAmount;
  }

  private void setAvailable(Boolean available) {
    Objects.requireNonNull(available);
    this.available = available;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ShoppingCartItem shoppingCartItem = (ShoppingCartItem) o;
    return Objects.equals(id, shoppingCartItem.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
