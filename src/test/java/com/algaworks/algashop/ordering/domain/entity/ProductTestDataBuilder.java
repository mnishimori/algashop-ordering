package com.algaworks.algashop.ordering.domain.entity;

import com.algaworks.algashop.ordering.domain.model.commons.Money;
import com.algaworks.algashop.ordering.domain.model.product.Product;
import com.algaworks.algashop.ordering.domain.model.product.ProductName;
import com.algaworks.algashop.ordering.domain.model.product.ProductId;
import java.util.UUID;

public class ProductTestDataBuilder {

  public static final ProductId DEFAULT_PRODUCT_ID = new ProductId(UUID.fromString("550e8400-e29b-41d4-a716-446655440010"));
  public static final UUID DEFAULT_CUSTOMER_ID = UUID.fromString("550e8400-e29b-41d4-a716-446655440011");

  private ProductTestDataBuilder() {
  }

  public static Product.ProductBuilder createProduct() {
    return Product.builder().id(new ProductId()).name(new ProductName("Notebook")).price(new Money("3000")).inStock(true);
  }

  public static Product.ProductBuilder createProduct(ProductName productName, Money price, Boolean inStock) {
    return Product.builder().id(new ProductId()).name(productName).price(price).inStock(inStock);
  }

  public static Product.ProductBuilder aProductUnavailable() {
    return Product.builder().id(new ProductId()).name(new ProductName("Notebook")).price(new Money("3000")).inStock(false);
  }
}
