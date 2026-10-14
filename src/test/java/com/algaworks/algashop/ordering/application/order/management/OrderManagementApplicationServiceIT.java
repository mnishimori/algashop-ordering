package com.algaworks.algashop.ordering.application.order.management;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.algaworks.algashop.ordering.IntegrationTest;
import com.algaworks.algashop.ordering.domain.entity.OrderTestDataBuilder;
import com.algaworks.algashop.ordering.domain.model.order.OrderCannotBeCanceledException;
import com.algaworks.algashop.ordering.domain.model.order.OrderCannotBeReadyException;
import com.algaworks.algashop.ordering.domain.model.order.OrderId;
import com.algaworks.algashop.ordering.domain.model.order.OrderNotFoundException;
import com.algaworks.algashop.ordering.domain.model.order.OrderStatus;
import com.algaworks.algashop.ordering.domain.model.order.Orders;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
class OrderManagementApplicationServiceIT {

  @Autowired
  private OrderManagementApplicationService orderManagementApplicationService;

  @Autowired
  private Orders orders;

  @Test
  void shouldCancelOrderSuccessfully() {
    var order = OrderTestDataBuilder.anOrder()
        .status(OrderStatus.PLACED)
        .build();
    orders.add(order);

    orderManagementApplicationService.cancel(order.id().value().toLong());

    var canceledOrder = orders.findById(order.id()).orElseThrow();
    assertThat(canceledOrder.status()).isEqualTo(OrderStatus.CANCELED);
    assertThat(canceledOrder.canceledAt()).isNotNull();
  }

  @Test
  void shouldThrowExceptionWhenOrderIsNotFound() {
    var nonExistingOrderId = new OrderId().value().toLong();

    assertThatThrownBy(() -> orderManagementApplicationService.cancel(nonExistingOrderId))
        .isInstanceOf(OrderNotFoundException.class);
  }

  @Test
  void shouldThrowExceptionWhenOrderIsAlreadyCanceled() {
    var order = OrderTestDataBuilder.anOrder()
        .status(OrderStatus.CANCELED)
        .canceledAt(OffsetDateTime.now())
        .build();
    orders.add(order);

    assertThatThrownBy(() -> orderManagementApplicationService.cancel(order.id().value().toLong()))
        .isInstanceOf(OrderCannotBeCanceledException.class);
  }

  @Test
  void shouldMarkOrderAsPaidSuccessfully() {
    var order = OrderTestDataBuilder.anOrder()
        .status(OrderStatus.PLACED)
        .build();
    orders.add(order);

    orderManagementApplicationService.markAsPaid(order.id().value().toLong());

    var paidOrder = orders.findById(order.id()).orElseThrow();
    assertThat(paidOrder.status()).isEqualTo(OrderStatus.PAID);
    assertThat(paidOrder.paidAt()).isNotNull();
  }

  @Test
  void shouldMarkOrderAsReadySuccessfully() {
    var order = OrderTestDataBuilder.anOrder()
        .status(OrderStatus.PAID)
        .paidAt(OffsetDateTime.now())
        .build();
    orders.add(order);

    orderManagementApplicationService.markAsReady(order.id().value().toLong());

    var readyOrder = orders.findById(order.id()).orElseThrow();
    assertThat(readyOrder.status()).isEqualTo(OrderStatus.READY);
    assertThat(readyOrder.readyAt()).isNotNull();
  }

  @Test
  void shouldThrowExceptionWhenOrderIsNotFoundForMarkAsReady() {
    var nonExistingOrderId = new OrderId().value().toLong();

    assertThatThrownBy(() -> orderManagementApplicationService.markAsReady(nonExistingOrderId))
        .isInstanceOf(OrderNotFoundException.class);
  }

  @Test
  void shouldThrowExceptionWhenOrderIsNotPaidForMarkAsReady() {
    var order = OrderTestDataBuilder.anOrder()
        .status(OrderStatus.PLACED)
        .build();
    orders.add(order);

    assertThatThrownBy(() -> orderManagementApplicationService.markAsReady(order.id().value().toLong()))
        .isInstanceOf(OrderCannotBeReadyException.class);
  }

  @Test
  void shouldThrowExceptionWhenPaidOrderIsNotFound() {
    var nonExistingOrderId = new OrderId().value().toLong();

    assertThatThrownBy(() -> orderManagementApplicationService.markAsPaid(nonExistingOrderId))
        .isInstanceOf(OrderNotFoundException.class);
  }



}
