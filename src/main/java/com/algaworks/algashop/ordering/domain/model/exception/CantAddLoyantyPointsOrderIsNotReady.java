package com.algaworks.algashop.ordering.domain.model.exception;

import static com.algaworks.algashop.ordering.domain.model.messages.ErrorMessages.CANNOT_ADD_LOYALTY_POINTS_ORDER_IS_NOT_READY;

public class CantAddLoyantyPointsOrderIsNotReady extends DomainException {

  public CantAddLoyantyPointsOrderIsNotReady() {
    super(CANNOT_ADD_LOYALTY_POINTS_ORDER_IS_NOT_READY);
  }
}
