package com.algaworks.algashop.ordering.infrastructure.persistence.assembler;

import com.algaworks.algashop.ordering.domain.model.entity.ShoppingCart;
import com.algaworks.algashop.ordering.domain.model.entity.ShoppingCartItem;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.CustomerPersistenceEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.ShoppingCartItemPersistenceEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.ShoppingCartPersistenceEntity;
import jakarta.persistence.EntityManager;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ShoppingCartPersistenceEntityAssembler {

  private final EntityManager entityManager;

  public ShoppingCartPersistenceEntity fromDomain(ShoppingCart shoppingCart) {
    return merge(new ShoppingCartPersistenceEntity(), shoppingCart);
  }

  public ShoppingCartPersistenceEntity merge(ShoppingCartPersistenceEntity shoppingCartPersistenceEntity,
      ShoppingCart shoppingCart) {
    shoppingCartPersistenceEntity.setId(shoppingCart.id().value());
    shoppingCartPersistenceEntity.setCustomer(
        entityManager.getReference(CustomerPersistenceEntity.class, shoppingCart.customerId().value()));
    shoppingCartPersistenceEntity.setTotalAmount(shoppingCart.totalAmount().value());
    shoppingCartPersistenceEntity.setTotalItems(shoppingCart.totalItems().value().intValue());

    var items = mergeItems(shoppingCartPersistenceEntity, shoppingCart);
    items.forEach(item -> item.setShoppingCart(shoppingCartPersistenceEntity));
    shoppingCartPersistenceEntity.setItems(items);
    return shoppingCartPersistenceEntity;
  }

  private Set<ShoppingCartItemPersistenceEntity> mergeItems(ShoppingCartPersistenceEntity shoppingCartPersistenceEntity,
      ShoppingCart shoppingCart) {
    Set<ShoppingCartItem> newOrUpdatedItems = shoppingCart.items();
    if (newOrUpdatedItems == null || newOrUpdatedItems.isEmpty()) {
      return new HashSet<>();
    }

    Set<ShoppingCartItemPersistenceEntity> existingItems = shoppingCartPersistenceEntity.getItems();
    if (existingItems == null || existingItems.isEmpty()) {
      return newOrUpdatedItems.stream().map(this::fromDomain).collect(Collectors.toSet());
    }

    var existingItemMap = existingItems.stream()
        .collect(Collectors.toMap(ShoppingCartItemPersistenceEntity::getId, item -> item));

    return newOrUpdatedItems.stream()
        .map(item -> {
          var persistenceItem = existingItemMap.getOrDefault(item.id().value(), new ShoppingCartItemPersistenceEntity());
          return merge(persistenceItem, item);
        })
        .collect(Collectors.toSet());
  }

  public ShoppingCartItemPersistenceEntity fromDomain(ShoppingCartItem shoppingCartItem) {
    return merge(new ShoppingCartItemPersistenceEntity(), shoppingCartItem);
  }

  private ShoppingCartItemPersistenceEntity merge(ShoppingCartItemPersistenceEntity persistenceEntity,
      ShoppingCartItem shoppingCartItem) {
    persistenceEntity.setId(shoppingCartItem.id().value());
    persistenceEntity.setProductId(shoppingCartItem.product().value());
    persistenceEntity.setProductName(shoppingCartItem.productName().value());
    persistenceEntity.setPrice(shoppingCartItem.price().value());
    persistenceEntity.setQuantity(shoppingCartItem.quantity().value().intValue());
    persistenceEntity.setTotalAmount(shoppingCartItem.totalAmount().value());
    persistenceEntity.setAvailable(shoppingCartItem.available());
    return persistenceEntity;
  }
}
