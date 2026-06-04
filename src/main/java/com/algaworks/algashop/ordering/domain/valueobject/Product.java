package com.algaworks.algashop.ordering.domain.valueobject;

import com.algaworks.algashop.ordering.domain.exception.ProductOutOfStockException;
import com.algaworks.algashop.ordering.domain.valueobject.id.ProductId;
import java.util.Objects;
import lombok.Builder;

@Builder
public record Product(ProductId id, ProductName name, Money price, Boolean inStock) {

  public Product {
    Objects.requireNonNull(id);
    Objects.requireNonNull(name);
    Objects.requireNonNull(price);
    Objects.requireNonNull(inStock);
  }

  public void changeOutStock() {
    if (isOutOfStock()) {
      throw new ProductOutOfStockException(this.id());
    }
  }

  private boolean isOutOfStock() {
    return !inStock;
  }
}
