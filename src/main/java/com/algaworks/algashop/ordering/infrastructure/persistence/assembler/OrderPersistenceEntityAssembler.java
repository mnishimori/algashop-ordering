package com.algaworks.algashop.ordering.infrastructure.persistence.assembler;

import com.algaworks.algashop.ordering.domain.model.entity.Order;
import com.algaworks.algashop.ordering.domain.model.valueobject.Address;
import com.algaworks.algashop.ordering.domain.model.valueobject.Recipient;
import com.algaworks.algashop.ordering.infrastructure.persistence.embeddable.AddressEmbeddable;
import com.algaworks.algashop.ordering.infrastructure.persistence.embeddable.RecipientEmbeddable;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import org.springframework.stereotype.Component;

@Component
public class OrderPersistenceEntityAssembler {

  public OrderPersistenceEntity fromDomain(Order order) {
    return merge(new OrderPersistenceEntity(), order);
  }

  public OrderPersistenceEntity merge(OrderPersistenceEntity orderPersistenceEntity, Order order) {
    orderPersistenceEntity.setId(order.id().value().toLong());
    orderPersistenceEntity.setCustomerId(order.customerId().value());
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
}
