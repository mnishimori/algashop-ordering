package com.algaworks.algashop.ordering.application.customer.management;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.algaworks.algashop.ordering.IntegrationTest;
import com.algaworks.algashop.ordering.application.customer.loyaltypoints.CustomerLoyaltyPointsApplicationService;
import com.algaworks.algashop.ordering.domain.entity.CustomerTestDataBuilder;
import com.algaworks.algashop.ordering.domain.entity.OrderTestDataBuilder;
import com.algaworks.algashop.ordering.domain.model.commons.Money;
import com.algaworks.algashop.ordering.domain.model.customer.CantAddLoyantyPointsOrderIsNotReady;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerArchivedException;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerId;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerNotFoundException;
import com.algaworks.algashop.ordering.domain.model.customer.Customers;
import com.algaworks.algashop.ordering.domain.model.customer.LoyaltyPoints;
import com.algaworks.algashop.ordering.domain.model.order.OrderNotBelongsToCustomerException;
import com.algaworks.algashop.ordering.domain.model.order.OrderId;
import com.algaworks.algashop.ordering.domain.model.order.OrderNotFoundException;
import com.algaworks.algashop.ordering.domain.model.order.OrderStatus;
import com.algaworks.algashop.ordering.domain.model.order.Orders;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
class CustomerLoyaltyPointsApplicationServiceIT {

  @Autowired
  private CustomerLoyaltyPointsApplicationService customerLoyaltyPointsApplicationService;

  @Autowired
  private Customers customers;

  @Autowired
  private Orders orders;

  @Test
  void shouldAddLoyaltyPointsSuccessfully() {
    var customer = CustomerTestDataBuilder.existedCustomer().build();
    customers.add(customer);

    var order = orderForCustomer(customer.id(), new Money("6000"), OrderStatus.READY);
    orders.add(order);

    customerLoyaltyPointsApplicationService.addLoyaltyPoints(customer.id().value(), order.id().value().toString());

    var updatedCustomer = customers.findById(customer.id()).orElseThrow();
    assertThat(updatedCustomer.loyaltyPoints()).isEqualTo(new LoyaltyPoints(30));
  }

  @Test
  void shouldThrowExceptionWhenCustomerIsNotFound() {
    var customer = CustomerTestDataBuilder.existedCustomer()
        .customerId(new CustomerId(UUID.randomUUID()))
        .build();
    var order = orderForCustomer(customer.id(), new Money("6000"), OrderStatus.READY);
    orders.add(order);

    assertThatThrownBy(() -> customerLoyaltyPointsApplicationService.addLoyaltyPoints(UUID.randomUUID(),
        order.id().value().toString()))
        .isInstanceOf(CustomerNotFoundException.class);
  }

  @Test
  void shouldThrowExceptionWhenOrderIsNotFound() {
    var customer = CustomerTestDataBuilder.brandNewCustomer().build();
    customers.add(customer);

    assertThatThrownBy(
        () -> customerLoyaltyPointsApplicationService.addLoyaltyPoints(customer.id().value(), new OrderId().toString()))
        .isInstanceOf(OrderNotFoundException.class);
  }

  @Test
  void shouldThrowExceptionWhenCustomerIsArchived() {
    var customer = CustomerTestDataBuilder.brandNewCustomer().build();
    customers.add(customer);
    customer.archive();
    customers.add(customer);

    var order = orderForCustomer(customer.id(), new Money("6000"), OrderStatus.READY);
    orders.add(order);

    assertThatThrownBy(() -> customerLoyaltyPointsApplicationService.addLoyaltyPoints(customer.id().value(),
        order.id().value().toString()))
        .isInstanceOf(CustomerArchivedException.class);
  }

  @Test
  void shouldThrowExceptionWhenOrderDoesNotBelongToCustomer() {
    var customerA = CustomerTestDataBuilder.brandNewCustomer().build();
    var customerB = CustomerTestDataBuilder.brandNewCustomer().build();
    customers.add(customerA);
    customers.add(customerB);

    var order = orderForCustomer(customerB.id(), new Money("6000"), OrderStatus.READY);
    orders.add(order);

    assertThatThrownBy(() -> customerLoyaltyPointsApplicationService.addLoyaltyPoints(customerA.id().value(),
        order.id().value().toString()))
        .isInstanceOf(OrderNotBelongsToCustomerException.class);
  }

  @Test
  void shouldThrowExceptionWhenOrderIsNotReady() {
    var customer = CustomerTestDataBuilder.brandNewCustomer().build();
    customers.add(customer);

    var order = orderForCustomer(customer.id(), new Money("6000"), OrderStatus.PLACED);
    orders.add(order);

    assertThatThrownBy(() -> customerLoyaltyPointsApplicationService.addLoyaltyPoints(customer.id().value(),
        order.id().value().toString()))
        .isInstanceOf(CantAddLoyantyPointsOrderIsNotReady.class);
  }

  @Test
  void shouldNotAddPointsWhenOrderAmountIsBelowMinimum() {
    var customer = CustomerTestDataBuilder.existedCustomer().build();
    customers.add(customer);

    var order = orderForCustomer(customer.id(), new Money("999.99"), OrderStatus.READY);
    orders.add(order);

    customerLoyaltyPointsApplicationService.addLoyaltyPoints(customer.id().value(), order.id().value().toString());

    var updatedCustomer = customers.findById(customer.id()).orElseThrow();
    assertThat(updatedCustomer.loyaltyPoints()).isEqualTo(LoyaltyPoints.ZERO);
  }

  private static com.algaworks.algashop.ordering.domain.model.order.Order orderForCustomer(CustomerId customerId,
      Money totalAmount, OrderStatus status) {
    return OrderTestDataBuilder.anOrder()
        .customerId(customerId)
        .totalAmount(totalAmount)
        .status(status)
        .build();
  }

}
