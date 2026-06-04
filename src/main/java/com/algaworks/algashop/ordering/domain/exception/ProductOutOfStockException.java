package com.algaworks.algashop.ordering.domain.exception;

import static com.algaworks.algashop.ordering.domain.messages.ErrorMessages.PRODUCT_OUT_OF_STOCK;

import com.algaworks.algashop.ordering.domain.valueobject.id.ProductId;

public class ProductOutOfStockException extends DomainException {

  public ProductOutOfStockException(ProductId id) {
    super(PRODUCT_OUT_OF_STOCK.formatted(id));
  }
}
