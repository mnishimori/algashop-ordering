package com.algaworks.algashop.ordering.application.shoppingcart.management;

import com.algaworks.algashop.ordering.domain.model.commons.Quantity;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerId;
import com.algaworks.algashop.ordering.domain.model.product.ProductCatalogService;
import com.algaworks.algashop.ordering.domain.model.product.ProductId;
import com.algaworks.algashop.ordering.domain.model.product.ProductNotFoundException;
import com.algaworks.algashop.ordering.domain.model.shoppingcart.ShoppingCartId;
import com.algaworks.algashop.ordering.domain.model.shoppingcart.ShoppingCartItemId;
import com.algaworks.algashop.ordering.domain.model.shoppingcart.ShoppingCartNotFoundException;
import com.algaworks.algashop.ordering.domain.model.shoppingcart.ShoppingCarts;
import com.algaworks.algashop.ordering.domain.model.shoppingcart.ShoppingService;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShoppingCartManagementApplicationService {

  private final ShoppingCarts shoppingCarts;
  private final ProductCatalogService productCatalogService;
  private final ShoppingService shoppingService;

  @Transactional
  public void addItem(ShoppingCartItemInput input) {
    var shoppingCartId = new ShoppingCartId(input.getShoppingCartId());
    var shoppingCart = shoppingCarts.findById(shoppingCartId)
        .orElseThrow(() -> new ShoppingCartNotFoundException(shoppingCartId));
    var productId = new ProductId(input.getProductId());
    var product = productCatalogService.ofId(productId)
        .orElseThrow(() -> new ProductNotFoundException(productId));
    var quantity = new BigDecimal(input.getQuantity());
    var productQuantity = new Quantity(quantity);
    shoppingCart.addItem(product, productQuantity);
    shoppingCarts.add(shoppingCart);
  }

  @Transactional
  public ShoppingCartId createNew(UUID rawCustomerId) {
    var customerId = new CustomerId(rawCustomerId);
    var shoppingCart = shoppingService.startShopping(customerId);
    shoppingCarts.add(shoppingCart);
    return shoppingCart.shoppingCartId();
  }

  @Transactional
  public void removeItem(UUID rawShoppingCartId, UUID rawShoppingCartItemId) {
    var shoppingCart = shoppingCarts.findById(new ShoppingCartId(rawShoppingCartId))
        .orElseThrow(() -> new ShoppingCartNotFoundException(new ShoppingCartId(rawShoppingCartId)));
    shoppingCart.removeItem(new ShoppingCartItemId(rawShoppingCartItemId));
    shoppingCarts.add(shoppingCart);
  }

  @Transactional
  public void empty(UUID rawShoppingCartId) {
    var shoppingCart = shoppingCarts.findById(new ShoppingCartId(rawShoppingCartId))
        .orElseThrow(() -> new ShoppingCartNotFoundException(new ShoppingCartId(rawShoppingCartId)));
    shoppingCart.empty();
    shoppingCarts.add(shoppingCart);
  }

}
