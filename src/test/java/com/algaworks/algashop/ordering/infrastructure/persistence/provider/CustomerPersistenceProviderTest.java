package com.algaworks.algashop.ordering.infrastructure.persistence.provider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.algaworks.algashop.ordering.domain.model.entity.Customer;
import com.algaworks.algashop.ordering.domain.model.valueobject.Address;
import com.algaworks.algashop.ordering.domain.model.valueobject.FullName;
import com.algaworks.algashop.ordering.domain.model.valueobject.ZipCode;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.infrastructure.persistence.disassembler.CustomerPersistenceEntityDisassembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.CustomerPersistenceEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.repository.CustomerPersistenceEntityRepository;
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
}
