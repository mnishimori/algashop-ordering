package com.algaworks.algashop.ordering.application.shoppingcart.management;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.algaworks.algashop.ordering.IntegrationTest;
import com.algaworks.algashop.ordering.domain.entity.ShoppingCartTestDataBuilder;
import com.algaworks.algashop.ordering.domain.model.commons.Money;
import com.algaworks.algashop.ordering.domain.model.commons.Quantity;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerAlreadyHaveShoppingCartException;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerId;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerNotFoundException;
import com.algaworks.algashop.ordering.domain.model.customer.Customers;
import com.algaworks.algashop.ordering.domain.model.product.Product;
import com.algaworks.algashop.ordering.domain.model.product.ProductCatalogService;
import com.algaworks.algashop.ordering.domain.model.product.ProductId;
import com.algaworks.algashop.ordering.domain.model.product.ProductName;
import com.algaworks.algashop.ordering.domain.model.shoppingcart.ShoppingCart;
import com.algaworks.algashop.ordering.domain.model.shoppingcart.ShoppingCartId;
import com.algaworks.algashop.ordering.domain.model.shoppingcart.ShoppingCartItemNotFoundException;
import com.algaworks.algashop.ordering.domain.model.shoppingcart.ShoppingCartNotFoundException;
import com.algaworks.algashop.ordering.domain.model.shoppingcart.ShoppingCarts;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@IntegrationTest
class ShoppingCartManagementApplicationServiceIntegrationTest {

  @Autowired
  private ShoppingCartManagementApplicationService shoppingCartManagementApplicationService;

  @MockitoBean
  private ShoppingCarts shoppingCarts;

  @MockitoBean
  private ProductCatalogService productCatalogService;

  @MockitoBean
  private Customers customers;

  @Test
  void shouldAddItemToShoppingCart() {
    var shoppingCart = ShoppingCartTestDataBuilder.aShoppingCart()
        .shoppingCartId(new ShoppingCartId(UUID.fromString("11111111-1111-1111-1111-111111111111")))
        .build();

    when(shoppingCarts.findById(shoppingCart.id())).thenReturn(java.util.Optional.of(shoppingCart));
    doNothing().when(shoppingCarts).add(any(ShoppingCart.class));

    var productId = new ProductId(UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb"));
    var product = Product.builder()
        .id(productId)
        .name(new ProductName("Notebook"))
        .price(new Money(new BigDecimal("1000.00")))
        .inStock(true)
        .build();
    when(productCatalogService.ofId(productId)).thenReturn(java.util.Optional.of(product));

    var input = ShoppingCartItemInput.builder()
        .shoppingCartId(shoppingCart.id().value())
        .productId(productId.value())
        .quantity(2)
        .build();

    shoppingCartManagementApplicationService.addItem(input);

    assertThat(shoppingCart.items()).hasSize(1);
    var item = shoppingCart.items().iterator().next();
    assertThat(item.product()).isEqualTo(productId);
    assertThat(item.productName()).isEqualTo(new ProductName("Notebook"));
    assertThat(item.price()).isEqualTo(new Money(new BigDecimal("1000.00")));
    assertThat(item.quantity()).isEqualTo(new Quantity(new BigDecimal("2")));
    assertThat(item.totalAmount()).isEqualTo(new Money(new BigDecimal("2000.00")));
    assertThat(shoppingCart.totalItems()).isEqualTo(new Quantity(new BigDecimal("2")));
    assertThat(shoppingCart.totalAmount()).isEqualTo(new Money(new BigDecimal("2000.00")));
  }

  @Test
  void shouldCreateNewShoppingCart() {
    var rawCustomerId = UUID.randomUUID();
    var customerId = new CustomerId(rawCustomerId);
    when(customers.existsById(customerId)).thenReturn(true);
    when(shoppingCarts.ofCustomer(customerId)).thenReturn(java.util.Optional.empty());
    doNothing().when(shoppingCarts).add(any(ShoppingCart.class));

    var shoppingCartId = shoppingCartManagementApplicationService.createNew(rawCustomerId);

    assertThat(shoppingCartId).isNotNull();
    var captor = ArgumentCaptor.forClass(ShoppingCart.class);
    verify(shoppingCarts).add(captor.capture());
    var saved = captor.getValue();
    assertThat(saved.id()).isEqualTo(shoppingCartId);
    assertThat(saved.customerId()).isEqualTo(customerId);
    assertThat(saved.items()).isEmpty();
    assertThat(saved.totalAmount()).isEqualTo(Money.ZERO);
    assertThat(saved.totalItems()).isEqualTo(Quantity.ZERO);
  }

  @Test
  void shouldFailToCreateNewShoppingCartWhenCustomerDoesNotExist() {
    var rawCustomerId = UUID.randomUUID();
    var customerId = new CustomerId(rawCustomerId);
    when(customers.existsById(customerId)).thenReturn(false);

    assertThatThrownBy(() -> shoppingCartManagementApplicationService.createNew(rawCustomerId))
        .isInstanceOf(CustomerNotFoundException.class);

    verify(shoppingCarts, never()).add(any(ShoppingCart.class));
  }

  @Test
  void shouldFailToCreateNewShoppingCartWhenCustomerAlreadyHasShoppingCart() {
    var rawCustomerId = UUID.randomUUID();
    var customerId = new CustomerId(rawCustomerId);
    var existing = ShoppingCartTestDataBuilder.aShoppingCart().customerId(customerId).build();
    when(customers.existsById(customerId)).thenReturn(true);
    when(shoppingCarts.ofCustomer(customerId)).thenReturn(java.util.Optional.of(existing));

    assertThatThrownBy(() -> shoppingCartManagementApplicationService.createNew(rawCustomerId))
        .isInstanceOf(CustomerAlreadyHaveShoppingCartException.class);

    verify(shoppingCarts, never()).add(any(ShoppingCart.class));
  }

  private ShoppingCart aCartWithTwoProducts() {
    var cart = ShoppingCart.newShoppingCart(new CustomerId());
    cart.addItem(aProduct("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb", "Notebook", "1000.00"),
        new Quantity(new BigDecimal("2")));
    cart.addItem(aProduct("cccccccc-cccc-cccc-cccc-cccccccccccc", "Mouse", "50.00"),
        new Quantity(new BigDecimal("1")));
    return cart;
  }

  private Product aProduct(String id, String name, String price) {
    return Product.builder()
        .id(new ProductId(UUID.fromString(id)))
        .name(new ProductName(name))
        .price(new Money(new BigDecimal(price)))
        .inStock(true)
        .build();
  }

  @Test
  void shouldRemoveItemFromShoppingCart() {
    var cart = aCartWithTwoProducts();
    var notebook = cart.items().stream()
        .filter(i -> i.productName().equals(new ProductName("Notebook")))
        .findFirst().orElseThrow();
    when(shoppingCarts.findById(cart.id())).thenReturn(java.util.Optional.of(cart));

    shoppingCartManagementApplicationService.removeItem(cart.id().value(), notebook.id().value());

    assertThat(cart.items()).hasSize(1);
    assertThat(cart.items()).noneMatch(i -> i.id().equals(notebook.id()));
    assertThat(cart.totalItems()).isEqualTo(new Quantity(new BigDecimal("1")));
    assertThat(cart.totalAmount()).isEqualTo(new Money(new BigDecimal("50.00")));
    verify(shoppingCarts).add(cart);
  }

  @Test
  void shouldFailToRemoveItemWhenShoppingCartDoesNotExist() {
    var cartId = UUID.randomUUID();
    when(shoppingCarts.findById(new ShoppingCartId(cartId))).thenReturn(java.util.Optional.empty());

    assertThatThrownBy(() -> shoppingCartManagementApplicationService.removeItem(cartId, UUID.randomUUID()))
        .isInstanceOf(ShoppingCartNotFoundException.class);

    verify(shoppingCarts, never()).add(any(ShoppingCart.class));
  }

  @Test
  void shouldFailToRemoveItemWhenItemDoesNotExistInShoppingCart() {
    var cart = aCartWithTwoProducts();
    when(shoppingCarts.findById(cart.id())).thenReturn(java.util.Optional.of(cart));

    assertThatThrownBy(() -> shoppingCartManagementApplicationService.removeItem(cart.id().value(), UUID.randomUUID()))
        .isInstanceOf(ShoppingCartItemNotFoundException.class);

    assertThat(cart.items()).hasSize(2);
    verify(shoppingCarts, never()).add(any(ShoppingCart.class));
  }

  @Test
  void shouldFailToRemoveItemWhenShoppingCartIdIsNull() {
    assertThatThrownBy(() -> shoppingCartManagementApplicationService.removeItem(null, UUID.randomUUID()))
        .isInstanceOf(NullPointerException.class);

    verify(shoppingCarts, never()).add(any(ShoppingCart.class));
  }

  @Test
  void shouldFailToRemoveItemWhenItemIdIsNull() {
    var cart = aCartWithTwoProducts();
    when(shoppingCarts.findById(cart.id())).thenReturn(java.util.Optional.of(cart));

    assertThatThrownBy(() -> shoppingCartManagementApplicationService.removeItem(cart.id().value(), null))
        .isInstanceOf(NullPointerException.class);

    assertThat(cart.items()).hasSize(2);
    verify(shoppingCarts, never()).add(any(ShoppingCart.class));
  }

  @Test
  void shouldFailToCreateNewShoppingCartWhenCustomerIdIsNull() {
    assertThatThrownBy(() -> shoppingCartManagementApplicationService.createNew(null))
        .isInstanceOf(NullPointerException.class);

    verify(shoppingCarts, never()).add(any(ShoppingCart.class));
  }
}
