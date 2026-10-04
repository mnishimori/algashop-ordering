package com.algaworks.algashop.ordering.domain.model.customer;

import static com.algaworks.algashop.ordering.domain.model.ErrorMessages.CANNOT_ADD_LOYALTY_POINTS_ORDER_IS_NOT_READY;

import com.algaworks.algashop.ordering.domain.model.DomainException;

public class CantAddLoyantyPointsOrderIsNotReady extends DomainException {

  public CantAddLoyantyPointsOrderIsNotReady() {
    super(CANNOT_ADD_LOYALTY_POINTS_ORDER_IS_NOT_READY);
  }
}
