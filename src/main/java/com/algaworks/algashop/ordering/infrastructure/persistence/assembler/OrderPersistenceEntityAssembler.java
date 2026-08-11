package com.algaworks.algashop.ordering.infrastructure.persistence.assembler;

import com.algaworks.algashop.ordering.domain.model.entity.Order;
import com.algaworks.algashop.ordering.domain.model.entity.OrderItem;
import com.algaworks.algashop.ordering.domain.model.valueobject.Address;
import com.algaworks.algashop.ordering.domain.model.valueobject.Recipient;
import com.algaworks.algashop.ordering.infrastructure.persistence.embeddable.AddressEmbeddable;
import com.algaworks.algashop.ordering.infrastructure.persistence.embeddable.RecipientEmbeddable;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.CustomerPersistenceEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderItemPersistenceEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import jakarta.persistence.EntityManager;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderPersistenceEntityAssembler {

  private final EntityManager entityManager;

  public OrderPersistenceEntity fromDomain(Order order) {
    return merge(new OrderPersistenceEntity(), order);
  }

  public OrderPersistenceEntity merge(OrderPersistenceEntity orderPersistenceEntity, Order order) {
    orderPersistenceEntity.setId(order.id().value().toLong());
    orderPersistenceEntity.setCustomer(
        entityManager.getReference(CustomerPersistenceEntity.class, order.customerId().value()));
    orderPersistenceEntity.setTotalAmount(order.totalAmount().value());
    orderPersistenceEntity.setTotalItems(order.totalItems().value().intValue());
    orderPersistenceEntity.setStatus(order.status().name());
    orderPersistenceEntity.setPaymentMethod(getPaymentMethodName(order));
    orderPersistenceEntity.setPlacedAt(order.placedAt());
    orderPersistenceEntity.setPaidAt(order.paidAt());
    orderPersistenceEntity.setCanceledAt(order.canceledAt());
    orderPersistenceEntity.setReadyAt(order.readyAt());
    setBillingInformation(orderPersistenceEntity, order);
    setShippingInformation(orderPersistenceEntity, order);
    var items = mergeItems(orderPersistenceEntity, order);
    orderPersistenceEntity.addOrderIntoItems(items);
    orderPersistenceEntity.setItems(items);
    return orderPersistenceEntity;
  }

  private String getPaymentMethodName(Order order) {
    return order.paymentMethod() != null ? order.paymentMethod().name() : null;
  }

  private void setBillingInformation(OrderPersistenceEntity orderPersistenceEntity, Order order) {
    if (orderPersistenceEntity.getBillingEmbeddable() == null || order.billing() == null) {
      return;
    }
    var billing = order.billing();
    var billingEmbeddable = orderPersistenceEntity.getBillingEmbeddable();

    if (billing.fullName() != null) {
      billingEmbeddable.setFirstName(billing.fullName().firstName());
      billingEmbeddable.setLastName(billing.fullName().lastName());
    }
    if (billing.document() != null) {
      billingEmbeddable.setDocument(billing.document().toString());
    }
    if (billing.phone() != null) {
      billingEmbeddable.setPhone(billing.phone().toString());
    }
    setAddressInformation(billingEmbeddable.getAddressEmbeddable(), billing.address());
  }

  private void setShippingInformation(OrderPersistenceEntity orderPersistenceEntity, Order order) {
    if (orderPersistenceEntity.getShippingEmbeddable() == null || order.shipping() == null) {
      return;
    }
    var shipping = order.shipping();
    var shippingEmbeddable = orderPersistenceEntity.getShippingEmbeddable();

    if (shipping.shippingCost() != null) {
      shippingEmbeddable.setShippingCost(shipping.shippingCost().value());
    }
    shippingEmbeddable.setExpectedDeliveryDate(shipping.expectedDeliveryDate());
    setRecipientInformation(shippingEmbeddable.getRecepient(), shipping.recipient());
    setAddressInformation(shippingEmbeddable.getAddress(), shipping.address());
  }

  private void setRecipientInformation(RecipientEmbeddable recipient, Recipient recipientData) {
    if (recipient == null || recipientData == null) {
      return;
    }
    if (recipientData.fullName() != null) {
      recipient.setFirstName(recipientData.fullName().firstName());
      recipient.setLastName(recipientData.fullName().lastName());
    }
    if (recipientData.document() != null) {
      recipient.setDocument(recipientData.document().toString());
    }
    if (recipientData.phone() != null) {
      recipient.setPhone(recipientData.phone().toString());
    }
  }

  private void setAddressInformation(AddressEmbeddable addressEmbeddable, Address address) {
    if (addressEmbeddable == null || address == null) {
      return;
    }
    addressEmbeddable.setStreet(address.street());
    addressEmbeddable.setNumber(address.number());
    addressEmbeddable.setComplement(address.complement());
    addressEmbeddable.setNeighborhood(address.neighborhood());
    addressEmbeddable.setCity(address.city());
    addressEmbeddable.setState(address.state());
    if (address.zipCode() != null) {
      addressEmbeddable.setZipCode(address.zipCode().toString());
    }
  }

  private Set<OrderItemPersistenceEntity> mergeItems(OrderPersistenceEntity orderPersistenceEntity, Order order) {
    Set<OrderItem> newOrUpdatedItems = order.items();

    if (newOrUpdatedItems == null || newOrUpdatedItems.isEmpty()) {
      return new HashSet<>();
    }

    Set<OrderItemPersistenceEntity> existingItems = orderPersistenceEntity.getItems();
    if (existingItems == null || existingItems.isEmpty()) {
      return newOrUpdatedItems.stream()
          .map(this::fromDomain)
          .collect(Collectors.toSet());
    }

    Map<Long, OrderItemPersistenceEntity> existingItemMap = existingItems.stream()
        .collect(Collectors.toMap(OrderItemPersistenceEntity::getId, item -> item));

    return newOrUpdatedItems.stream()
        .map(orderItem -> {
          OrderItemPersistenceEntity itemPersistence = existingItemMap.getOrDefault(
              orderItem.id().value().toLong(), new OrderItemPersistenceEntity()
          );
          return merge(itemPersistence, orderItem);
        })
        .collect(Collectors.toSet());
  }

  public OrderItemPersistenceEntity fromDomain(OrderItem orderItem) {
    return merge(new OrderItemPersistenceEntity(), orderItem);
  }

  private OrderItemPersistenceEntity merge(OrderItemPersistenceEntity orderItemPersistenceEntity,
      OrderItem orderItem) {
    orderItemPersistenceEntity.setId(orderItem.id().value().toLong());
    orderItemPersistenceEntity.setProductId(orderItem.productId().value());
    orderItemPersistenceEntity.setProductName(orderItem.productName().value());
    orderItemPersistenceEntity.setPrice(orderItem.price().value());
    orderItemPersistenceEntity.setQuantity(orderItem.quantity().value().intValue());
    orderItemPersistenceEntity.setTotalAmount(orderItem.totalAmount().value());
    return orderItemPersistenceEntity;
  }
}
