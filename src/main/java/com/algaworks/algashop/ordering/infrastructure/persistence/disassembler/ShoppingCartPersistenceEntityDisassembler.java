package com.algaworks.algashop.ordering.infrastructure.persistence.disassembler;

import com.algaworks.algashop.ordering.domain.model.entity.ShoppingCart;
import com.algaworks.algashop.ordering.domain.model.entity.ShoppingCartItem;
import com.algaworks.algashop.ordering.domain.model.valueobject.Money;
import com.algaworks.algashop.ordering.domain.model.valueobject.ProductName;
import com.algaworks.algashop.ordering.domain.model.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.ShoppingCartId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.ShoppingCartItemId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.ProductId;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.ShoppingCartItemPersistenceEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.ShoppingCartPersistenceEntity;
import java.util.HashSet;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class ShoppingCartPersistenceEntityDisassembler {

  public ShoppingCart toDomainEntity(ShoppingCartPersistenceEntity shoppingCartPersistenceEntity) {
    return ShoppingCart.existingShoppingCartBuilder()
        .shoppingCartId(new ShoppingCartId(shoppingCartPersistenceEntity.getId()))
        .customerId(new CustomerId(shoppingCartPersistenceEntity.getCustomer().getId()))
        .totalAmount(new Money(shoppingCartPersistenceEntity.getTotalAmount()))
        .totalItems(new Quantity(new java.math.BigDecimal(shoppingCartPersistenceEntity.getTotalItems())))
        .createdAt(shoppingCartPersistenceEntity.getCreatedAt())
        .items(getItems(shoppingCartPersistenceEntity.getItems()))
        .build();
  }

  private Set<ShoppingCartItem> getItems(Set<ShoppingCartItemPersistenceEntity> shoppingCartItemPersistenceEntities) {
    var shoppingCartItems = new HashSet<ShoppingCartItem>();
    if (shoppingCartItemPersistenceEntities == null || shoppingCartItemPersistenceEntities.isEmpty()) {
      return shoppingCartItems;
    }

    shoppingCartItemPersistenceEntities.forEach(shoppingCartItemPersistenceEntity -> {
      var shoppingCartItem = ShoppingCartItem.existingShoppingCartItemBuilder()
          .id(new ShoppingCartItemId(shoppingCartItemPersistenceEntity.getId()))
          .shoppingCartId(new ShoppingCartId(shoppingCartItemPersistenceEntity.getShoppingCart().getId()))
          .product(new ProductId(shoppingCartItemPersistenceEntity.getProductId()))
          .productName(new ProductName(shoppingCartItemPersistenceEntity.getProductName()))
          .price(new Money(shoppingCartItemPersistenceEntity.getPrice()))
          .quantity(new Quantity(new java.math.BigDecimal(shoppingCartItemPersistenceEntity.getQuantity())))
          .totalAmount(new Money(shoppingCartItemPersistenceEntity.getTotalAmount()))
          .available(shoppingCartItemPersistenceEntity.getAvailable())
          .build();
      shoppingCartItems.add(shoppingCartItem);
    });

    return shoppingCartItems;
  }
}
