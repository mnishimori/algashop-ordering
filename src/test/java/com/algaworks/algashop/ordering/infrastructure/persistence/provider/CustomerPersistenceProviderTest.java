package com.algaworks.algashop.ordering.infrastructure.persistence.provider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.algaworks.algashop.ordering.domain.entity.CustomerTestDataBuilder;
import com.algaworks.algashop.ordering.domain.model.entity.Customer;
import com.algaworks.algashop.ordering.domain.model.valueobject.Address;
import com.algaworks.algashop.ordering.domain.model.valueobject.FullName;
import com.algaworks.algashop.ordering.domain.model.valueobject.ZipCode;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.infrastructure.persistence.assembler.CustomerPersistenceEntityAssembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.disassembler.CustomerPersistenceEntityDisassembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.CustomerPersistenceEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.repository.CustomerPersistenceEntityRepository;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CustomerPersistenceProviderTest {

  @Mock
  private CustomerPersistenceEntityRepository customerPersistenceEntityRepository;

  @Mock
  private CustomerPersistenceEntityDisassembler disassembler;

  @Mock
  private CustomerPersistenceEntityAssembler assembler;

  @Mock
  private EntityManager entityManager;

  @InjectMocks
  private CustomerPersistenceProvider provider;

  @Test
  @DisplayName("Should return Optional.empty when customer not found by id")
  void shouldReturnEmptyWhenCustomerNotFoundById() {
    CustomerId customerId = new CustomerId(UUID.randomUUID());
    when(customerPersistenceEntityRepository.findById(customerId.value())).thenReturn(Optional.empty());

    Optional<Customer> result = provider.findById(customerId);

    assertThat(result).isEmpty();
    verify(disassembler, never()).toDomainEntity(any());
  }

  @Test
  @DisplayName("Should return customer when found by id")
  void shouldReturnCustomerWhenFoundById() {
    CustomerId customerId = new CustomerId(UUID.randomUUID());
    CustomerPersistenceEntity entity = new CustomerPersistenceEntity();
    entity.setId(customerId.value());
    entity.setFirstName("John");
    entity.setLastName("Doe");
    entity.setEmail("john.doe@example.com");
    entity.setPhone("11999999999");
    entity.setDocument("12345678900");
    entity.setBirthDate(LocalDate.of(1990, 1, 1));
    entity.setPromotionNotificationsAllowed(true);
    entity.setArchived(false);

    Customer customer = Customer.brandnew()
        .fullName(new FullName("John", "Doe"))
        .birthDate(LocalDate.of(1990, 1, 1))
        .email("john.doe@example.com")
        .phone("11999999999")
        .document("12345678900")
        .promotionNotificationsAllowed(true)
        .address(Address.builder()
            .street("Street")
            .number("123")
            .complement("Apt")
            .neighborhood("Neighborhood")
            .city("City")
            .state("SP")
            .zipCode(new ZipCode("12345-678"))
            .build())
        .build();

    when(customerPersistenceEntityRepository.findById(customerId.value())).thenReturn(Optional.of(entity));
    when(disassembler.toDomainEntity(entity)).thenReturn(customer);

    Optional<Customer> result = provider.findById(customerId);

    assertThat(result).isPresent();
    assertThat(result.get()).isEqualTo(customer);
  }

  @Test
  @DisplayName("Should propagate exception when repository throws exception")
  void shouldPropagateExceptionWhenRepositoryThrowsException() {
    CustomerId customerId = new CustomerId(UUID.randomUUID());
    RuntimeException exception = new RuntimeException("Database connection failed");
    when(customerPersistenceEntityRepository.findById(customerId.value())).thenThrow(exception);

    assertThatThrownBy(() -> provider.findById(customerId))
        .isInstanceOf(RuntimeException.class)
        .hasMessage("Database connection failed");

    verify(disassembler, never()).toDomainEntity(any());
  }

  @Test
  @DisplayName("Should propagate exception when disassembler throws exception")
  void shouldPropagateExceptionWhenDisassemblerThrowsException() {
    CustomerId customerId = new CustomerId(UUID.randomUUID());
    CustomerPersistenceEntity entity = new CustomerPersistenceEntity();
    entity.setId(customerId.value());

    RuntimeException exception = new RuntimeException("Disassembler failed");
    when(customerPersistenceEntityRepository.findById(customerId.value())).thenReturn(Optional.of(entity));
    when(disassembler.toDomainEntity(entity)).thenThrow(exception);

    assertThatThrownBy(() -> provider.findById(customerId))
        .isInstanceOf(RuntimeException.class)
        .hasMessage("Disassembler failed");
  }

  @Test
  @DisplayName("Should return true when customer exists by id")
  void shouldReturnTrueWhenCustomerExistsById() {
    CustomerId customerId = new CustomerId(UUID.randomUUID());
    when(customerPersistenceEntityRepository.existsById(customerId.value())).thenReturn(true);

    boolean result = provider.existsById(customerId);

    assertThat(result).isTrue();
  }

  @Test
  @DisplayName("Should return false when customer does not exist by id")
  void shouldReturnFalseWhenCustomerDoesNotExistById() {
    CustomerId customerId = new CustomerId(UUID.randomUUID());
    when(customerPersistenceEntityRepository.existsById(customerId.value())).thenReturn(false);

    boolean result = provider.existsById(customerId);

    assertThat(result).isFalse();
  }

  @Test
  @DisplayName("Should propagate exception when repository throws exception on existsById")
  void shouldPropagateExceptionWhenRepositoryThrowsExceptionOnExistsById() {
    CustomerId customerId = new CustomerId(UUID.randomUUID());
    RuntimeException exception = new RuntimeException("Database connection failed");
    when(customerPersistenceEntityRepository.existsById(customerId.value())).thenThrow(exception);

    assertThatThrownBy(() -> provider.existsById(customerId))
        .isInstanceOf(RuntimeException.class)
        .hasMessage("Database connection failed");
  }

  @Test
  @DisplayName("Should return customer count from repository")
  void shouldReturnCustomerCountFromRepository() {
    long expectedCount = 42L;
    when(customerPersistenceEntityRepository.count()).thenReturn(expectedCount);

    int result = provider.count();

    assertThat(result).isEqualTo((int) expectedCount);
  }

  @Test
  @DisplayName("Should return zero when repository returns zero count")
  void shouldReturnZeroWhenRepositoryReturnsZeroCount() {
    when(customerPersistenceEntityRepository.count()).thenReturn(0L);

    int result = provider.count();

    assertThat(result).isZero();
  }

  @Test
  @DisplayName("Should propagate exception when repository throws exception on count")
  void shouldPropagateExceptionWhenRepositoryThrowsExceptionOnCount() {
    RuntimeException exception = new RuntimeException("Database connection failed");
    when(customerPersistenceEntityRepository.count()).thenThrow(exception);

    assertThatThrownBy(() -> provider.count())
        .isInstanceOf(RuntimeException.class)
        .hasMessage("Database connection failed");
  }

  @Test
  @DisplayName("Should insert new customer when it does not exist in repository")
  void shouldInsertNewCustomerWhenItDoesNotExistInRepository() {
    Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
    CustomerPersistenceEntity entity = new CustomerPersistenceEntity();
    entity.setId(customer.id().value());

    when(customerPersistenceEntityRepository.findById(customer.id().value())).thenReturn(Optional.empty());
    when(assembler.fromDomain(customer)).thenReturn(entity);

    provider.add(customer);

    verify(customerPersistenceEntityRepository).findById(customer.id().value());
    verify(assembler).fromDomain(customer);
    verify(customerPersistenceEntityRepository).saveAndFlush(entity);
    verify(assembler, never()).merge(any(), any());
    verify(entityManager, never()).detach(any());
  }

  @Test
  @DisplayName("Should update existing customer when it already exists in repository")
  void shouldUpdateExistingCustomerWhenItAlreadyExistsInRepository() {
    Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
    CustomerPersistenceEntity existingEntity = new CustomerPersistenceEntity();
    existingEntity.setId(customer.id().value());
    CustomerPersistenceEntity mergedEntity = new CustomerPersistenceEntity();
    mergedEntity.setId(customer.id().value());
    CustomerPersistenceEntity savedEntity = new CustomerPersistenceEntity();
    savedEntity.setId(customer.id().value());
    savedEntity.setVersion(5L);

    when(customerPersistenceEntityRepository.findById(customer.id().value())).thenReturn(Optional.of(existingEntity));
    when(assembler.merge(existingEntity, customer)).thenReturn(mergedEntity);
    when(customerPersistenceEntityRepository.saveAndFlush(mergedEntity)).thenReturn(savedEntity);

    provider.add(customer);

    verify(customerPersistenceEntityRepository).findById(customer.id().value());
    verify(assembler).merge(existingEntity, customer);
    verify(entityManager).detach(existingEntity);
    verify(customerPersistenceEntityRepository).saveAndFlush(mergedEntity);
    verify(assembler, never()).fromDomain(any());
  }

  @Test
  @DisplayName("Should propagate exception when repository throws exception on add")
  void shouldPropagateExceptionWhenRepositoryThrowsExceptionOnAdd() {
    Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
    RuntimeException exception = new RuntimeException("Database connection failed");
    when(customerPersistenceEntityRepository.findById(customer.id().value())).thenThrow(exception);

    assertThatThrownBy(() -> provider.add(customer))
        .isInstanceOf(RuntimeException.class)
        .hasMessage("Database connection failed");

    verify(assembler, never()).fromDomain(any());
    verify(assembler, never()).merge(any(), any());
  }

  @Test
  @DisplayName("Should propagate exception when assembler throws exception on insert")
  void shouldPropagateExceptionWhenAssemblerThrowsExceptionOnInsert() {
    Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
    RuntimeException exception = new RuntimeException("Assembler failed");
    when(customerPersistenceEntityRepository.findById(customer.id().value())).thenReturn(Optional.empty());
    when(assembler.fromDomain(customer)).thenThrow(exception);

    assertThatThrownBy(() -> provider.add(customer))
        .isInstanceOf(RuntimeException.class)
        .hasMessage("Assembler failed");

    verify(customerPersistenceEntityRepository, never()).saveAndFlush(any());
  }

  @Test
  @DisplayName("Should propagate exception when repository throws exception on saveAndFlush during update")
  void shouldPropagateExceptionWhenRepositoryThrowsExceptionOnSaveAndFlushDuringUpdate() {
    Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
    CustomerPersistenceEntity existingEntity = new CustomerPersistenceEntity();
    existingEntity.setId(customer.id().value());
    CustomerPersistenceEntity mergedEntity = new CustomerPersistenceEntity();
    mergedEntity.setId(customer.id().value());
    RuntimeException exception = new RuntimeException("Save failed");

    when(customerPersistenceEntityRepository.findById(customer.id().value())).thenReturn(Optional.of(existingEntity));
    when(assembler.merge(existingEntity, customer)).thenReturn(mergedEntity);
    when(customerPersistenceEntityRepository.saveAndFlush(mergedEntity)).thenThrow(exception);

    assertThatThrownBy(() -> provider.add(customer))
        .isInstanceOf(RuntimeException.class)
        .hasMessage("Save failed");

    verify(entityManager).detach(existingEntity);
  }
}
