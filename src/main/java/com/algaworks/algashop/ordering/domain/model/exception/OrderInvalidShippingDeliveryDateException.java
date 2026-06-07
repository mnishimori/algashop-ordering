package com.algaworks.algashop.ordering.domain.model.exception;

import static com.algaworks.algashop.ordering.domain.model.messages.ErrorMessages.ORDER_INVALID_SHIPPING_DELIVERY_DATE;

import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderId;
import java.time.LocalDate;

public class OrderInvalidShippingDeliveryDateException extends DomainException {

  public OrderInvalidShippingDeliveryDateException(OrderId id, LocalDate expectedDeliveryDate) {
    super(ORDER_INVALID_SHIPPING_DELIVERY_DATE.formatted(id, expectedDeliveryDate));
  }
}
