package com.algaworks.algashop.ordering.domain.exception;

import static com.algaworks.algashop.ordering.domain.messages.ErrorMessages.ORDER_INVALID_SHIPPING_DELIVERY_DATE;

import com.algaworks.algashop.ordering.domain.valueobject.id.OrderId;
import java.time.LocalDate;

public class OrderInvalidShippingDeliveryDateException extends DomainException {

  public OrderInvalidShippingDeliveryDateException(OrderId id, LocalDate expectedDeliveryDate) {
    super(ORDER_INVALID_SHIPPING_DELIVERY_DATE.formatted(id, expectedDeliveryDate));
  }
}
