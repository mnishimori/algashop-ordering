package com.algaworks.algashop.ordering.domain.entity;

import com.algaworks.algashop.ordering.domain.model.valueobject.Money;
import com.algaworks.algashop.ordering.domain.model.valueobject.Product;
import com.algaworks.algashop.ordering.domain.model.valueobject.ProductName;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.ProductId;

public class ProductTestDataBuilder {

  private ProductTestDataBuilder() {
  }

  public static Product.ProductBuilder createProduct() {
    return Product.builder().id(new ProductId()).name(new ProductName("Notebook")).price(new Money("3000")).inStock(true);
  }

  public static Product.ProductBuilder createProduct(ProductName productName, Money price, Boolean inStock) {
    return Product.builder().id(new ProductId()).name(productName).price(price).inStock(inStock);
  }
}
