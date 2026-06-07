package com.algaworks.algashop.ordering.domain.model.exception;

import static com.algaworks.algashop.ordering.domain.model.messages.ErrorMessages.PRODUCT_OUT_OF_STOCK;

import com.algaworks.algashop.ordering.domain.model.valueobject.id.ProductId;

public class ProductOutOfStockException extends DomainException {

  public ProductOutOfStockException(ProductId id) {
    super(PRODUCT_OUT_OF_STOCK.formatted(id));
  }
}
