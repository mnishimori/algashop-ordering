package com.algaworks.algashop.ordering.application.order.management;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import com.algaworks.algashop.ordering.IntegrationTest;
import com.algaworks.algashop.ordering.application.customer.loyaltypoints.CustomerLoyaltyPointsApplicationService;
import com.algaworks.algashop.ordering.domain.entity.OrderTestDataBuilder;
import com.algaworks.algashop.ordering.domain.model.order.Order;
import com.algaworks.algashop.ordering.domain.model.order.OrderCanceledEvent;
import com.algaworks.algashop.ordering.domain.model.order.OrderCannotBeCanceledException;
import com.algaworks.algashop.ordering.domain.model.order.OrderCannotBeReadyException;
import com.algaworks.algashop.ordering.domain.model.order.OrderId;
import com.algaworks.algashop.ordering.domain.model.order.OrderNotFoundException;
import com.algaworks.algashop.ordering.domain.model.order.OrderPaidEvent;
import com.algaworks.algashop.ordering.domain.model.order.OrderReadyEvent;
import com.algaworks.algashop.ordering.domain.model.order.OrderStatus;
import com.algaworks.algashop.ordering.domain.model.order.Orders;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

@IntegrationTest
class OrderManagementApplicationServiceIT {

  @Autowired
  private OrderManagementApplicationService orderManagementApplicationService;

  @MockitoSpyBean
  private Orders orders;
  @MockitoSpyBean
  private CustomerLoyaltyPointsApplicationService customerLoyaltyPointsApplicationService;

  @Test
  void shouldCancelOrderSuccessfully() {
    var order = OrderTestDataBuilder.anOrder()
        .status(OrderStatus.PLACED)
        .build();
    orders.add(order);
    clearInvocations(orders);

    orderManagementApplicationService.cancel(order.id().value().toLong());

    var canceledOrder = orders.findById(order.id()).orElseThrow();
    assertThat(canceledOrder.status()).isEqualTo(OrderStatus.CANCELED);
    assertThat(canceledOrder.canceledAt()).isNotNull();

    var orderCaptor = ArgumentCaptor.forClass(Order.class);
    verify(orders).add(orderCaptor.capture());
    assertThat(orderCaptor.getValue().domainEvents())
        .singleElement()
        .isInstanceOf(OrderCanceledEvent.class)
        .satisfies(event -> {
          var canceledEvent = (OrderCanceledEvent) event;
          assertThat(canceledEvent.orderId()).isEqualTo(order.id());
          assertThat(canceledEvent.customerId()).isEqualTo(order.customerId());
          assertThat(canceledEvent.canceledAt()).isNotNull();
        });
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
    clearInvocations(orders);

    orderManagementApplicationService.markAsPaid(order.id().value().toLong());

    var paidOrder = orders.findById(order.id()).orElseThrow();
    assertThat(paidOrder.status()).isEqualTo(OrderStatus.PAID);
    assertThat(paidOrder.paidAt()).isNotNull();

    var orderCaptor = ArgumentCaptor.forClass(Order.class);
    verify(orders).add(orderCaptor.capture());
    assertThat(orderCaptor.getValue().domainEvents())
        .singleElement()
        .isInstanceOf(OrderPaidEvent.class)
        .satisfies(event -> {
          var paidEvent = (OrderPaidEvent) event;
          assertThat(paidEvent.orderId()).isEqualTo(order.id());
          assertThat(paidEvent.customerId()).isEqualTo(order.customerId());
          assertThat(paidEvent.paidAt()).isNotNull();
        });
  }

  @Test
  void shouldMarkOrderAsReadySuccessfully() {
    var order = OrderTestDataBuilder.anOrder()
        .status(OrderStatus.PAID)
        .paidAt(OffsetDateTime.now())
        .build();
    orders.add(order);
    clearInvocations(orders);
    doNothing()
        .when(customerLoyaltyPointsApplicationService)
        .addLoyaltyPoints(any(UUID.class), anyString());

    orderManagementApplicationService.markAsReady(order.id().value().toLong());

    var readyOrder = orders.findById(order.id()).orElseThrow();
    assertThat(readyOrder.status()).isEqualTo(OrderStatus.READY);
    assertThat(readyOrder.readyAt()).isNotNull();

    var orderCaptor = ArgumentCaptor.forClass(Order.class);
    verify(orders).add(orderCaptor.capture());
    assertThat(orderCaptor.getValue().domainEvents())
        .singleElement()
        .isInstanceOf(OrderReadyEvent.class)
        .satisfies(event -> {
          var readyEvent = (OrderReadyEvent) event;
          assertThat(readyEvent.orderId()).isEqualTo(order.id());
          assertThat(readyEvent.customerId()).isEqualTo(order.customerId());
          assertThat(readyEvent.readyAt()).isNotNull();
        });
    verify(customerLoyaltyPointsApplicationService)
        .addLoyaltyPoints(any(UUID.class), anyString());
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
