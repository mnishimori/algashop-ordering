package com.algaworks.algashop.ordering.infrastructure.persistence.order;

import com.algaworks.algashop.ordering.application.commons.AddressData;
import com.algaworks.algashop.ordering.application.order.query.BillingData;
import com.algaworks.algashop.ordering.application.order.query.CustomerMinimalOutput;
import com.algaworks.algashop.ordering.application.order.query.OrderDetailOutput;
import com.algaworks.algashop.ordering.application.order.query.OrderItemDetailOutput;
import com.algaworks.algashop.ordering.application.order.query.OrderQueryService;
import com.algaworks.algashop.ordering.application.order.query.OrderSummaryOutput;
import com.algaworks.algashop.ordering.application.order.query.RecipientData;
import com.algaworks.algashop.ordering.application.order.query.ShippingData;
import com.algaworks.algashop.ordering.application.utility.PageFilter;
import com.algaworks.algashop.ordering.domain.model.order.OrderId;
import com.algaworks.algashop.ordering.domain.model.order.OrderNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderQueryServiceImpl implements OrderQueryService {

  private final OrderPersistenceEntityRepository repository;
  private final EntityManager entityManager;

  @Override
  public OrderDetailOutput findById(String id) {
    var orderId = new OrderId(id);
    var order = repository.findById(orderId.value().toLong()).orElseThrow(() -> new OrderNotFoundException(orderId));
    return new OrderDetailOutput(
        order.getId().toString(),
        mapCustomer(order.getCustomer()),
        order.getTotalItems(),
        order.getTotalAmount(),
        order.getPlacedAt(),
        order.getPaidAt(),
        order.getCanceledAt(),
        order.getReadyAt(),
        order.getStatus(),
        order.getPaymentMethod(),
        mapShipping(order.getShippingEmbeddable()),
        mapBilling(order.getBillingEmbeddable()),
        mapItems(order.getItems()));
  }

  private CustomerMinimalOutput mapCustomer(
      com.algaworks.algashop.ordering.infrastructure.persistence.customer.CustomerPersistenceEntity customer) {
    if (customer == null) {
      return null;
    }
    return CustomerMinimalOutput.builder()
        .id(customer.getId())
        .firstName(customer.getFirstName())
        .lastName(customer.getLastName())
        .email(customer.getEmail())
        .document(customer.getDocument())
        .phone(customer.getPhone())
        .build();
  }

  private BillingData mapBilling(BillingEmbeddable billingEmbeddable) {
    if (billingEmbeddable == null) {
      return null;
    }
    return BillingData.builder()
        .firstName(billingEmbeddable.getFirstName())
        .lastName(billingEmbeddable.getLastName())
        .document(billingEmbeddable.getDocument())
        .email(billingEmbeddable.getEmail())
        .phone(billingEmbeddable.getPhone())
        .address(mapAddress(billingEmbeddable.getAddressEmbeddable()))
        .build();
  }

  private ShippingData mapShipping(ShippingEmbeddable shippingEmbeddable) {
    if (shippingEmbeddable == null) {
      return null;
    }
    return ShippingData.builder()
        .cost(shippingEmbeddable.getShippingCost())
        .expectedDate(shippingEmbeddable.getExpectedDeliveryDate())
        .recipient(mapRecipient(shippingEmbeddable.getRecepient()))
        .address(mapAddress(shippingEmbeddable.getAddress()))
        .build();
  }

  private RecipientData mapRecipient(
      com.algaworks.algashop.ordering.infrastructure.persistence.commons.RecipientEmbeddable recipientEmbeddable) {
    if (recipientEmbeddable == null) {
      return null;
    }
    return RecipientData.builder()
        .firstName(recipientEmbeddable.getFirstName())
        .lastName(recipientEmbeddable.getLastName())
        .document(recipientEmbeddable.getDocument())
        .phone(recipientEmbeddable.getPhone())
        .build();
  }

  private AddressData mapAddress(
      com.algaworks.algashop.ordering.infrastructure.persistence.commons.AddressEmbeddable addressEmbeddable) {
    if (addressEmbeddable == null) {
      return null;
    }
    return AddressData.builder()
        .street(addressEmbeddable.getStreet())
        .number(addressEmbeddable.getNumber())
        .complement(addressEmbeddable.getComplement())
        .neighbourhood(addressEmbeddable.getNeighborhood())
        .city(addressEmbeddable.getCity())
        .state(addressEmbeddable.getState())
        .zipCode(addressEmbeddable.getZipCode())
        .build();
  }

  private List<OrderItemDetailOutput> mapItems(java.util.Set<OrderItemPersistenceEntity> items) {
    if (items == null || items.isEmpty()) {
      return List.of();
    }
    return items.stream()
        .map(item -> OrderItemDetailOutput.builder()
            .id(String.valueOf(item.getId()))
            .orderId(String.valueOf(item.getOrderId()))
            .productId(item.getProductId())
            .productName(item.getProductName())
            .price(item.getPrice())
            .quantity(item.getQuantity())
            .totalAmount(item.getTotalAmount())
            .build())
        .toList();
  }

  @Override
  public Page<OrderSummaryOutput> filter(PageFilter filter) {
    var totalQueryResults = countTotalQueryResults(filter);

    if (totalQueryResults.equals(0L)) {
      var pageRequest = PageRequest.of(filter.getPage(), filter.getSize());
      return new PageImpl<>(new ArrayList<>(), pageRequest, totalQueryResults);
    }

    return filterQuery(filter, totalQueryResults);
  }

  private Long countTotalQueryResults(PageFilter filter) {
    var builder = entityManager.getCriteriaBuilder();
    var criteriaQuery = builder.createQuery(Long.class);
    var root = criteriaQuery.from(OrderPersistenceEntity.class);
    var count = builder.count(root);
    criteriaQuery.select(count);
    var query = entityManager.createQuery(criteriaQuery);
    return query.getSingleResult();
  }

  private Page<OrderSummaryOutput> filterQuery(PageFilter filter, Long totalQueryResults) {
    var builder = entityManager.getCriteriaBuilder();
    var criteriaQuery = builder.createQuery(OrderSummaryOutput.class);
    var root = criteriaQuery.from(OrderPersistenceEntity.class);
    var customer = root.get("customer");

    criteriaQuery.select(
        builder.construct(OrderSummaryOutput.class,
            root.get("id"),
            builder.construct(CustomerMinimalOutput.class,
                customer.get("id"),
                customer.get("firstName"),
                customer.get("lastName"),
                customer.get("email"),
                customer.get("document"),
                customer.get("phone")
            ),
            root.get("totalItems"),
            root.get("totalAmount"),
            root.get("placedAt"),
            root.get("paidAt"),
            root.get("canceledAt"),
            root.get("readyAt"),
            root.get("status"),
            root.get("paymentMethod")
        )
    );

    var typedQuery = entityManager.createQuery(criteriaQuery);
    typedQuery.setFirstResult(filter.getSize() * filter.getPage());
    typedQuery.setMaxResults(filter.getSize());
    var pageRequest = PageRequest.of(filter.getPage(), filter.getSize());
    return new PageImpl<>(typedQuery.getResultList(), pageRequest, totalQueryResults);
  }
}
