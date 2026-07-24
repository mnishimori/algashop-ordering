package com.algaworks.algashop.ordering.infrastructure.persistence.provider;

import static org.assertj.core.api.Assertions.assertThat;

import com.algaworks.algashop.ordering.IntegrationTest;
import com.algaworks.algashop.ordering.domain.entity.OrderItemTestDataBuilder;
import com.algaworks.algashop.ordering.domain.entity.OrderTestDataBuilder;
import com.algaworks.algashop.ordering.domain.model.entity.OrderStatus;
import com.algaworks.algashop.ordering.infrastructure.persistence.assembler.OrderPersistenceEntityAssembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.config.SpringDataAuditingConfig;
import com.algaworks.algashop.ordering.infrastructure.persistence.disassembler.OrderPersistenceEntityDisassembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.repository.OrderPersistenceEntityRepository;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderItemId;
import io.hypersistence.tsid.TSID;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@IntegrationTest
@Import({OrdersPersistenceProvider.class, OrderPersistenceEntityDisassembler.class,
    OrderPersistenceEntityAssembler.class, SpringDataAuditingConfig.class})
class OrdersPersistenceProviderIntegrationTest {

  private OrdersPersistenceProvider persistenceProvider;
  private OrderPersistenceEntityRepository repository;

  @Autowired
  OrdersPersistenceProviderIntegrationTest(OrdersPersistenceProvider persistenceProvider,
      OrderPersistenceEntityRepository repository) {
    this.persistenceProvider = persistenceProvider;
    this.repository = repository;
  }

  @Test
  void shouldUpdateAndKeepPersistenceEntityState() {
    var orderId = new OrderId(TSID.from(123456789L));
    var order = OrderTestDataBuilder.anOrder()
        .id(orderId)
        .status(OrderStatus.PLACED)
        .items(Set.of(
            OrderItemTestDataBuilder.anOrderItem()
                .id(new OrderItemId(TSID.from(111111111L)))
                .orderId(orderId)
                .build(),
            OrderItemTestDataBuilder.anOrderItem()
                .id(new OrderItemId(TSID.from(222222222L)))
                .orderId(orderId)
                .build()
        ))
        .build();
    persistenceProvider.add(order);

    var orderPersistenceEntity = repository.findById(orderId.value().toLong()).orElseThrow();

    assertThat(orderPersistenceEntity).isNotNull();
    assertThat(orderPersistenceEntity.getStatus()).isNotNull();
    assertThat(orderPersistenceEntity.getStatus()).isEqualTo(order.status().name());
    assertThat(orderPersistenceEntity.getCreatedByUserId()).isNotNull();
    assertThat(orderPersistenceEntity.getLastModifiedAt()).isNotNull();
    assertThat(orderPersistenceEntity.getLastModifiedByUserId()).isNotNull();
    assertThat(orderPersistenceEntity.getItems()).isNotNull();
    assertThat(orderPersistenceEntity.getItems()).hasSize(2);

    order = persistenceProvider.findById(orderId).orElseThrow();
    order.markAsPaid();
    persistenceProvider.add(order);

    orderPersistenceEntity = repository.findById(orderId.value().toLong()).orElseThrow();

    assertThat(orderPersistenceEntity).isNotNull();
    assertThat(orderPersistenceEntity.getStatus()).isNotNull();
    assertThat(orderPersistenceEntity.getStatus()).isEqualTo(order.status().name());
    assertThat(orderPersistenceEntity.getCreatedByUserId()).isNotNull();
    assertThat(orderPersistenceEntity.getLastModifiedAt()).isNotNull();
    assertThat(orderPersistenceEntity.getLastModifiedByUserId()).isNotNull();
    assertThat(orderPersistenceEntity.getItems()).isNotNull();
    assertThat(orderPersistenceEntity.getItems()).hasSize(2);
  }
}