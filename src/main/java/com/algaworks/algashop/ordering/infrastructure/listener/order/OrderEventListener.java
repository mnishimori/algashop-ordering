package com.algaworks.algashop.ordering.infrastructure.listener.order;

import com.algaworks.algashop.ordering.domain.model.order.OrderCanceledEvent;
import com.algaworks.algashop.ordering.domain.model.order.OrderPaidEvent;
import com.algaworks.algashop.ordering.domain.model.order.OrderPlacedEvent;
import com.algaworks.algashop.ordering.domain.model.order.OrderReadyEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventListener {

  public void listen(OrderPlacedEvent event) {
    log.info("Order {} placed at {}", event.orderId(), event.placedAt());
  }

  public void listen(OrderReadyEvent event) {
    log.info("Order {} ready at {}", event.orderId(), event.readyAt());
  }

  public void listen(OrderCanceledEvent event) {
    log.info("Order {} canceled at {}", event.orderId(), event.canceledAt());
  }

  public void listen(OrderPaidEvent event) {
    log.info("Order {} paid at {}", event.orderId(), event.paidAt());
  }

}
