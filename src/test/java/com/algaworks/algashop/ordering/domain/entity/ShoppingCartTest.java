package com.algaworks.algashop.ordering.domain.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.algaworks.algashop.ordering.domain.model.exception.ProductOutOfStockException;
import com.algaworks.algashop.ordering.domain.model.exception.ShoppingCartItemNotFoundException;
import com.algaworks.algashop.ordering.domain.model.entity.ShoppingCart;
import com.algaworks.algashop.ordering.domain.model.entity.ShoppingCartItem;
import com.algaworks.algashop.ordering.domain.model.valueobject.Money;
import com.algaworks.algashop.ordering.domain.model.valueobject.Product;
import com.algaworks.algashop.ordering.domain.model.valueobject.ProductName;
import com.algaworks.algashop.ordering.domain.model.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.ShoppingCartId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.ShoppingCartItemId;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("ShoppingCart")
class ShoppingCartTest {

  private ShoppingCart cart;
  private Product product;
  private Quantity quantity;

  @BeforeEach
  void setUp() {
    cart = ShoppingCartTestDataBuilder.aShoppingCart().build();
    product = ProductTestDataBuilder.createProduct()
        .name(new ProductName("Notebook Pro"))
        .price(new Money("100.00"))
        .inStock(true)
        .build();
    quantity = new Quantity(new BigDecimal("2"));
  }

  @Nested
  @DisplayName("newShoppingCart")
  class NewShoppingCart {

    @Test
    @DisplayName("Should create a new cart with Money.ZERO totalAmount")
    void shouldCreateNewCartWithZeroTotalAmount() {
      ShoppingCart newCart = ShoppingCart.newShoppingCart(
          new CustomerId(UUID.randomUUID()));

      assertThat(newCart.totalAmount()).isEqualTo(Money.ZERO);
    }

    @Test
    @DisplayName("Should create a new cart with Quantity.ZERO totalItems")
    void shouldCreateNewCartWithZeroTotalItems() {
      ShoppingCart newCart = ShoppingCart.newShoppingCart(
          new CustomerId(UUID.randomUUID()));

      assertThat(newCart.totalItems()).isEqualTo(Quantity.ZERO);
    }

    @Test
    @DisplayName("Should create a new cart with empty items collection")
    void shouldCreateNewCartWithEmptyItems() {
      ShoppingCart newCart = ShoppingCart.newShoppingCart(
          new CustomerId(UUID.randomUUID()));

      assertThat(newCart.items()).isEmpty();
    }

    @Test
    @DisplayName("Should create a new cart with non-null id")
    void shouldCreateNewCartWithNonNullId() {
      ShoppingCart newCart = ShoppingCart.newShoppingCart(
          new CustomerId(UUID.randomUUID()));

      assertThat(newCart.shoppingCartId()).isNotNull();
    }

    @Test
    @DisplayName("Should create a new cart with non-null createdAt")
    void shouldCreateNewCartWithNonNullCreatedAt() {
      ShoppingCart newCart = ShoppingCart.newShoppingCart(
          new CustomerId(UUID.randomUUID()));

      assertThat(newCart.createdAt()).isNotNull();
    }
  }

  @Nested
  @DisplayName("addItem")
  class AddItem {

    @Nested
    @DisplayName("Success scenarios")
    class SuccessScenarios {

      @Test
      @DisplayName("Should add a new item when product is not yet in the cart")
      void shouldAddNewItemWhenProductNotInCart() {
        cart.addItem(product, quantity);

        assertThat(cart.items()).hasSize(1);
      }

      @Test
      @DisplayName("Should set item product data correctly")
      void shouldSetItemProductDataCorrectly() {
        cart.addItem(product, quantity);

        ShoppingCartItem item = cart.items().iterator().next();
        assertThat(item.product()).isEqualTo(product.id());
        assertThat(item.productName()).isEqualTo(product.name());
        assertThat(item.price()).isEqualTo(product.price());
        assertThat(item.quantity()).isEqualTo(quantity);
      }

      @Test
      @DisplayName("Should generate a non-null id for the added item")
      void shouldGenerateNonNullItemId() {
        cart.addItem(product, quantity);

        ShoppingCartItem item = cart.items().iterator().next();
        assertThat(item.id()).isNotNull();
      }

      @Test
      @DisplayName("Should bind the item to the cart id")
      void shouldBindItemToCartId() {
        cart.addItem(product, quantity);

        ShoppingCartItem item = cart.items().iterator().next();
        assertThat(item.shoppingCartId()).isEqualTo(cart.shoppingCartId());
      }

      @Test
      @DisplayName("Should recalculate totalItems after adding an item")
      void shouldRecalculateTotalItemsAfterAdding() {
        cart.addItem(product, quantity);

        assertThat(cart.totalItems()).isEqualTo(quantity);
      }

      @Test
      @DisplayName("Should recalculate totalAmount after adding an item")
      void shouldRecalculateTotalAmountAfterAdding() {
        cart.addItem(product, quantity);

        Money expectedTotal = product.price().multiply(quantity);
        assertThat(cart.totalAmount()).isEqualTo(expectedTotal);
      }

      @Test
      @DisplayName("Should accumulate totals when adding multiple different products")
      void shouldAccumulateTotalsWithMultipleProducts() {
        Product product2 = ProductTestDataBuilder.createProduct()
            .name(new ProductName("Mouse"))
            .price(new Money("50.00"))
            .inStock(true)
            .build();
        Quantity quantity2 = new Quantity(new BigDecimal("3"));

        cart.addItem(product, quantity);
        cart.addItem(product2, quantity2);

        assertThat(cart.items()).hasSize(2);
        assertThat(cart.totalItems()).isEqualTo(new Quantity(new BigDecimal("5")));
        assertThat(cart.totalAmount()).isEqualTo(new Money("350.00"));
      }

      @Test
      @DisplayName("Should increment quantity when adding an already existing product")
      void shouldIncrementQuantityWhenProductAlreadyInCart() {
        Quantity additionalQuantity = new Quantity(new BigDecimal("3"));

        cart.addItem(product, quantity);
        cart.addItem(product, additionalQuantity);

        assertThat(cart.items()).hasSize(1);
        ShoppingCartItem item = cart.items().iterator().next();
        assertThat(item.quantity()).isEqualTo(new Quantity(new BigDecimal("5")));
      }

      @Test
      @DisplayName("Should recalculate totalItems when re-adding the same product")
      void shouldRecalculateTotalItemsOnDuplicateProduct() {
        Quantity additionalQuantity = new Quantity(new BigDecimal("3"));

        cart.addItem(product, quantity);
        cart.addItem(product, additionalQuantity);

        assertThat(cart.totalItems()).isEqualTo(new Quantity(new BigDecimal("5")));
      }

      @Test
      @DisplayName("Should update price when re-adding the same product with a new price")
      void shouldUpdatePriceOnDuplicateProduct() {
        cart.addItem(product, quantity);

        Product updatedProduct = ProductTestDataBuilder.createProduct()
            .id(product.id())
            .name(product.name())
            .price(new Money("120.00"))
            .inStock(true)
            .build();

        cart.addItem(updatedProduct, new Quantity(BigDecimal.ONE));

        ShoppingCartItem item = cart.items().iterator().next();
        assertThat(item.price()).isEqualTo(new Money("120.00"));
      }
    }

    @Nested
    @DisplayName("Failure scenarios")
    class FailureScenarios {

      @Test
      @DisplayName("Should throw NullPointerException when product is null")
      void shouldThrowWhenProductIsNull() {
        assertThatThrownBy(() -> cart.addItem(null, quantity))
            .isInstanceOf(NullPointerException.class);
      }

      @Test
      @DisplayName("Should throw NullPointerException when quantity is null")
      void shouldThrowWhenQuantityIsNull() {
        assertThatThrownBy(() -> cart.addItem(product, null))
            .isInstanceOf(NullPointerException.class);
      }

      @Test
      @DisplayName("Should throw ProductOutOfStockException when product is out of stock")
      void shouldThrowWhenProductIsOutOfStock() {
        Product outOfStockProduct = ProductTestDataBuilder.createProduct()
            .name(new ProductName("Notebook Pro"))
            .price(new Money("100.00"))
            .inStock(false)
            .build();

        assertThatThrownBy(() -> cart.addItem(outOfStockProduct, quantity))
            .isInstanceOf(ProductOutOfStockException.class);
      }

      @Test
      @DisplayName("Should not add item when product is out of stock")
      void shouldNotAddItemWhenProductIsOutOfStock() {
        Product outOfStockProduct = ProductTestDataBuilder.createProduct()
            .name(new ProductName("Notebook Pro"))
            .price(new Money("100.00"))
            .inStock(false)
            .build();

        try {
          cart.addItem(outOfStockProduct, quantity);
        } catch (ProductOutOfStockException ignored) {
        }

        assertThat(cart.items()).isEmpty();
      }

      @Test
      @DisplayName("Should not change totalItems when product is null")
      void shouldNotChangeTotalItemsWhenProductIsNull() {
        try {
          cart.addItem(null, quantity);
        } catch (NullPointerException ignored) {
        }

        assertThat(cart.totalItems()).isEqualTo(Quantity.ZERO);
      }
    }
  }

  @Nested
  @DisplayName("removeItem")
  class RemoveItem {

    @Nested
    @DisplayName("Success scenarios")
    class SuccessScenarios {

      @Test
      @DisplayName("Should remove an existing item by id")
      void shouldRemoveExistingItem() {
        cart.addItem(product, quantity);
        ShoppingCartItemId itemId = cart.items().iterator().next().id();

        cart.removeItem(itemId);

        assertThat(cart.items()).isEmpty();
      }

      @Test
      @DisplayName("Should recalculate totalItems after removing an item")
      void shouldRecalculateTotalItemsAfterRemoving() {
        cart.addItem(product, quantity);
        ShoppingCartItemId itemId = cart.items().iterator().next().id();

        cart.removeItem(itemId);

        assertThat(cart.totalItems()).isEqualTo(Quantity.ZERO);
      }

      @Test
      @DisplayName("Should recalculate totalAmount after removing an item")
      void shouldRecalculateTotalAmountAfterRemoving() {
        cart.addItem(product, quantity);
        ShoppingCartItemId itemId = cart.items().iterator().next().id();

        cart.removeItem(itemId);

        assertThat(cart.totalAmount()).isEqualTo(Money.ZERO);
      }

      @Test
      @DisplayName("Should keep other items when removing one of multiple items")
      void shouldKeepOtherItemsWhenRemovingOne() {
        Product product2 = ProductTestDataBuilder.createProduct()
            .name(new ProductName("Mouse"))
            .price(new Money("50.00"))
            .inStock(true)
            .build();

        cart.addItem(product, quantity);
        cart.addItem(product2, new Quantity(BigDecimal.ONE));

        ShoppingCartItemId firstItemId = cart.items().stream()
            .filter(i -> i.product().equals(product.id()))
            .findFirst()
            .orElseThrow()
            .id();

        cart.removeItem(firstItemId);

        assertThat(cart.items()).hasSize(1);
        assertThat(cart.items().iterator().next().product()).isEqualTo(product2.id());
      }
    }

    @Nested
    @DisplayName("Failure scenarios")
    class FailureScenarios {

      @Test
      @DisplayName("Should throw NullPointerException when itemId is null")
      void shouldThrowWhenItemIdIsNull() {
        assertThatThrownBy(() -> cart.removeItem(null))
            .isInstanceOf(NullPointerException.class);
      }

      @Test
      @DisplayName("Should throw ShoppingCartItemNotFoundException when item does not exist")
      void shouldThrowWhenItemNotFound() {
        assertThatThrownBy(() -> cart.removeItem(new ShoppingCartItemId()))
            .isInstanceOf(ShoppingCartItemNotFoundException.class);
      }
    }
  }

  @Nested
  @DisplayName("changeQuantityitem")
  class ChangeQuantityItem {

    @Nested
    @DisplayName("Success scenarios")
    class SuccessScenarios {

      @Test
      @DisplayName("Should change item quantity to the new value")
      void shouldChangeItemQuantity() {
        cart.addItem(product, quantity);
        ShoppingCartItemId itemId = cart.items().iterator().next().id();
        Quantity newQuantity = new Quantity(new BigDecimal("5"));

        cart.changeQuantityitem(itemId, newQuantity);

        ShoppingCartItem item = cart.items().iterator().next();
        assertThat(item.quantity()).isEqualTo(newQuantity);
      }

      @Test
      @DisplayName("Should recalculate totalItems after changing quantity")
      void shouldRecalculateTotalItemsAfterChangingQuantity() {
        cart.addItem(product, quantity);
        ShoppingCartItemId itemId = cart.items().iterator().next().id();
        Quantity newQuantity = new Quantity(new BigDecimal("5"));

        cart.changeQuantityitem(itemId, newQuantity);

        assertThat(cart.totalItems()).isEqualTo(newQuantity);
      }

      @Test
      @DisplayName("Should recalculate totalAmount after changing quantity")
      void shouldRecalculateTotalAmountAfterChangingQuantity() {
        cart.addItem(product, quantity);
        ShoppingCartItemId itemId = cart.items().iterator().next().id();
        Quantity newQuantity = new Quantity(new BigDecimal("5"));

        cart.changeQuantityitem(itemId, newQuantity);

        Money expectedAmount = product.price().multiply(newQuantity);
        assertThat(cart.totalAmount()).isEqualTo(expectedAmount);
      }
    }

    @Nested
    @DisplayName("Failure scenarios")
    class FailureScenarios {

      @Test
      @DisplayName("Should throw NullPointerException when itemId is null")
      void shouldThrowWhenItemIdIsNull() {
        assertThatThrownBy(() -> cart.changeQuantityitem(null, quantity))
            .isInstanceOf(NullPointerException.class);
      }

      @Test
      @DisplayName("Should throw NullPointerException when quantity is null")
      void shouldThrowWhenQuantityIsNull() {
        cart.addItem(product, quantity);
        ShoppingCartItemId itemId = cart.items().iterator().next().id();

        assertThatThrownBy(() -> cart.changeQuantityitem(itemId, null))
            .isInstanceOf(NullPointerException.class);
      }

      @Test
      @DisplayName("Should throw IllegalArgumentException when quantity is zero")
      void shouldThrowWhenQuantityIsZero() {
        cart.addItem(product, quantity);
        ShoppingCartItemId itemId = cart.items().iterator().next().id();

        assertThatThrownBy(() -> cart.changeQuantityitem(itemId, Quantity.ZERO))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Quantity must be greater than 0");
      }

      @Test
      @DisplayName("Should throw ShoppingCartItemNotFoundException when item does not exist")
      void shouldThrowWhenItemNotFound() {
        assertThatThrownBy(() -> cart.changeQuantityitem(new ShoppingCartItemId(), quantity))
            .isInstanceOf(ShoppingCartItemNotFoundException.class);
      }
    }
  }

  @Nested
  @DisplayName("refreshItem")
  class RefreshItem {

    @Nested
    @DisplayName("Success scenarios")
    class SuccessScenarios {

      @Test
      @DisplayName("Should update price when product price changes")
      void shouldUpdatePriceOnRefresh() {
        cart.addItem(product, quantity);

        Product refreshedProduct = ProductTestDataBuilder.createProduct()
            .id(product.id())
            .name(product.name())
            .price(new Money("150.00"))
            .inStock(true)
            .build();

        cart.refreshItem(refreshedProduct);

        ShoppingCartItem item = cart.items().iterator().next();
        assertThat(item.price()).isEqualTo(new Money("150.00"));
      }

      @Test
      @DisplayName("Should update product name on refresh")
      void shouldUpdateProductNameOnRefresh() {
        cart.addItem(product, quantity);

        ProductName newName = new ProductName("Notebook Ultra Pro");
        Product refreshedProduct = ProductTestDataBuilder.createProduct()
            .id(product.id())
            .name(newName)
            .price(product.price())
            .inStock(true)
            .build();

        cart.refreshItem(refreshedProduct);

        ShoppingCartItem item = cart.items().iterator().next();
        assertThat(item.productName()).isEqualTo(newName);
      }

      @Test
      @DisplayName("Should mark item as unavailable when product goes out of stock")
      void shouldMarkItemUnavailableWhenOutOfStock() {
        cart.addItem(product, quantity);

        Product outOfStockProduct = ProductTestDataBuilder.createProduct()
            .id(product.id())
            .name(product.name())
            .price(product.price())
            .inStock(false)
            .build();

        cart.refreshItem(outOfStockProduct);

        ShoppingCartItem item = cart.items().iterator().next();
        assertThat(item.available()).isFalse();
      }

      @Test
      @DisplayName("Should recalculate totalAmount after refreshing price")
      void shouldRecalculateTotalAmountAfterRefresh() {
        cart.addItem(product, quantity);

        Product refreshedProduct = ProductTestDataBuilder.createProduct()
            .id(product.id())
            .name(product.name())
            .price(new Money("150.00"))
            .inStock(true)
            .build();

        cart.refreshItem(refreshedProduct);

        assertThat(cart.totalAmount()).isEqualTo(new Money("300.00"));
      }
    }

    @Nested
    @DisplayName("Failure scenarios")
    class FailureScenarios {

      @Test
      @DisplayName("Should throw NullPointerException when product is null")
      void shouldThrowWhenProductIsNull() {
        assertThatThrownBy(() -> cart.refreshItem(null))
            .isInstanceOf(NullPointerException.class);
      }

      @Test
      @DisplayName("Should throw ShoppingCartItemNotFoundException when product is not in the cart")
      void shouldThrowWhenProductNotInCart() {
        assertThatThrownBy(() -> cart.refreshItem(product))
            .isInstanceOf(ShoppingCartItemNotFoundException.class);
      }
    }
  }

  @Nested
  @DisplayName("containsUnavailableItems")
  class ContainsUnavailableItems {

    @Nested
    @DisplayName("Success scenarios")
    class SuccessScenarios {

      @Test
      @DisplayName("Should return false when all items are available")
      void shouldReturnFalseWhenAllItemsAvailable() {
        cart.addItem(product, quantity);

        assertThat(cart.containsUnavailableItems()).isFalse();
      }

      @Test
      @DisplayName("Should return false when cart is empty")
      void shouldReturnFalseWhenCartIsEmpty() {
        assertThat(cart.containsUnavailableItems()).isFalse();
      }

      @Test
      @DisplayName("Should return true when at least one item is unavailable")
      void shouldReturnTrueWhenOneItemUnavailable() {
        cart.addItem(product, quantity);

        Product outOfStockProduct = ProductTestDataBuilder.createProduct()
            .id(product.id())
            .name(product.name())
            .price(product.price())
            .inStock(false)
            .build();

        cart.refreshItem(outOfStockProduct);

        assertThat(cart.containsUnavailableItems()).isTrue();
      }
    }
  }

  @Nested
  @DisplayName("isEmpty")
  class IsEmpty {

    @Nested
    @DisplayName("Success scenarios")
    class SuccessScenarios {

      @Test
      @DisplayName("Should return true when cart has no items")
      void shouldReturnTrueWhenNoItems() {
        assertThat(cart.isEmpty()).isTrue();
      }

      @Test
      @DisplayName("Should return false when cart has at least one item")
      void shouldReturnFalseWhenCartHasItems() {
        cart.addItem(product, quantity);

        assertThat(cart.isEmpty()).isFalse();
      }

      @Test
      @DisplayName("Should return true after all items are removed")
      void shouldReturnTrueAfterAllItemsRemoved() {
        cart.addItem(product, quantity);
        ShoppingCartItemId itemId = cart.items().iterator().next().id();
        cart.removeItem(itemId);

        assertThat(cart.isEmpty()).isTrue();
      }
    }
  }

  @Nested
  @DisplayName("empty")
  class Empty {

    @Nested
    @DisplayName("Success scenarios")
    class SuccessScenarios {

      @Test
      @DisplayName("Should clear all items from the cart")
      void shouldClearAllItems() {
        Product product2 = ProductTestDataBuilder.createProduct()
            .name(new ProductName("Mouse"))
            .price(new Money("50.00"))
            .inStock(true)
            .build();

        cart.addItem(product, quantity);
        cart.addItem(product2, new Quantity(BigDecimal.ONE));

        cart.empty();

        assertThat(cart.items()).isEmpty();
      }

      @Test
      @DisplayName("Should reset totalItems to zero after emptying")
      void shouldResetTotalItemsToZero() {
        cart.addItem(product, quantity);

        cart.empty();

        assertThat(cart.totalItems()).isEqualTo(Quantity.ZERO);
      }

      @Test
      @DisplayName("Should reset totalAmount to zero after emptying")
      void shouldResetTotalAmountToZero() {
        cart.addItem(product, quantity);

        cart.empty();

        assertThat(cart.totalAmount()).isEqualTo(Money.ZERO);
      }

      @Test
      @DisplayName("Should be idempotent when called on an already empty cart")
      void shouldBeIdempotentOnAlreadyEmptyCart() {
        cart.empty();

        assertThat(cart.isEmpty()).isTrue();
        assertThat(cart.totalItems()).isEqualTo(Quantity.ZERO);
        assertThat(cart.totalAmount()).isEqualTo(Money.ZERO);
      }
    }
  }

  @Nested
  @DisplayName("equals and hashCode")
  class EqualsAndHashCode {

    @Test
    @DisplayName("Should be equal when same shopping cart id")
    void shouldBeEqualWhenSameShoppingCartId() {
      ShoppingCartId cartId = new ShoppingCartId(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"));
      CustomerId customerId = new CustomerId(UUID.fromString("660e8400-e29b-41d4-a716-446655440001"));

      ShoppingCart cart1 = ShoppingCart.existingShoppingCartBuilder()
          .shoppingCartId(cartId)
          .customerId(customerId)
          .totalAmount(Money.ZERO)
          .totalItems(Quantity.ZERO)
          .createdAt(OffsetDateTime.now())
          .items(new HashSet<>())
          .build();

      ShoppingCart cart2 = ShoppingCart.existingShoppingCartBuilder()
          .shoppingCartId(cartId)
          .customerId(customerId)
          .totalAmount(Money.ZERO)
          .totalItems(Quantity.ZERO)
          .createdAt(OffsetDateTime.now())
          .items(new HashSet<>())
          .build();

      assertThat(cart1).isEqualTo(cart2);
      assertThat(cart1.hashCode()).isEqualTo(cart2.hashCode());
    }

    @Test
    @DisplayName("Should not be equal when different shopping cart id")
    void shouldNotBeEqualWhenDifferentShoppingCartId() {
      CustomerId customerId = new CustomerId(UUID.fromString("660e8400-e29b-41d4-a716-446655440001"));

      ShoppingCart cart1 = ShoppingCart.existingShoppingCartBuilder()
          .shoppingCartId(new ShoppingCartId(UUID.fromString("550e8400-e29b-41d4-a716-446655440000")))
          .customerId(customerId)
          .totalAmount(Money.ZERO)
          .totalItems(Quantity.ZERO)
          .createdAt(OffsetDateTime.now())
          .items(new HashSet<>())
          .build();

      ShoppingCart cart2 = ShoppingCart.existingShoppingCartBuilder()
          .shoppingCartId(new ShoppingCartId(UUID.fromString("770e8400-e29b-41d4-a716-446655440002")))
          .customerId(customerId)
          .totalAmount(Money.ZERO)
          .totalItems(Quantity.ZERO)
          .createdAt(OffsetDateTime.now())
          .items(new HashSet<>())
          .build();

      assertThat(cart1).isNotEqualTo(cart2);
    }

    @Test
    @DisplayName("Should not be equal to null")
    void shouldNotBeEqualToNull() {
      assertThat(cart).isNotEqualTo(null);
    }

    @Test
    @DisplayName("Should not be equal to different class")
    void shouldNotBeEqualToDifferentClass() {
      assertThat(cart).isNotEqualTo("not a shopping cart");
    }
  }
}
