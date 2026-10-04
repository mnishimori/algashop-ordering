package com.algaworks.algashop.ordering.domain.model.order;

import static com.algaworks.algashop.ordering.domain.model.ErrorMessages.ORDER_INVALID_SHIPPING_DELIVERY_DATE;

import com.algaworks.algashop.ordering.domain.model.DomainException;
import java.time.LocalDate;

public class OrderInvalidShippingDeliveryDateException extends DomainException {

  public OrderInvalidShippingDeliveryDateException(OrderId id, LocalDate expectedDeliveryDate) {
    super(ORDER_INVALID_SHIPPING_DELIVERY_DATE.formatted(id, expectedDeliveryDate));
  }
}
