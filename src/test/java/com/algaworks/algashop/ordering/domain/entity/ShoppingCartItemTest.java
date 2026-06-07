package com.algaworks.algashop.ordering.domain.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.algaworks.algashop.ordering.domain.model.entity.ShoppingCartItem;
import com.algaworks.algashop.ordering.domain.model.valueobject.Money;
import com.algaworks.algashop.ordering.domain.model.valueobject.Product;
import com.algaworks.algashop.ordering.domain.model.valueobject.ProductName;
import com.algaworks.algashop.ordering.domain.model.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.ProductId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.ShoppingCartId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.ShoppingCartItemId;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ShoppingCartItemTest {

  @Test
  @DisplayName("Should create brand new shopping cart item")
  void shouldCreateBrandNewShoppingCartItem() {
    ShoppingCartId shoppingCartId = new ShoppingCartId(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"));
    ProductId productId = new ProductId(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"));
    ProductName productName = new ProductName("Test Product");
    Money price = new Money("50.00");
    Quantity quantity = new Quantity(new BigDecimal(2));
    Product product = Product.builder()
        .id(productId)
        .name(productName)
        .price(price)
        .inStock(true)
        .build();

    ShoppingCartItem item = ShoppingCartItem.brandNew(shoppingCartId, product, quantity);

    assertThat(item.id()).isNotNull();
    assertThat(item.shoppingCartId()).isEqualTo(shoppingCartId);
    assertThat(item.product()).isEqualTo(productId);
    assertThat(item.productName()).isEqualTo(productName);
    assertThat(item.price()).isEqualTo(price);
    assertThat(item.quantity()).isEqualTo(quantity);
    assertThat(item.totalAmount()).isEqualTo(new Money("100.00"));
    assertThat(item.available()).isTrue();
  }

  @Test
  @DisplayName("Should throw exception when brand new with null shopping cart id")
  void shouldThrowExceptionWhenBrandNewWithNullShoppingCartId() {
    ProductId productId = new ProductId();
    Product product = Product.builder()
        .id(productId)
        .name(new ProductName("Test"))
        .price(new Money("10.00"))
        .inStock(true)
        .build();
    Quantity quantity = new Quantity(new BigDecimal(1));

    assertThatThrownBy(() -> ShoppingCartItem.brandNew(null, product, quantity))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should throw exception when brand new with null product")
  void shouldThrowExceptionWhenBrandNewWithNullProduct() {
    ShoppingCartId shoppingCartId = new ShoppingCartId();
    Quantity quantity = new Quantity(new BigDecimal(1));

    assertThatThrownBy(() -> ShoppingCartItem.brandNew(shoppingCartId, null, quantity))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should throw exception when brand new with null quantity")
  void shouldThrowExceptionWhenBrandNewWithNullQuantity() {
    ShoppingCartId shoppingCartId = new ShoppingCartId();
    ProductId productId = new ProductId();
    Product product = Product.builder()
        .id(productId)
        .name(new ProductName("Test"))
        .price(new Money("10.00"))
        .inStock(true)
        .build();

    assertThatThrownBy(() -> ShoppingCartItem.brandNew(shoppingCartId, product, null))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should create existing shopping cart item using builder")
  void shouldCreateExistingShoppingCartItemUsingBuilder() {
    ShoppingCartItemId id = new ShoppingCartItemId(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"));
    ShoppingCartId shoppingCartId = new ShoppingCartId(UUID.fromString("660e8400-e29b-41d4-a716-446655440001"));
    ProductId product = new ProductId(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"));
    ProductName productName = new ProductName("Product Name");
    Money price = new Money("50.00");
    Quantity quantity = new Quantity(new BigDecimal(2));
    Money totalAmount = new Money("100.00");
    Boolean available = true;

    ShoppingCartItem item = ShoppingCartItem.existingShoppingCartItemBuilder()
        .id(id)
        .shoppingCartId(shoppingCartId)
        .product(product)
        .productName(productName)
        .price(price)
        .quantity(quantity)
        .totalAmount(totalAmount)
        .available(available)
        .build();

    assertThat(item.id()).isEqualTo(id);
    assertThat(item.shoppingCartId()).isEqualTo(shoppingCartId);
    assertThat(item.product()).isEqualTo(product);
    assertThat(item.productName()).isEqualTo(productName);
    assertThat(item.price()).isEqualTo(price);
    assertThat(item.quantity()).isEqualTo(quantity);
    assertThat(item.totalAmount()).isEqualTo(totalAmount);
    assertThat(item.available()).isEqualTo(available);
  }

  @Test
  @DisplayName("Should throw exception when using builder with null id")
  void shouldThrowExceptionWhenUsingBuilderWithNullId() {
    assertThatThrownBy(() -> ShoppingCartItem.existingShoppingCartItemBuilder()
        .id(null)
        .shoppingCartId(new ShoppingCartId())
        .product(new ProductId())
        .productName(new ProductName("Product"))
        .price(new Money("10.00"))
        .quantity(new Quantity(new BigDecimal(1)))
        .totalAmount(Money.ZERO)
        .available(true)
        .build())
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should recalculate totals")
  void shouldRecalculateTotals() {
    ShoppingCartItem item = ShoppingCartItem.existingShoppingCartItemBuilder()
        .id(new ShoppingCartItemId())
        .shoppingCartId(new ShoppingCartId())
        .product(new ProductId())
        .productName(new ProductName("Product"))
        .price(new Money("50.00"))
        .quantity(new Quantity(new BigDecimal(3)))
        .totalAmount(Money.ZERO)
        .available(true)
        .build();

    item.recalculateTotals();

    assertThat(item.totalAmount()).isEqualTo(new Money("150.00"));
  }

  @Test
  @DisplayName("Should be equal when same id")
  void shouldBeEqualWhenSameId() {
    ShoppingCartItemId id = new ShoppingCartItemId(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"));
    ShoppingCartItem item1 = ShoppingCartItem.existingShoppingCartItemBuilder()
        .id(id)
        .shoppingCartId(new ShoppingCartId())
        .product(new ProductId())
        .productName(new ProductName("Product"))
        .price(new Money("10.00"))
        .quantity(new Quantity(new BigDecimal(1)))
        .totalAmount(Money.ZERO)
        .available(true)
        .build();
    ShoppingCartItem item2 = ShoppingCartItem.existingShoppingCartItemBuilder()
        .id(id)
        .shoppingCartId(new ShoppingCartId())
        .product(new ProductId())
        .productName(new ProductName("Product"))
        .price(new Money("10.00"))
        .quantity(new Quantity(new BigDecimal(1)))
        .totalAmount(Money.ZERO)
        .available(true)
        .build();

    assertThat(item1).isEqualTo(item2);
    assertThat(item1.hashCode()).isEqualTo(item2.hashCode());
  }

  @Test
  @DisplayName("Should not be equal when different id")
  void shouldNotBeEqualWhenDifferentId() {
    ShoppingCartItem item1 = ShoppingCartItem.existingShoppingCartItemBuilder()
        .id(new ShoppingCartItemId(UUID.fromString("550e8400-e29b-41d4-a716-446655440000")))
        .shoppingCartId(new ShoppingCartId())
        .product(new ProductId())
        .productName(new ProductName("Product"))
        .price(new Money("10.00"))
        .quantity(new Quantity(new BigDecimal(1)))
        .totalAmount(Money.ZERO)
        .available(true)
        .build();
    ShoppingCartItem item2 = ShoppingCartItem.existingShoppingCartItemBuilder()
        .id(new ShoppingCartItemId(UUID.fromString("660e8400-e29b-41d4-a716-446655440001")))
        .shoppingCartId(new ShoppingCartId())
        .product(new ProductId())
        .productName(new ProductName("Product"))
        .price(new Money("10.00"))
        .quantity(new Quantity(new BigDecimal(1)))
        .totalAmount(Money.ZERO)
        .available(true)
        .build();

    assertThat(item1).isNotEqualTo(item2);
  }

  @Test
  @DisplayName("Should not be equal to null")
  void shouldNotBeEqualToNull() {
    ShoppingCartItem item = ShoppingCartItem.existingShoppingCartItemBuilder()
        .id(new ShoppingCartItemId())
        .shoppingCartId(new ShoppingCartId())
        .product(new ProductId())
        .productName(new ProductName("Product"))
        .price(new Money("10.00"))
        .quantity(new Quantity(new BigDecimal(1)))
        .totalAmount(Money.ZERO)
        .available(true)
        .build();

    assertThat(item).isNotEqualTo(null);
  }

  @Test
  @DisplayName("Should not be equal to different class")
  void shouldNotBeEqualToDifferentClass() {
    ShoppingCartItem item = ShoppingCartItem.existingShoppingCartItemBuilder()
        .id(new ShoppingCartItemId())
        .shoppingCartId(new ShoppingCartId())
        .product(new ProductId())
        .productName(new ProductName("Product"))
        .price(new Money("10.00"))
        .quantity(new Quantity(new BigDecimal(1)))
        .totalAmount(Money.ZERO)
        .available(true)
        .build();

    assertThat(item).isNotEqualTo("not a shopping cart item");
  }

  @Test
  @DisplayName("Should return id")
  void shouldReturnId() {
    ShoppingCartItemId id = new ShoppingCartItemId(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"));
    ShoppingCartItem item = ShoppingCartItem.existingShoppingCartItemBuilder()
        .id(id)
        .shoppingCartId(new ShoppingCartId())
        .product(new ProductId())
        .productName(new ProductName("Product"))
        .price(new Money("10.00"))
        .quantity(new Quantity(new BigDecimal(1)))
        .totalAmount(Money.ZERO)
        .available(true)
        .build();

    assertThat(item.id()).isEqualTo(id);
  }

  @Test
  @DisplayName("Should return shopping cart id")
  void shouldReturnShoppingCartId() {
    ShoppingCartId shoppingCartId = new ShoppingCartId(UUID.fromString("660e8400-e29b-41d4-a716-446655440001"));
    ShoppingCartItem item = ShoppingCartItem.existingShoppingCartItemBuilder()
        .id(new ShoppingCartItemId())
        .shoppingCartId(shoppingCartId)
        .product(new ProductId())
        .productName(new ProductName("Product"))
        .price(new Money("10.00"))
        .quantity(new Quantity(new BigDecimal(1)))
        .totalAmount(Money.ZERO)
        .available(true)
        .build();

    assertThat(item.shoppingCartId()).isEqualTo(shoppingCartId);
  }

  @Test
  @DisplayName("Should return product id")
  void shouldReturnProductId() {
    ProductId productId = new ProductId(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"));
    ShoppingCartItem item = ShoppingCartItem.existingShoppingCartItemBuilder()
        .id(new ShoppingCartItemId())
        .shoppingCartId(new ShoppingCartId())
        .product(productId)
        .productName(new ProductName("Product"))
        .price(new Money("10.00"))
        .quantity(new Quantity(new BigDecimal(1)))
        .totalAmount(Money.ZERO)
        .available(true)
        .build();

    assertThat(item.product()).isEqualTo(productId);
  }

  @Test
  @DisplayName("Should return product name")
  void shouldReturnProductName() {
    ProductName productName = new ProductName("Test Product");
    ShoppingCartItem item = ShoppingCartItem.existingShoppingCartItemBuilder()
        .id(new ShoppingCartItemId())
        .shoppingCartId(new ShoppingCartId())
        .product(new ProductId())
        .productName(productName)
        .price(new Money("10.00"))
        .quantity(new Quantity(new BigDecimal(1)))
        .totalAmount(Money.ZERO)
        .available(true)
        .build();

    assertThat(item.productName()).isEqualTo(productName);
  }

  @Test
  @DisplayName("Should return price")
  void shouldReturnPrice() {
    Money price = new Money("75.00");
    ShoppingCartItem item = ShoppingCartItem.existingShoppingCartItemBuilder()
        .id(new ShoppingCartItemId())
        .shoppingCartId(new ShoppingCartId())
        .product(new ProductId())
        .productName(new ProductName("Product"))
        .price(price)
        .quantity(new Quantity(new BigDecimal(1)))
        .totalAmount(Money.ZERO)
        .available(true)
        .build();

    assertThat(item.price()).isEqualTo(price);
  }

  @Test
  @DisplayName("Should return quantity")
  void shouldReturnQuantity() {
    Quantity quantity = new Quantity(new BigDecimal(3));
    ShoppingCartItem item = ShoppingCartItem.existingShoppingCartItemBuilder()
        .id(new ShoppingCartItemId())
        .shoppingCartId(new ShoppingCartId())
        .product(new ProductId())
        .productName(new ProductName("Product"))
        .price(new Money("10.00"))
        .quantity(quantity)
        .totalAmount(Money.ZERO)
        .available(true)
        .build();

    assertThat(item.quantity()).isEqualTo(quantity);
  }

  @Test
  @DisplayName("Should return total amount")
  void shouldReturnTotalAmount() {
    Money totalAmount = new Money("225.00");
    ShoppingCartItem item = ShoppingCartItem.existingShoppingCartItemBuilder()
        .id(new ShoppingCartItemId())
        .shoppingCartId(new ShoppingCartId())
        .product(new ProductId())
        .productName(new ProductName("Product"))
        .price(new Money("10.00"))
        .quantity(new Quantity(new BigDecimal(1)))
        .totalAmount(totalAmount)
        .available(true)
        .build();

    assertThat(item.totalAmount()).isEqualTo(totalAmount);
  }

  @Test
  @DisplayName("Should return available")
  void shouldReturnAvailable() {
    ShoppingCartItem item = ShoppingCartItem.existingShoppingCartItemBuilder()
        .id(new ShoppingCartItemId())
        .shoppingCartId(new ShoppingCartId())
        .product(new ProductId())
        .productName(new ProductName("Product"))
        .price(new Money("10.00"))
        .quantity(new Quantity(new BigDecimal(1)))
        .totalAmount(Money.ZERO)
        .available(false)
        .build();

    assertThat(item.available()).isFalse();
  }
}
