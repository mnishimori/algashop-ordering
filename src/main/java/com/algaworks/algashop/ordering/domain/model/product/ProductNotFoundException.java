package com.algaworks.algashop.ordering.domain.model.product;

import static com.algaworks.algashop.ordering.domain.model.ErrorMessages.PRODUCT_NOT_FOUND;

import com.algaworks.algashop.ordering.domain.model.DomainException;

public class ProductNotFoundException extends DomainException {

  public ProductNotFoundException(ProductId productId) {
    super(PRODUCT_NOT_FOUND.formatted(productId));
  }
}
