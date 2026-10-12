package com.algaworks.algashop.ordering.application.shoppingcart.management;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.algaworks.algashop.ordering.IntegrationTest;
import com.algaworks.algashop.ordering.domain.entity.ShoppingCartTestDataBuilder;
import com.algaworks.algashop.ordering.domain.model.commons.Money;
import com.algaworks.algashop.ordering.domain.model.commons.Quantity;
import com.algaworks.algashop.ordering.domain.model.product.Product;
import com.algaworks.algashop.ordering.domain.model.product.ProductCatalogService;
import com.algaworks.algashop.ordering.domain.model.product.ProductId;
import com.algaworks.algashop.ordering.domain.model.product.ProductName;
import com.algaworks.algashop.ordering.domain.model.shoppingcart.ShoppingCart;
import com.algaworks.algashop.ordering.domain.model.shoppingcart.ShoppingCartId;
import com.algaworks.algashop.ordering.domain.model.shoppingcart.ShoppingCarts;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.Test;
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

    var updatedShoppingCart = shoppingCart;
    assertThat(updatedShoppingCart.items()).hasSize(1);
    var item = updatedShoppingCart.items().iterator().next();
    assertThat(item.product()).isEqualTo(productId);
    assertThat(item.productName()).isEqualTo(new ProductName("Notebook"));
    assertThat(item.price()).isEqualTo(new Money(new BigDecimal("1000.00")));
    assertThat(item.quantity()).isEqualTo(new Quantity(new BigDecimal("2")));
    assertThat(item.totalAmount()).isEqualTo(new Money(new BigDecimal("2000.00")));
    assertThat(updatedShoppingCart.totalItems()).isEqualTo(new Quantity(new BigDecimal("2")));
    assertThat(updatedShoppingCart.totalAmount()).isEqualTo(new Money(new BigDecimal("2000.00")));
  }
}
