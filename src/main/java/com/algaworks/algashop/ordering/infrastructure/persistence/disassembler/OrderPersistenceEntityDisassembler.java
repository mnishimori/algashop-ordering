package com.algaworks.algashop.ordering.infrastructure.persistence.disassembler;

import com.algaworks.algashop.ordering.domain.model.entity.Order;
import com.algaworks.algashop.ordering.domain.model.entity.OrderItem;
import com.algaworks.algashop.ordering.domain.model.entity.OrderStatus;
import com.algaworks.algashop.ordering.domain.model.entity.PaymentMethod;
import com.algaworks.algashop.ordering.domain.model.valueobject.Address;
import com.algaworks.algashop.ordering.domain.model.valueobject.Billing;
import com.algaworks.algashop.ordering.domain.model.valueobject.Document;
import com.algaworks.algashop.ordering.domain.model.valueobject.Email;
import com.algaworks.algashop.ordering.domain.model.valueobject.FullName;
import com.algaworks.algashop.ordering.domain.model.valueobject.Money;
import com.algaworks.algashop.ordering.domain.model.valueobject.Phone;
import com.algaworks.algashop.ordering.domain.model.valueobject.ProductName;
import com.algaworks.algashop.ordering.domain.model.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.model.valueobject.Recipient;
import com.algaworks.algashop.ordering.domain.model.valueobject.Shipping;
import com.algaworks.algashop.ordering.domain.model.valueobject.ZipCode;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderItemId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.ProductId;
import com.algaworks.algashop.ordering.infrastructure.persistence.embeddable.BillingEmbeddable;
import com.algaworks.algashop.ordering.infrastructure.persistence.embeddable.ShippingEmbeddable;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderItemPersistenceEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import jakarta.annotation.Nullable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class OrderPersistenceEntityDisassembler {

  public Order toDomainEntity(OrderPersistenceEntity orderPersistenceEntity) {
    return Order.existingOrderBuilder()
        .id(new OrderId(orderPersistenceEntity.getId()))
        .customerId(new CustomerId(orderPersistenceEntity.getCustomerId()))
        .status(OrderStatus.valueOf(orderPersistenceEntity.getStatus()))
        .totalAmount(new Money(orderPersistenceEntity.getTotalAmount()))
        .totalItems(new Quantity(new BigDecimal(orderPersistenceEntity.getTotalItems())))
        .paymentMethod(getPaymentMethod(orderPersistenceEntity))
        .placedAt(orderPersistenceEntity.getPlacedAt())
        .paidAt(orderPersistenceEntity.getPaidAt())
        .canceledAt(orderPersistenceEntity.getCanceledAt())
        .readyAt(orderPersistenceEntity.getReadyAt())
        .billing(getBilling(orderPersistenceEntity.getBillingEmbeddable()))
        .shipping(getShipping(orderPersistenceEntity.getShippingEmbeddable()))
        .items(getItems(orderPersistenceEntity.getItems()))
        .version(orderPersistenceEntity.getVersion())
        .build();
  }

  private Set<OrderItem> getItems(Set<OrderItemPersistenceEntity> orderItemPersistenceEntities) {
    var orderItems = new HashSet<OrderItem>();
    if (orderItemPersistenceEntities == null || orderItemPersistenceEntities.isEmpty()) {
      return orderItems;
    }
    orderItemPersistenceEntities.forEach(orderItemPersistenceEntity -> {
      var orderId = new OrderId(orderItemPersistenceEntity.getOrderId());
      var orderItemId = new OrderItemId(orderItemPersistenceEntity.getId());
      var productId = new ProductId(orderItemPersistenceEntity.getProductId());
      var productName = new ProductName(orderItemPersistenceEntity.getProductName());
      var quantity = new Quantity(new BigDecimal(orderItemPersistenceEntity.getQuantity()));
      var price = new Money(orderItemPersistenceEntity.getPrice());
      var orderItem = OrderItem.existingOrderItemBuilder().id(orderItemId).orderId(orderId).productId(productId)
          .productName(productName).quantity(quantity).price(price)
          .build();
      orderItems.add(orderItem);
    });
    return orderItems;
  }

  @Nullable
  private Shipping getShipping(ShippingEmbeddable shippingEmbeddable) {
    if (shippingEmbeddable == null) {
      return null;
    }
    if (shippingEmbeddable.getShippingCost() == null || shippingEmbeddable.getExpectedDeliveryDate() == null) {
      return null;
    }
    var shipping = Shipping.builder();
    shipping.shippingCost(new Money(shippingEmbeddable.getShippingCost()));
    shipping.expectedDeliveryDate(shippingEmbeddable.getExpectedDeliveryDate());
    if (shippingEmbeddable.getRecepient() != null) {
      var recipient = Recipient.builder();
      var firstName = "";
      if (shippingEmbeddable.getRecepient().getFirstName() != null && !shippingEmbeddable.getRecepient().getFirstName()
          .isBlank()) {
        firstName = shippingEmbeddable.getRecepient().getFirstName();
      }
      var lastName = "";
      if (shippingEmbeddable.getRecepient().getLastName() != null && !shippingEmbeddable.getRecepient().getLastName()
          .isBlank()) {
        lastName = shippingEmbeddable.getRecepient().getLastName();
      }
      recipient.fullName(new FullName(firstName, lastName));
      var document = "";
      if (shippingEmbeddable.getRecepient().getDocument() != null && !shippingEmbeddable.getRecepient().getDocument()
          .isBlank()) {
        document = shippingEmbeddable.getRecepient().getDocument();
      }
      recipient.document(new Document(document));
      var phone = "";
      if (shippingEmbeddable.getRecepient().getPhone() != null && !shippingEmbeddable.getRecepient().getPhone()
          .isBlank()) {
        phone = shippingEmbeddable.getRecepient().getPhone();
      }
      recipient.phone(new Phone(phone));
      shipping.recipient(recipient.build());
    }
    if (shippingEmbeddable.getAddress() != null) {
      var address = Address.builder();
      if (shippingEmbeddable.getAddress().getStreet() != null && !shippingEmbeddable.getAddress().getStreet()
          .isBlank()) {
        address.street(shippingEmbeddable.getAddress().getStreet());
      }
      if (shippingEmbeddable.getAddress().getNumber() != null && !shippingEmbeddable.getAddress().getNumber()
          .isBlank()) {
        address.number(shippingEmbeddable.getAddress().getNumber());
      }
      if (shippingEmbeddable.getAddress().getComplement() != null && !shippingEmbeddable.getAddress().getComplement()
          .isBlank()) {
        address.complement(shippingEmbeddable.getAddress().getComplement());
      }
      if (shippingEmbeddable.getAddress().getNeighborhood() != null && !shippingEmbeddable.getAddress()
          .getNeighborhood().isBlank()) {
        address.neighborhood(shippingEmbeddable.getAddress().getNeighborhood());
      }
      if (shippingEmbeddable.getAddress().getCity() != null && !shippingEmbeddable.getAddress().getCity().isBlank()) {
        address.city(shippingEmbeddable.getAddress().getCity());
      }
      if (shippingEmbeddable.getAddress().getState() != null && !shippingEmbeddable.getAddress().getState().isBlank()) {
        address.state(shippingEmbeddable.getAddress().getState());
      }
      if (shippingEmbeddable.getAddress().getZipCode() != null && !shippingEmbeddable.getAddress().getZipCode()
          .isBlank()) {
        address.zipCode(new ZipCode(shippingEmbeddable.getAddress().getZipCode()));
      }
      shipping.address(address.build());
    }
    return shipping.build();
  }

  @Nullable
  private static PaymentMethod getPaymentMethod(OrderPersistenceEntity orderPersistenceEntity) {
    return orderPersistenceEntity.getPaymentMethod() != null
        ? PaymentMethod.valueOf(orderPersistenceEntity.getPaymentMethod())
        : null;
  }

  @Nullable
  private Billing getBilling(BillingEmbeddable billingEmbeddable) {
    if (billingEmbeddable == null) {
      return null;
    }
    if (billingEmbeddable.getAddressEmbeddable() == null || billingEmbeddable.getEmail() == null) {
      return null;
    }
    var billing = Billing.builder();
    var firstName = "";
    if (billingEmbeddable.getFirstName() != null && !billingEmbeddable.getFirstName().isBlank()) {
      firstName = billingEmbeddable.getFirstName();
    }
    var lastName = "";
    if (billingEmbeddable.getLastName() != null && !billingEmbeddable.getLastName().isBlank()) {
      lastName = billingEmbeddable.getLastName();
    }
    billing.fullName(new FullName(firstName, lastName));
    var document = "";
    if (billingEmbeddable.getDocument() != null && !billingEmbeddable.getDocument().isBlank()) {
      document = billingEmbeddable.getDocument();
    }
    billing.document(new Document(document));
    var phone = "";
    if (billingEmbeddable.getPhone() != null && !billingEmbeddable.getPhone().isBlank()) {
      phone = billingEmbeddable.getPhone();
    }
    billing.phone(new Phone(phone));
    var address = Address.builder();
    if (billingEmbeddable.getAddressEmbeddable().getStreet() != null && !billingEmbeddable.getAddressEmbeddable()
        .getStreet().isBlank()) {
      address.street(billingEmbeddable.getAddressEmbeddable().getStreet());
    }
    if (billingEmbeddable.getAddressEmbeddable().getNumber() != null && !billingEmbeddable.getAddressEmbeddable()
        .getNumber().isBlank()) {
      address.number(billingEmbeddable.getAddressEmbeddable().getNumber());
    }
    if (billingEmbeddable.getAddressEmbeddable().getComplement() != null
        && !billingEmbeddable.getAddressEmbeddable().getComplement().isBlank()) {
      address.complement(billingEmbeddable.getAddressEmbeddable().getComplement());
    }
    if (billingEmbeddable.getAddressEmbeddable().getNeighborhood() != null
        && !billingEmbeddable.getAddressEmbeddable().getNeighborhood().isBlank()) {
      address.neighborhood(billingEmbeddable.getAddressEmbeddable().getNeighborhood());
    }
    if (billingEmbeddable.getAddressEmbeddable().getCity() != null && !billingEmbeddable.getAddressEmbeddable()
        .getCity().isBlank()) {
      address.city(billingEmbeddable.getAddressEmbeddable().getCity());
    }
    if (billingEmbeddable.getAddressEmbeddable().getState() != null && !billingEmbeddable.getAddressEmbeddable()
        .getState().isBlank()) {
      address.state(billingEmbeddable.getAddressEmbeddable().getState());
    }
    if (billingEmbeddable.getAddressEmbeddable().getZipCode() != null && !billingEmbeddable.getAddressEmbeddable()
        .getZipCode().isBlank()) {
      address.zipCode(new ZipCode(billingEmbeddable.getAddressEmbeddable().getZipCode()));
    }
    billing.address(address.build());
    if (billingEmbeddable.getEmail() != null && !billingEmbeddable.getEmail().isBlank()) {
      billing.email(new Email(billingEmbeddable.getEmail()));
    }
    return billing.build();
  }

}
