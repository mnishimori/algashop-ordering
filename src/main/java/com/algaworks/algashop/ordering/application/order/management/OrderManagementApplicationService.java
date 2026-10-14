package com.algaworks.algashop.ordering.application.order.management;

import com.algaworks.algashop.ordering.domain.model.order.OrderId;
import com.algaworks.algashop.ordering.domain.model.order.OrderNotFoundException;
import com.algaworks.algashop.ordering.domain.model.order.Orders;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderManagementApplicationService {

  private final Orders orders;

  @Transactional
  public void cancel(Long rawOrderId) {
    var orderId = new OrderId(rawOrderId);
    var order = orders.findById(orderId)
        .orElseThrow(() -> new OrderNotFoundException(orderId));
    order.cancel();
    orders.add(order);
  }

  @Transactional
  public void markAsPaid(Long rawOrderId) {
    var orderId = new OrderId(rawOrderId);
    var order = orders.findById(orderId)
        .orElseThrow(() -> new OrderNotFoundException(orderId));
    order.markAsPaid();
    orders.add(order);
  }

}
