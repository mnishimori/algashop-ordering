package com.algaworks.algashop.ordering.infrastructure.persistence.provider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

import com.algaworks.algashop.ordering.IntegrationTest;
import com.algaworks.algashop.ordering.domain.entity.CustomerTestDataBuilder;
import com.algaworks.algashop.ordering.domain.model.valueobject.Email;
import com.algaworks.algashop.ordering.domain.model.valueobject.FullName;
import com.algaworks.algashop.ordering.infrastructure.persistence.assembler.CustomerPersistenceEntityAssembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.config.SpringDataAuditingConfig;
import com.algaworks.algashop.ordering.infrastructure.persistence.disassembler.CustomerPersistenceEntityDisassembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.repository.CustomerPersistenceEntityRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@IntegrationTest
@Import({CustomerPersistenceProvider.class, CustomerPersistenceEntityDisassembler.class,
    CustomerPersistenceEntityAssembler.class, SpringDataAuditingConfig.class})
class CustomerPersistenceProviderIntegrationTest {

  private final CustomerPersistenceProvider persistenceProvider;
  private final CustomerPersistenceEntityRepository repository;

  @Autowired
  CustomerPersistenceProviderIntegrationTest(CustomerPersistenceProvider persistenceProvider,
      CustomerPersistenceEntityRepository repository) {
    this.persistenceProvider = persistenceProvider;
    this.repository = repository;
  }

  @Test
  void shouldInsertNewCustomer() {
    var customer = CustomerTestDataBuilder.brandNewCustomer().build();

    persistenceProvider.add(customer);

    var customerPersistenceEntity = repository.findById(customer.id().value()).orElseThrow();
    assertThat(customerPersistenceEntity).isNotNull();
    assertThat(customerPersistenceEntity.getFirstName()).isEqualTo(customer.fullName().firstName());
    assertThat(customerPersistenceEntity.getLastName()).isEqualTo(customer.fullName().lastName());
    assertThat(customerPersistenceEntity.getEmail()).isEqualTo(customer.email());
    assertThat(customerPersistenceEntity.getCreatedByUserId()).isNotNull();
    assertThat(customerPersistenceEntity.getLastModifiedAt()).isNotNull();
    assertThat(customerPersistenceEntity.getLastModifiedByUserId()).isNotNull();
  }

  @Test
  void shouldUpdateAndKeepPersistenceEntityState() {
    var customer = CustomerTestDataBuilder.brandNewCustomer().build();
    persistenceProvider.add(customer);

    var updatedCustomer = persistenceProvider.findById(customer.id()).orElseThrow();
    updatedCustomer.changeName(new FullName("New", "Name"));
    persistenceProvider.add(updatedCustomer);

    var customerPersistenceEntity = repository.findById(customer.id().value()).orElseThrow();
    assertThat(customerPersistenceEntity).isNotNull();
    assertThat(customerPersistenceEntity.getFirstName()).isEqualTo("New");
    assertThat(customerPersistenceEntity.getLastName()).isEqualTo("Name");
    assertThat(customerPersistenceEntity.getCreatedByUserId()).isNotNull();
    assertThat(customerPersistenceEntity.getLastModifiedAt()).isNotNull();
    assertThat(customerPersistenceEntity.getLastModifiedByUserId()).isNotNull();
  }

  @Test
  void shouldReturnTrueWhenCustomerExists() {
    var customer = CustomerTestDataBuilder.brandNewCustomer().build();
    persistenceProvider.add(customer);

    var customerExists = persistenceProvider.existsById(customer.id());

    assertThat(customerExists).isTrue();
  }

  @Test
  void shouldReturnFalseWhenCustomerDoesNotExist() {
    var customer = CustomerTestDataBuilder.brandNewCustomer().build();

    var customerExists = persistenceProvider.existsById(customer.id());

    assertThat(customerExists).isFalse();
  }

  @Test
  void shouldReturnQuantityOneWhenCustomerExists() {
    var customer = CustomerTestDataBuilder.brandNewCustomer().build();
    persistenceProvider.add(customer);

    var quantity = persistenceProvider.count();

    assertThat(quantity).isEqualTo(1);
  }

  @Test
  void shouldReturnQuantityZeroWhenNoCustomerExists() {
    var quantity = persistenceProvider.count();

    assertThat(quantity).isEqualTo(0);
  }

  @Test
  void shouldAddCustomerAndNotThrowException() {
    var customer = CustomerTestDataBuilder.brandNewCustomer().build();

    assertThatNoException().isThrownBy(() -> persistenceProvider.add(customer));
    assertThatNoException().isThrownBy(() -> persistenceProvider.findById(customer.id()));
  }

  @Test
  void shouldReturnCustomerWhenFoundByEmail() {
    var customer = CustomerTestDataBuilder.brandNewCustomer().build();
    persistenceProvider.add(customer);

    var foundCustomer = persistenceProvider.ofEmail(new Email(customer.email()));

    assertThat(foundCustomer).isPresent();
    assertThat(foundCustomer.get().id()).isEqualTo(customer.id());
    assertThat(foundCustomer.get().email()).isEqualTo(customer.email());
    assertThat(foundCustomer.get().fullName()).isEqualTo(customer.fullName());
  }

  @Test
  void shouldReturnEmptyWhenCustomerNotFoundByEmail() {
    var foundCustomer = persistenceProvider.ofEmail(new Email("nonexistent@example.com"));

    assertThat(foundCustomer).isEmpty();
  }

  @Test
  void shouldReturnCorrectCustomerWhenMultipleCustomersExist() {
    var customer1 = CustomerTestDataBuilder.brandNewCustomer().build();
    var customer2 = CustomerTestDataBuilder.brandNewCustomer()
        .email("another@example.com")
        .build();
    persistenceProvider.add(customer1);
    persistenceProvider.add(customer2);

    var foundCustomer = persistenceProvider.ofEmail(new Email(customer1.email()));

    assertThat(foundCustomer).isPresent();
    assertThat(foundCustomer.get().id()).isEqualTo(customer1.id());
  }
}
