package com.algaworks.algashop.ordering.domain.model.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.algaworks.algashop.ordering.IntegrationTest;
import com.algaworks.algashop.ordering.domain.model.entity.Order;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
class OrdersTest {

  private final Orders orders;

  @Autowired
  OrdersTest(Orders orders) {
    this.orders = orders;
  }

  @Test
  void shouldPersistAndFindOrder() {
    Order order = Order.createDraftOrder(new CustomerId());
    orders.add(order);

    Optional<Order> orderFound = orders.findById(order.id());

    assertThat(orderFound).isPresent();
    assertThat(orderFound.get()).isEqualTo(order);
  }
}
