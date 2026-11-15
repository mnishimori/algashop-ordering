package com.algaworks.algashop.ordering.infrastructure.persistence.customer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.algaworks.algashop.ordering.IntegrationTest;
import com.algaworks.algashop.ordering.application.customer.query.CustomerFilter;
import com.algaworks.algashop.ordering.application.customer.query.CustomerQueryService;
import com.algaworks.algashop.ordering.application.customer.query.CustomerSummaryOutput;
import com.algaworks.algashop.ordering.infrastructure.persistence.order.OrderPersistenceEntityRepository;
import java.time.LocalDate;
import java.util.UUID;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

@IntegrationTest
class CustomerQueryServiceImplIntegrationTest {

  @Autowired
  private CustomerQueryService customerQueryService;

  @Autowired
  private CustomerPersistenceEntityRepository customerRepository;

  @Autowired
  private OrderPersistenceEntityRepository orderRepository;

  @BeforeEach
  void cleanDatabase() {
    orderRepository.deleteAll();
    orderRepository.flush();
    customerRepository.deleteAll();
    customerRepository.flush();
  }

  @Test
  void shouldFilterReturningEmptyPageWhenThereAreNoCustomers() {
    Page<CustomerSummaryOutput> page = customerQueryService.filter(new CustomerFilter(10, 0));

    assertThat(page.getContent()).isEmpty();
    assertThat(page.getTotalElements()).isZero();
    assertThat(page.getNumber()).isZero();
    assertThat(page.getSize()).isEqualTo(10);
  }

  @Test
  void shouldFilterReturningSummaryOfSavedCustomer() {
    var id = saveCustomer("John", "john@example.com");

    Page<CustomerSummaryOutput> page = customerQueryService.filter(new CustomerFilter(10, 0));

    assertThat(page.getTotalElements()).isEqualTo(1);
    var summary = page.getContent().getFirst();
    assertThat(summary.getId()).isEqualTo(id);
    assertThat(summary.getFirstName()).isEqualTo("John");
    assertThat(summary.getLastName()).isEqualTo("Doe");
    assertThat(summary.getEmail()).isEqualTo("john@example.com");
    assertThat(summary.getDocument()).isEqualTo("12345678901");
    assertThat(summary.getPhone()).isEqualTo("11999999999");
    assertThat(summary.getBirthDate()).isEqualTo(LocalDate.of(1990, 1, 1));
    assertThat(summary.getLoyaltyPoints()).isEqualTo(10);
    assertThat(summary.getPromotionNotificationsAllowed()).isTrue();
    assertThat(summary.getArchived()).isFalse();
  }

  @Test
  void shouldFilterPaginatingResults() {
    IntStream.range(0, 5).forEach(i -> saveCustomer("Name" + i, "c" + i + "@example.com"));

    Page<CustomerSummaryOutput> firstPage = customerQueryService.filter(new CustomerFilter(2, 0));
    Page<CustomerSummaryOutput> lastPage = customerQueryService.filter(new CustomerFilter(2, 2));

    assertThat(firstPage.getTotalElements()).isEqualTo(5);
    assertThat(firstPage.getTotalPages()).isEqualTo(3);
    assertThat(firstPage.getContent()).hasSize(2);
    assertThat(lastPage.getContent()).hasSize(1);
    assertThat(lastPage.getNumber()).isEqualTo(2);
  }

  @Test
  void shouldFilterReturningEmptyContentWhenPageIsBeyondLastPage() {
    saveCustomer("John", "john@example.com");

    Page<CustomerSummaryOutput> page = customerQueryService.filter(new CustomerFilter(10, 5));

    assertThat(page.getContent()).isEmpty();
    assertThat(page.getTotalElements()).isEqualTo(1);
  }

  @Test
  void shouldFilterByEmailIgnoringCaseAndPartialMatch() {
    saveCustomer("John", "john@example.com");
    saveCustomer("Mary", "mary@other.org");

    var filter = new CustomerFilter(10, 0);
    filter.setEmail("EXAMPLE.com");
    var page = customerQueryService.filter(filter);

    assertThat(page.getTotalElements()).isEqualTo(1);
    assertThat(page.getContent().getFirst().getEmail()).isEqualTo("john@example.com");
  }

  @Test
  void shouldFilterByFirstNameIgnoringCaseAndPartialMatch() {
    saveCustomer("Johnathan", "a@example.com");
    saveCustomer("Mary", "b@example.com");

    var filter = new CustomerFilter(10, 0);
    filter.setFirstName("JOHN");
    var page = customerQueryService.filter(filter);

    assertThat(page.getTotalElements()).isEqualTo(1);
    assertThat(page.getContent().getFirst().getFirstName()).isEqualTo("Johnathan");
  }

  @Test
  void shouldCombineEmailAndFirstNameFilters() {
    saveCustomer("John", "john@example.com");
    saveCustomer("John", "john@other.org");
    saveCustomer("Mary", "mary@example.com");

    var filter = new CustomerFilter(10, 0);
    filter.setFirstName("John");
    filter.setEmail("example");
    var page = customerQueryService.filter(filter);

    assertThat(page.getTotalElements()).isEqualTo(1);
    assertThat(page.getContent().getFirst().getEmail()).isEqualTo("john@example.com");
  }

  @Test
  void shouldIgnoreBlankFilters() {
    saveCustomer("John", "john@example.com");
    saveCustomer("Mary", "mary@example.com");

    var filter = new CustomerFilter(10, 0);
    filter.setEmail(" ");
    filter.setFirstName("");

    assertThat(customerQueryService.filter(filter).getTotalElements()).isEqualTo(2);
  }

  @Test
  void shouldReturnEmptyPageWhenNothingMatchesFilters() {
    saveCustomer("John", "john@example.com");

    var filter = new CustomerFilter(10, 0);
    filter.setFirstName("John");
    filter.setEmail("nomatch");
    var page = customerQueryService.filter(filter);

    assertThat(page.getContent()).isEmpty();
    assertThat(page.getTotalElements()).isZero();
  }

  @Test
  void shouldFilterAndCountOnlyMatchingResultsWhenPaginating() {
    saveCustomer("John1", "j1@example.com");
    saveCustomer("John2", "j2@example.com");
    saveCustomer("John3", "j3@example.com");
    saveCustomer("Mary", "m@example.com");

    var filter = new CustomerFilter(2, 0);
    filter.setFirstName("John");
    var firstPage = customerQueryService.filter(filter);
    filter.setPage(1);
    var secondPage = customerQueryService.filter(filter);

    assertThat(firstPage.getTotalElements()).isEqualTo(3);
    assertThat(firstPage.getTotalPages()).isEqualTo(2);
    assertThat(firstPage.getContent()).hasSize(2);
    assertThat(secondPage.getContent()).hasSize(1);
  }

  @Test
  void shouldSortByFirstNameAscendingByRequest() {
    saveCustomer("Charlie", "c@example.com");
    saveCustomer("Alice", "a@example.com");
    saveCustomer("Bob", "b@example.com");

    var filter = new CustomerFilter(10, 0);
    filter.setSortByProperty(CustomerFilter.SortType.FIRST_NAME);
    filter.setSortDirection(Sort.Direction.ASC);
    var page = customerQueryService.filter(filter);

    assertThat(page.getContent()).extracting(CustomerSummaryOutput::getFirstName)
        .containsExactly("Alice", "Bob", "Charlie");
  }

  @Test
  void shouldSortByFirstNameDescendingByRequest() {
    saveCustomer("Charlie", "c@example.com");
    saveCustomer("Alice", "a@example.com");
    saveCustomer("Bob", "b@example.com");

    var filter = new CustomerFilter(10, 0);
    filter.setSortByProperty(CustomerFilter.SortType.FIRST_NAME);
    filter.setSortDirection(Sort.Direction.DESC);
    var page = customerQueryService.filter(filter);

    assertThat(page.getContent()).extracting(CustomerSummaryOutput::getFirstName)
        .containsExactly("Charlie", "Bob", "Alice");
  }

  @Test
  void shouldUseDefaultSortWhenNoSortIsProvided() {
    saveCustomer("Alice", "a@example.com");
    saveCustomer("Bob", "b@example.com");

    var page = customerQueryService.filter(new CustomerFilter(10, 0));

    assertThat(page.getContent()).hasSize(2);
  }

  @Test
  void shouldFailToFilterWhenPageSizeIsZero() {
    saveCustomer("John", "john@example.com");

    assertThatThrownBy(() -> customerQueryService.filter(new CustomerFilter(0, 0)))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void shouldFailToFilterWhenPageSizeIsNegative() {
    assertThatThrownBy(() -> customerQueryService.filter(new CustomerFilter(-1, 0)))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void shouldFailToFilterWhenPageIndexIsNegative() {
    saveCustomer("John", "john@example.com");

    assertThatThrownBy(() -> customerQueryService.filter(new CustomerFilter(10, -1)))
        .isInstanceOf(IllegalArgumentException.class);
  }

  private UUID saveCustomer(String firstName, String email) {
    var customer = new CustomerPersistenceEntity();
    customer.setId(UUID.randomUUID());
    customer.setFirstName(firstName);
    customer.setLastName("Doe");
    customer.setEmail(email);
    customer.setDocument("12345678901");
    customer.setPhone("11999999999");
    customer.setBirthDate(LocalDate.of(1990, 1, 1));
    customer.setLoyaltyPoints(10);
    customer.setPromotionNotificationsAllowed(true);
    customer.setArchived(false);
    customerRepository.saveAndFlush(customer);
    return customer.getId();
  }
}
