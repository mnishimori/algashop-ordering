package com.algaworks.algashop.ordering.infrastructure.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.algaworks.algashop.ordering.IntegrationTest;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntityTestDataBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
class OrderPersistenceEntityRepositoryTest {

  private final OrderPersistenceEntityRepository repository;

  @Autowired
  OrderPersistenceEntityRepositoryTest(OrderPersistenceEntityRepository repository) {
    this.repository = repository;
  }

  @Test
  void shouldPersist() {
    OrderPersistenceEntity order = OrderPersistenceEntityTestDataBuilder.existingOrder().build();

    repository.saveAndFlush(order);

    var orderPersisted = repository.findById(order.getId()).orElse(null);
    assertThat(orderPersisted).isNotNull();
    assertThat(orderPersisted.getId()).isNotNull();
    assertThat(orderPersisted.getItems()).isNotNull().isNotEmpty().hasSize(2);
  }
}