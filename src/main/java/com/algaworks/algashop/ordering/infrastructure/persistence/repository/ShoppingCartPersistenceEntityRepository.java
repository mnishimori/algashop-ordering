package com.algaworks.algashop.ordering.infrastructure.persistence.repository;

import com.algaworks.algashop.ordering.infrastructure.persistence.entity.ShoppingCartPersistenceEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartPersistenceEntityRepository extends JpaRepository<ShoppingCartPersistenceEntity, UUID> {

}
