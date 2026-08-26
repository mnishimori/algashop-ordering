package com.algaworks.algashop.ordering.infrastructure.persistence.repository;

import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderPersistenceEntityRepository extends JpaRepository<OrderPersistenceEntity, Long> {

  @Query("SELECT o FROM OrderPersistenceEntity o WHERE o.customer.id = :customerId AND YEAR(o.placedAt) = :year")
  List<OrderPersistenceEntity> placedByCustomerInYear(UUID customerId, Integer year);

}
