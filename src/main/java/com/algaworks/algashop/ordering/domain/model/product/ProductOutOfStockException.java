package com.algaworks.algashop.ordering.domain.model.product;

import static com.algaworks.algashop.ordering.domain.model.ErrorMessages.PRODUCT_OUT_OF_STOCK;

import com.algaworks.algashop.ordering.domain.model.DomainException;

public class ProductOutOfStockException extends DomainException {

  public ProductOutOfStockException(ProductId id) {
    super(PRODUCT_OUT_OF_STOCK.formatted(id));
  }
}
