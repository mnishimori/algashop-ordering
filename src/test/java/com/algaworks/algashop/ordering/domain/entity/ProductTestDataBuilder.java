package com.algaworks.algashop.ordering.domain.entity;

import com.algaworks.algashop.ordering.domain.model.commons.Money;
import com.algaworks.algashop.ordering.domain.model.product.Product;
import com.algaworks.algashop.ordering.domain.model.product.ProductName;
import com.algaworks.algashop.ordering.domain.model.product.ProductId;

public class ProductTestDataBuilder {

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
