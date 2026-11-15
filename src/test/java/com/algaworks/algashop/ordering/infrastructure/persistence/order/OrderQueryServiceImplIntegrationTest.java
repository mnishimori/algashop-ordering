package com.algaworks.algashop.ordering.infrastructure.persistence.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.algaworks.algashop.ordering.IntegrationTest;
import com.algaworks.algashop.ordering.application.order.query.OrderFilter;
import com.algaworks.algashop.ordering.application.order.query.OrderItemDetailOutput;
import com.algaworks.algashop.ordering.application.order.query.OrderQueryService;
import com.algaworks.algashop.ordering.application.order.query.OrderSummaryOutput;
import com.algaworks.algashop.ordering.domain.model.IdGenerator;
import com.algaworks.algashop.ordering.domain.model.order.OrderNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

@IntegrationTest
class OrderQueryServiceImplIntegrationTest {

  @Autowired
  private OrderQueryService orderQueryService;

  @Autowired
  private OrderPersistenceEntityRepository repository;

  @Test
  void shouldFindOrderById() {
    var customerId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
    var orderId = IdGenerator.generateTSID().toLong();
    var itemId = IdGenerator.generateTSID().toLong();
    var itemIdAlt = IdGenerator.generateTSID().toLong();

    var customer = new com.algaworks.algashop.ordering.infrastructure.persistence.customer.CustomerPersistenceEntity();
    customer.setId(customerId);
    customer.setFirstName("João");
    customer.setLastName("Silva");
    customer.setEmail("joao.silva@example.com");
    customer.setDocument("12345678901");
    customer.setPhone("11999999999");

    var billingAddress = new com.algaworks.algashop.ordering.infrastructure.persistence.commons.AddressEmbeddable(
        "Bourbon Street",
        "1234",
        "Apt 12",
        "North Valley",
        "New York",
        "NY",
        "12345-678");
    var billing = new BillingEmbeddable(
        "João",
        "Silva",
        "12345678901",
        "11999999999",
        billingAddress,
        "joao.silva@example.com");

    var shippingAddress = new com.algaworks.algashop.ordering.infrastructure.persistence.commons.AddressEmbeddable(
        "Main Street",
        "100",
        "House 2",
        "Downtown",
        "Los Angeles",
        "CA",
        "90001-000");
    var recipient = new com.algaworks.algashop.ordering.infrastructure.persistence.commons.RecipientEmbeddable(
        "Maria",
        "Souza",
        "98765432100",
        "11888888888");
    var shipping = new ShippingEmbeddable(
        new BigDecimal("25.90"),
        LocalDate.of(2026, 11, 20),
        recipient,
        shippingAddress);

    var order = OrderPersistenceEntity.builder()
        .id(orderId)
        .customer(customer)
        .totalItems(3)
        .totalAmount(new BigDecimal("1275.90"))
        .status("PLACED")
        .paymentMethod("CREDIT_CARD")
        .placedAt(OffsetDateTime.parse("2026-11-12T08:00:00-03:00"))
        .paidAt(OffsetDateTime.parse("2026-11-12T08:05:00-03:00"))
        .canceledAt(null)
        .readyAt(OffsetDateTime.parse("2026-11-12T09:00:00-03:00"))
        .billingEmbeddable(billing)
        .shippingEmbeddable(shipping)
        .items(java.util.Set.of(
            OrderItemPersistenceEntity.builder()
                .id(itemId)
                .productId(UUID.fromString("550e8400-e29b-41d4-a716-446655440010"))
                .productName("Notebook")
                .price(new BigDecimal("1000.00"))
                .quantity(2)
                .totalAmount(new BigDecimal("2000.00"))
                .build(),
            OrderItemPersistenceEntity.builder()
                .id(itemIdAlt)
                .productId(UUID.fromString("550e8400-e29b-41d4-a716-446655440011"))
                .productName("Mouse")
                .price(new BigDecimal("250.00"))
                .quantity(1)
                .totalAmount(new BigDecimal("250.00"))
                .build()))
        .build();

    repository.saveAndFlush(order);

    var output = orderQueryService.findById(
        new com.algaworks.algashop.ordering.domain.model.order.OrderId(orderId).toString());

    assertThat(output.getId()).isEqualTo(String.valueOf(orderId));
    assertThat(output.getCustomer()).isNotNull();
    assertThat(output.getCustomer().getId()).isEqualTo(customerId);
    assertThat(output.getCustomer().getFirstName()).isEqualTo("João");
    assertThat(output.getCustomer().getLastName()).isEqualTo("Silva");
    assertThat(output.getCustomer().getEmail()).isEqualTo("joao.silva@example.com");
    assertThat(output.getCustomer().getDocument()).isEqualTo("12345678901");
    assertThat(output.getCustomer().getPhone()).isEqualTo("11999999999");
    assertThat(output.getTotalItems()).isEqualTo(3);
    assertThat(output.getTotalAmount()).isEqualByComparingTo("1275.90");
    assertThat(output.getPlacedAt()).isEqualTo(OffsetDateTime.parse("2026-11-12T08:00:00-03:00"));
    assertThat(output.getPaidAt()).isEqualTo(OffsetDateTime.parse("2026-11-12T08:05:00-03:00"));
    assertThat(output.getCanceledAt()).isNull();
    assertThat(output.getReadyAt()).isEqualTo(OffsetDateTime.parse("2026-11-12T09:00:00-03:00"));
    assertThat(output.getStatus()).isEqualTo("PLACED");
    assertThat(output.getPaymentMethod()).isEqualTo("CREDIT_CARD");
    assertThat(output.getBilling()).isNotNull();
    assertThat(output.getBilling().getFirstName()).isEqualTo("João");
    assertThat(output.getBilling().getLastName()).isEqualTo("Silva");
    assertThat(output.getBilling().getDocument()).isEqualTo("12345678901");
    assertThat(output.getBilling().getEmail()).isEqualTo("joao.silva@example.com");
    assertThat(output.getBilling().getPhone()).isEqualTo("11999999999");
    assertThat(output.getBilling().getAddress()).isNotNull();
    assertThat(output.getBilling().getAddress().getStreet()).isEqualTo("Bourbon Street");
    assertThat(output.getBilling().getAddress().getNumber()).isEqualTo("1234");
    assertThat(output.getBilling().getAddress().getComplement()).isEqualTo("Apt 12");
    assertThat(output.getBilling().getAddress().getNeighbourhood()).isEqualTo("North Valley");
    assertThat(output.getBilling().getAddress().getCity()).isEqualTo("New York");
    assertThat(output.getBilling().getAddress().getState()).isEqualTo("NY");
    assertThat(output.getBilling().getAddress().getZipCode()).isEqualTo("12345-678");
    assertThat(output.getShipping()).isNotNull();
    assertThat(output.getShipping().getCost()).isEqualByComparingTo("25.90");
    assertThat(output.getShipping().getExpectedDate()).isEqualTo(LocalDate.of(2026, 11, 20));
    assertThat(output.getShipping().getRecipient()).isNotNull();
    assertThat(output.getShipping().getRecipient().getFirstName()).isEqualTo("Maria");
    assertThat(output.getShipping().getRecipient().getLastName()).isEqualTo("Souza");
    assertThat(output.getShipping().getRecipient().getDocument()).isEqualTo("98765432100");
    assertThat(output.getShipping().getRecipient().getPhone()).isEqualTo("11888888888");
    assertThat(output.getShipping().getAddress()).isNotNull();
    assertThat(output.getShipping().getAddress().getStreet()).isEqualTo("Main Street");
    assertThat(output.getShipping().getAddress().getNumber()).isEqualTo("100");
    assertThat(output.getShipping().getAddress().getComplement()).isEqualTo("House 2");
    assertThat(output.getShipping().getAddress().getNeighbourhood()).isEqualTo("Downtown");
    assertThat(output.getShipping().getAddress().getCity()).isEqualTo("Los Angeles");
    assertThat(output.getShipping().getAddress().getState()).isEqualTo("CA");
    assertThat(output.getShipping().getAddress().getZipCode()).isEqualTo("90001-000");
    assertThat(output.getItems())
        .hasSize(2)
        .extracting(OrderItemDetailOutput::getProductName)
        .containsExactlyInAnyOrder("Notebook", "Mouse");
  }

  @Test
  void shouldThrowWhenOrderDoesNotExist() {
    var missingOrderId = IdGenerator.generateTSID().toLong();

    assertThatThrownBy(() -> orderQueryService.findById(
        new com.algaworks.algashop.ordering.domain.model.order.OrderId(missingOrderId).toString()))
        .isInstanceOf(OrderNotFoundException.class);
  }

  @Test
  void shouldFilterReturningEmptyPageWhenThereAreNoOrders() {
    repository.deleteAll();
    repository.flush();

    Page<OrderSummaryOutput> page = orderQueryService.filter(new OrderFilter(10, 0));

    assertThat(page.getContent()).isEmpty();
    assertThat(page.getTotalElements()).isZero();
    assertThat(page.getNumber()).isZero();
    assertThat(page.getSize()).isEqualTo(10);
  }

  @Test
  void shouldFilterReturningSummaryOfSavedOrder() {
    repository.deleteAll();
    repository.flush();
    var customerId = UUID.randomUUID();
    var orderId = saveOrder(customerId, 3, "PLACED");

    Page<OrderSummaryOutput> page = orderQueryService.filter(new OrderFilter(10, 0));

    assertThat(page.getTotalElements()).isEqualTo(1);
    assertThat(page.getContent()).hasSize(1);
    var summary = page.getContent().getFirst();
    assertThat(summary.getId())
        .isEqualTo(new com.algaworks.algashop.ordering.domain.model.order.OrderId(orderId).toString());
    assertThat(summary.getCustomer().getId()).isEqualTo(customerId);
    assertThat(summary.getCustomer().getFirstName()).isEqualTo("João");
    assertThat(summary.getCustomer().getLastName()).isEqualTo("Silva");
    assertThat(summary.getTotalItems()).isEqualTo(3);
    assertThat(summary.getTotalAmount()).isEqualByComparingTo("100.00");
    assertThat(summary.getStatus()).isEqualTo("PLACED");
    assertThat(summary.getPaymentMethod()).isEqualTo("CREDIT_CARD");
  }

  @Test
  void shouldFilterPaginatingResults() {
    repository.deleteAll();
    repository.flush();
    IntStream.range(0, 5).forEach(i -> saveOrder(UUID.randomUUID(), i + 1, "PLACED"));

    Page<OrderSummaryOutput> firstPage = orderQueryService.filter(new OrderFilter(2, 0));
    Page<OrderSummaryOutput> lastPage = orderQueryService.filter(new OrderFilter(2, 2));

    assertThat(firstPage.getTotalElements()).isEqualTo(5);
    assertThat(firstPage.getTotalPages()).isEqualTo(3);
    assertThat(firstPage.getContent()).hasSize(2);
    assertThat(lastPage.getContent()).hasSize(1);
    assertThat(lastPage.getNumber()).isEqualTo(2);
  }

  @Test
  void shouldFilterReturningEmptyContentWhenPageIsBeyondLastPage() {
    repository.deleteAll();
    repository.flush();
    saveOrder(UUID.randomUUID(), 1, "PLACED");

    Page<OrderSummaryOutput> page = orderQueryService.filter(new OrderFilter(10, 5));

    assertThat(page.getContent()).isEmpty();
    assertThat(page.getTotalElements()).isEqualTo(1);
  }

  @Test
  void shouldFailToFilterWhenPageSizeIsZero() {
    saveOrder(UUID.randomUUID(), 1, "PLACED");

    assertThatThrownBy(() -> orderQueryService.filter(new OrderFilter(0, 0)))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void shouldFailToFilterWhenPageSizeIsNegative() {
    assertThatThrownBy(() -> orderQueryService.filter(new OrderFilter(-1, 0)))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void shouldFailToFilterWhenPageIndexIsNegative() {
    saveOrder(UUID.randomUUID(), 1, "PLACED");

    assertThatThrownBy(() -> orderQueryService.filter(new OrderFilter(10, -1)))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void shouldFilterByCustomerId() {
    repository.deleteAll();
    repository.flush();
    var customerId = UUID.randomUUID();
    saveOrder(customerId, 1, "PLACED");
    saveOrder(UUID.randomUUID(), 1, "PLACED");

    var filter = new OrderFilter(10, 0);
    filter.setCustomerId(customerId);
    var page = orderQueryService.filter(filter);

    assertThat(page.getTotalElements()).isEqualTo(1);
    assertThat(page.getContent().getFirst().getCustomer().getId()).isEqualTo(customerId);
  }

  @Test
  void shouldFilterByOrderId() {
    repository.deleteAll();
    repository.flush();
    var orderId = saveOrder(UUID.randomUUID(), 1, "PLACED");
    saveOrder(UUID.randomUUID(), 1, "PLACED");

    var filter = new OrderFilter(10, 0);
    filter.setOrderId(new com.algaworks.algashop.ordering.domain.model.order.OrderId(orderId).toString());
    var page = orderQueryService.filter(filter);

    assertThat(page.getTotalElements()).isEqualTo(1);
    assertThat(page.getContent().getFirst().getId())
        .isEqualTo(new com.algaworks.algashop.ordering.domain.model.order.OrderId(orderId).toString());
  }

  @Test
  void shouldFilterByStatusIgnoringCase() {
    repository.deleteAll();
    repository.flush();
    saveOrder(UUID.randomUUID(), 1, "PLACED");
    saveOrder(UUID.randomUUID(), 1, "PAID");

    var filter = new OrderFilter(10, 0);
    filter.setStatus("paid");
    var page = orderQueryService.filter(filter);

    assertThat(page.getTotalElements()).isEqualTo(1);
    assertThat(page.getContent().getFirst().getStatus()).isEqualTo("PAID");
  }

  @Test
  void shouldIgnoreBlankStatus() {
    repository.deleteAll();
    repository.flush();
    saveOrder(UUID.randomUUID(), 1, "PLACED");
    saveOrder(UUID.randomUUID(), 1, "PAID");

    var filter = new OrderFilter(10, 0);
    filter.setStatus(" ");

    assertThat(orderQueryService.filter(filter).getTotalElements()).isEqualTo(2);
  }

  @Test
  void shouldFilterByPlacedAtRange() {
    repository.deleteAll();
    repository.flush();
    saveOrder(UUID.randomUUID(), 1, "PLACED", new BigDecimal("10.00"),
        OffsetDateTime.parse("2026-01-10T10:00:00-03:00"));
    saveOrder(UUID.randomUUID(), 1, "PLACED", new BigDecimal("10.00"),
        OffsetDateTime.parse("2026-02-10T10:00:00-03:00"));
    saveOrder(UUID.randomUUID(), 1, "PLACED", new BigDecimal("10.00"),
        OffsetDateTime.parse("2026-03-10T10:00:00-03:00"));

    var filter = new OrderFilter(10, 0);
    filter.setPlacedAtFrom(OffsetDateTime.parse("2026-02-01T00:00:00-03:00"));
    filter.setPlacedAtTo(OffsetDateTime.parse("2026-02-28T23:59:59-03:00"));
    var page = orderQueryService.filter(filter);

    assertThat(page.getTotalElements()).isEqualTo(1);
    assertThat(page.getContent().getFirst().getPlacedAt().toInstant())
        .isEqualTo(OffsetDateTime.parse("2026-02-10T10:00:00-03:00").toInstant());
  }

  @Test
  void shouldFilterByPlacedAtFromOnly() {
    repository.deleteAll();
    repository.flush();
    saveOrder(UUID.randomUUID(), 1, "PLACED", new BigDecimal("10.00"),
        OffsetDateTime.parse("2026-01-10T10:00:00-03:00"));
    saveOrder(UUID.randomUUID(), 1, "PLACED", new BigDecimal("10.00"),
        OffsetDateTime.parse("2026-03-10T10:00:00-03:00"));

    var filter = new OrderFilter(10, 0);
    filter.setPlacedAtFrom(OffsetDateTime.parse("2026-02-01T00:00:00-03:00"));

    assertThat(orderQueryService.filter(filter).getTotalElements()).isEqualTo(1);
  }

  @Test
  void shouldFilterByTotalAmountRange() {
    repository.deleteAll();
    repository.flush();
    var placedAt = OffsetDateTime.parse("2026-11-12T08:00:00-03:00");
    saveOrder(UUID.randomUUID(), 1, "PLACED", new BigDecimal("50.00"), placedAt);
    saveOrder(UUID.randomUUID(), 1, "PLACED", new BigDecimal("150.00"), placedAt);
    saveOrder(UUID.randomUUID(), 1, "PLACED", new BigDecimal("300.00"), placedAt);

    var filter = new OrderFilter(10, 0);
    filter.setTotalAmountFrom(new BigDecimal("100.00"));
    filter.setTotalAmountTo(new BigDecimal("200.00"));
    var page = orderQueryService.filter(filter);

    assertThat(page.getTotalElements()).isEqualTo(1);
    assertThat(page.getContent().getFirst().getTotalAmount()).isEqualByComparingTo("150.00");
  }

  @Test
  void shouldFilterByTotalAmountFromOnly() {
    repository.deleteAll();
    repository.flush();
    var placedAt = OffsetDateTime.parse("2026-11-12T08:00:00-03:00");
    saveOrder(UUID.randomUUID(), 1, "PLACED", new BigDecimal("50.00"), placedAt);
    saveOrder(UUID.randomUUID(), 1, "PLACED", new BigDecimal("150.00"), placedAt);

    var filter = new OrderFilter(10, 0);
    filter.setTotalAmountFrom(new BigDecimal("150.00"));

    assertThat(orderQueryService.filter(filter).getTotalElements()).isEqualTo(1);
  }

  @Test
  void shouldCombineFiltersAndReturnEmptyWhenNothingMatches() {
    repository.deleteAll();
    repository.flush();
    var customerId = UUID.randomUUID();
    saveOrder(customerId, 1, "PLACED");

    var filter = new OrderFilter(10, 0);
    filter.setCustomerId(customerId);
    filter.setStatus("PAID");
    var page = orderQueryService.filter(filter);

    assertThat(page.getContent()).isEmpty();
    assertThat(page.getTotalElements()).isZero();
  }

  @Test
  void shouldFilterByPlacedAtToOnly() {
    repository.deleteAll();
    repository.flush();
    saveOrder(UUID.randomUUID(), 1, "PLACED", new BigDecimal("50.00"),
        OffsetDateTime.parse("2026-01-10T10:00:00-03:00"));
    saveOrder(UUID.randomUUID(), 1, "PLACED", new BigDecimal("50.00"),
        OffsetDateTime.parse("2026-03-10T10:00:00-03:00"));

    var filter = new OrderFilter(10, 0);
    filter.setPlacedAtTo(OffsetDateTime.parse("2026-01-10T10:00:00-03:00"));

    var page = orderQueryService.filter(filter);

    assertThat(page.getTotalElements()).isEqualTo(1);
    assertThat(page.getContent().getFirst().getPlacedAt())
        .isEqualTo(OffsetDateTime.parse("2026-01-10T10:00:00-03:00"));
  }

  @Test
  void shouldFilterByTotalAmountToOnly() {
    repository.deleteAll();
    repository.flush();
    var placedAt = OffsetDateTime.parse("2026-11-12T08:00:00-03:00");
    saveOrder(UUID.randomUUID(), 1, "PLACED", new BigDecimal("50.00"), placedAt);
    saveOrder(UUID.randomUUID(), 1, "PLACED", new BigDecimal("100.00"), placedAt);
    saveOrder(UUID.randomUUID(), 1, "PLACED", new BigDecimal("150.00"), placedAt);

    var filter = new OrderFilter(10, 0);
    filter.setTotalAmountTo(new BigDecimal("100.00"));

    var page = orderQueryService.filter(filter);

    assertThat(page.getTotalElements()).isEqualTo(2);
    assertThat(page.getContent()).extracting(OrderSummaryOutput::getTotalAmount)
        .containsExactly(new BigDecimal("50.00"), new BigDecimal("100.00"));
  }

  @Test
  void shouldIncludeBoundaryValuesInRangeFilters() {
    repository.deleteAll();
    repository.flush();
    saveOrder(UUID.randomUUID(), 1, "PLACED", new BigDecimal("100.00"),
        OffsetDateTime.parse("2026-02-01T00:00:00-03:00"));
    saveOrder(UUID.randomUUID(), 1, "PLACED", new BigDecimal("150.00"),
        OffsetDateTime.parse("2026-02-15T10:00:00-03:00"));
    saveOrder(UUID.randomUUID(), 1, "PLACED", new BigDecimal("200.00"),
        OffsetDateTime.parse("2026-02-28T23:59:59-03:00"));
    saveOrder(UUID.randomUUID(), 1, "PLACED", new BigDecimal("250.00"),
        OffsetDateTime.parse("2026-03-01T00:00:00-03:00"));

    var filter = new OrderFilter(10, 0);
    filter.setPlacedAtFrom(OffsetDateTime.parse("2026-02-01T00:00:00-03:00"));
    filter.setPlacedAtTo(OffsetDateTime.parse("2026-02-28T23:59:59-03:00"));
    filter.setTotalAmountFrom(new BigDecimal("100.00"));
    filter.setTotalAmountTo(new BigDecimal("200.00"));

    var page = orderQueryService.filter(filter);

    assertThat(page.getTotalElements()).isEqualTo(3);
    assertThat(page.getContent()).extracting(OrderSummaryOutput::getTotalAmount)
        .containsExactly(new BigDecimal("100.00"), new BigDecimal("150.00"), new BigDecimal("200.00"));
    assertThat(page.getContent()).extracting(OrderSummaryOutput::getPlacedAt)
        .containsExactly(
            OffsetDateTime.parse("2026-02-01T00:00:00-03:00"),
            OffsetDateTime.parse("2026-02-15T10:00:00-03:00"),
            OffsetDateTime.parse("2026-02-28T23:59:59-03:00"));
  }

  @Test
  void shouldFailToFilterWhenOrderIdIsInvalid() {
    var filter = new OrderFilter(10, 0);
    filter.setOrderId("invalid-order-id");

    assertThatThrownBy(() -> orderQueryService.filter(filter))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void shouldReturnEmptyPageWhenOrderIdDoesNotExist() {
    repository.deleteAll();
    repository.flush();
    saveOrder(UUID.randomUUID(), 1, "PLACED");

    var filter = new OrderFilter(10, 0);
    filter.setOrderId(new com.algaworks.algashop.ordering.domain.model.order.OrderId(
        IdGenerator.generateTSID().toLong()).toString());

    var page = orderQueryService.filter(filter);

    assertThat(page.getContent()).isEmpty();
    assertThat(page.getTotalElements()).isZero();
  }

  @Test
  void shouldFilterAndCountOnlyMatchingResultsWhenPaginating() {
    repository.deleteAll();
    repository.flush();
    saveOrder(UUID.randomUUID(), 1, "PAID");
    saveOrder(UUID.randomUUID(), 2, "PAID");
    saveOrder(UUID.randomUUID(), 3, "PAID");
    saveOrder(UUID.randomUUID(), 4, "CANCELED");
    saveOrder(UUID.randomUUID(), 5, "PLACED");

    var filter = new OrderFilter(2, 0);
    filter.setStatus("PAID");

    var firstPage = orderQueryService.filter(filter);

    filter.setPage(1);
    var secondPage = orderQueryService.filter(filter);

    assertThat(firstPage.getTotalElements()).isEqualTo(3);
    assertThat(firstPage.getTotalPages()).isEqualTo(2);
    assertThat(firstPage.getContent()).hasSize(2);
    assertThat(secondPage.getContent()).hasSize(1);
    assertThat(secondPage.getTotalElements()).isEqualTo(3);
  }

  @Test
  void shouldSortResultsByRequestedPropertyAndDirection() {
    repository.deleteAll();
    repository.flush();
    saveOrder(UUID.randomUUID(), 1, "PAID", new BigDecimal("100.00"),
        OffsetDateTime.parse("2026-11-10T08:00:00-03:00"));
    saveOrder(UUID.randomUUID(), 1, "CANCELED", new BigDecimal("100.00"),
        OffsetDateTime.parse("2026-11-11T08:00:00-03:00"));
    saveOrder(UUID.randomUUID(), 1, "PLACED", new BigDecimal("100.00"),
        OffsetDateTime.parse("2026-11-12T08:00:00-03:00"));

    var filter = new OrderFilter(10, 0);
    filter.setSortByProperty(OrderFilter.SortType.PLACED_AT);
    filter.setSortDirection(Sort.Direction.DESC);

    var page = orderQueryService.filter(filter);

    assertThat(page.getContent()).hasSize(3);
    assertThat(page.getContent().get(0).getPlacedAt())
        .isAfter(page.getContent().get(1).getPlacedAt());
    assertThat(page.getContent().get(1).getPlacedAt())
        .isAfter(page.getContent().get(2).getPlacedAt());
  }

  private Long saveOrder(UUID customerId, int totalItems, String status) {
    return saveOrder(customerId, totalItems, status, new BigDecimal("100.00"),
        OffsetDateTime.parse("2026-11-12T08:00:00-03:00"));
  }

  private Long saveOrder(UUID customerId, int totalItems, String status, BigDecimal totalAmount,
      OffsetDateTime placedAt) {
    var orderId = IdGenerator.generateTSID().toLong();

    var customer = new com.algaworks.algashop.ordering.infrastructure.persistence.customer.CustomerPersistenceEntity();
    customer.setId(customerId);
    customer.setFirstName("João");
    customer.setLastName("Silva");
    customer.setEmail(customerId + "@example.com");
    customer.setDocument("12345678901");
    customer.setPhone("11999999999");

    var address = new com.algaworks.algashop.ordering.infrastructure.persistence.commons.AddressEmbeddable(
        "Bourbon Street", "1234", "Apt 12", "North Valley", "New York", "NY", "12345-678");
    var billing = new BillingEmbeddable(
        "João", "Silva", "12345678901", "11999999999", address, "joao.silva@example.com");
    var recipient = new com.algaworks.algashop.ordering.infrastructure.persistence.commons.RecipientEmbeddable(
        "Maria", "Souza", "98765432100", "11888888888");
    var shipping = new ShippingEmbeddable(
        new BigDecimal("25.90"), LocalDate.of(2026, 11, 20), recipient, address);

    var order = OrderPersistenceEntity.builder()
        .id(orderId)
        .customer(customer)
        .totalItems(totalItems)
        .totalAmount(totalAmount)
        .status(status)
        .paymentMethod("CREDIT_CARD")
        .placedAt(placedAt)
        .billingEmbeddable(billing)
        .shippingEmbeddable(shipping)
        .items(java.util.Set.of())
        .build();

    repository.saveAndFlush(order);
    return orderId;
  }
}
