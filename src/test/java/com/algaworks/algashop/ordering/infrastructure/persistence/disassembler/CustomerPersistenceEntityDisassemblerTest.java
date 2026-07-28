package com.algaworks.algashop.ordering.infrastructure.persistence.disassembler;

import static org.assertj.core.api.Assertions.assertThat;

import com.algaworks.algashop.ordering.domain.model.entity.Customer;
import com.algaworks.algashop.ordering.domain.model.valueobject.Address;
import com.algaworks.algashop.ordering.domain.model.valueobject.FullName;
import com.algaworks.algashop.ordering.domain.model.valueobject.LoyaltyPoints;
import com.algaworks.algashop.ordering.domain.model.valueobject.ZipCode;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.infrastructure.persistence.embeddable.AddressEmbeddable;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.CustomerPersistenceEntity;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CustomerPersistenceEntityDisassemblerTest {

  private final CustomerPersistenceEntityDisassembler disassembler = new CustomerPersistenceEntityDisassembler();

  @Test
  @DisplayName("Should convert CustomerPersistenceEntity to Customer domain entity with all fields")
  void shouldConvertCustomerPersistenceEntityToCustomerWithAllFields() {
    UUID customerId = UUID.randomUUID();
    CustomerPersistenceEntity entity = new CustomerPersistenceEntity();
    entity.setId(customerId);
    entity.setFirstName("John");
    entity.setLastName("Doe");
    entity.setBirthDate(LocalDate.of(1990, 1, 1));
    entity.setEmail("john.doe@example.com");
    entity.setPhone("11999999999");
    entity.setDocument("12345678900");
    entity.setPromotionNotificationsAllowed(true);
    entity.setArchived(false);
    entity.setArchivedAt(OffsetDateTime.now());
    entity.setRegisteredAt(OffsetDateTime.now());
    entity.setLoyaltyPoints(100);

    AddressEmbeddable addressEmbeddable = AddressEmbeddable.builder()
        .street("Main Street")
        .number("123")
        .complement("Apt 4B")
        .neighborhood("Downtown")
        .city("São Paulo")
        .state("SP")
        .zipCode("01234-567")
        .build();
    entity.setAddressEmbeddable(addressEmbeddable);

    Customer result = disassembler.toDomainEntity(entity);

    assertThat(result).isNotNull();
    assertThat(result.id()).isEqualTo(new CustomerId(customerId));
    assertThat(result.fullName()).isEqualTo(new FullName("John", "Doe"));
    assertThat(result.birthDate()).isEqualTo(LocalDate.of(1990, 1, 1));
    assertThat(result.email()).isEqualTo("john.doe@example.com");
    assertThat(result.phone()).isEqualTo("11999999999");
    assertThat(result.document()).isEqualTo("12345678900");
    assertThat(result.promotionNotificationsAllowed()).isTrue();
    assertThat(result.archived()).isFalse();
    assertThat(result.archivedAt()).isNotNull();
    assertThat(result.registeredAt()).isNotNull();
    assertThat(result.loyaltyPoints()).isEqualTo(new LoyaltyPoints(100));

    Address address = result.address();
    assertThat(address).isNotNull();
    assertThat(address.street()).isEqualTo("Main Street");
    assertThat(address.number()).isEqualTo("123");
    assertThat(address.complement()).isEqualTo("Apt 4B");
    assertThat(address.neighborhood()).isEqualTo("Downtown");
    assertThat(address.city()).isEqualTo("São Paulo");
    assertThat(address.state()).isEqualTo("SP");
    assertThat(address.zipCode()).isEqualTo(new ZipCode("01234-567"));
  }

  @Test
  @DisplayName("Should convert CustomerPersistenceEntity to Customer with archived status")
  void shouldConvertCustomerPersistenceEntityToCustomerWithArchivedStatus() {
    UUID customerId = UUID.randomUUID();
    CustomerPersistenceEntity entity = new CustomerPersistenceEntity();
    entity.setId(customerId);
    entity.setFirstName("Jane");
    entity.setLastName("Smith");
    entity.setBirthDate(LocalDate.of(1985, 5, 15));
    entity.setEmail("jane.smith@example.com");
    entity.setPhone("21888888888");
    entity.setDocument("98765432100");
    entity.setPromotionNotificationsAllowed(false);
    entity.setArchived(true);
    entity.setArchivedAt(OffsetDateTime.now());
    entity.setRegisteredAt(OffsetDateTime.now());
    entity.setLoyaltyPoints(50);

    AddressEmbeddable addressEmbeddable = AddressEmbeddable.builder()
        .street("Maple Street")
        .number("789")
        .complement("Apt 1")
        .neighborhood("Uptown")
        .city("New York")
        .state("NY")
        .zipCode("10001-000")
        .build();
    entity.setAddressEmbeddable(addressEmbeddable);

    Customer result = disassembler.toDomainEntity(entity);

    assertThat(result).isNotNull();
    assertThat(result.id()).isEqualTo(new CustomerId(customerId));
    assertThat(result.fullName()).isEqualTo(new FullName("Jane", "Smith"));
    assertThat(result.birthDate()).isEqualTo(LocalDate.of(1985, 5, 15));
    assertThat(result.email()).isEqualTo("jane.smith@example.com");
    assertThat(result.phone()).isEqualTo("21888888888");
    assertThat(result.document()).isEqualTo("98765432100");
    assertThat(result.promotionNotificationsAllowed()).isFalse();
    assertThat(result.archived()).isTrue();
    assertThat(result.archivedAt()).isNotNull();
    assertThat(result.registeredAt()).isNotNull();
    assertThat(result.loyaltyPoints()).isEqualTo(new LoyaltyPoints(50));

    Address address = result.address();
    assertThat(address).isNotNull();
    assertThat(address.street()).isEqualTo("Maple Street");
    assertThat(address.number()).isEqualTo("789");
    assertThat(address.complement()).isEqualTo("Apt 1");
    assertThat(address.neighborhood()).isEqualTo("Uptown");
    assertThat(address.city()).isEqualTo("New York");
    assertThat(address.state()).isEqualTo("NY");
    assertThat(address.zipCode()).isEqualTo(new ZipCode("10001-000"));
  }

  @Test
  @DisplayName("Should convert CustomerPersistenceEntity to Customer with high loyalty points")
  void shouldConvertCustomerPersistenceEntityToCustomerWithHighLoyaltyPoints() {
    UUID customerId = UUID.randomUUID();
    CustomerPersistenceEntity entity = new CustomerPersistenceEntity();
    entity.setId(customerId);
    entity.setFirstName("Bob");
    entity.setLastName("Johnson");
    entity.setBirthDate(LocalDate.of(2000, 12, 25));
    entity.setEmail("bob.johnson@example.com");
    entity.setPhone("31777777777");
    entity.setDocument("11122233344");
    entity.setPromotionNotificationsAllowed(true);
    entity.setArchived(false);
    entity.setArchivedAt(OffsetDateTime.now());
    entity.setRegisteredAt(OffsetDateTime.now());
    entity.setLoyaltyPoints(5000);

    AddressEmbeddable addressEmbeddable = AddressEmbeddable.builder()
        .street("Oak Street")
        .number("456")
        .complement("Suite 100")
        .neighborhood("Copacabana")
        .city("Rio de Janeiro")
        .state("RJ")
        .zipCode("22070-001")
        .build();
    entity.setAddressEmbeddable(addressEmbeddable);

    Customer result = disassembler.toDomainEntity(entity);

    assertThat(result).isNotNull();
    assertThat(result.id()).isEqualTo(new CustomerId(customerId));
    assertThat(result.fullName()).isEqualTo(new FullName("Bob", "Johnson"));
    assertThat(result.birthDate()).isEqualTo(LocalDate.of(2000, 12, 25));
    assertThat(result.email()).isEqualTo("bob.johnson@example.com");
    assertThat(result.phone()).isEqualTo("31777777777");
    assertThat(result.document()).isEqualTo("11122233344");
    assertThat(result.promotionNotificationsAllowed()).isTrue();
    assertThat(result.archived()).isFalse();
    assertThat(result.archivedAt()).isNotNull();
    assertThat(result.registeredAt()).isNotNull();
    assertThat(result.loyaltyPoints()).isEqualTo(new LoyaltyPoints(5000));

    Address address = result.address();
    assertThat(address).isNotNull();
    assertThat(address.street()).isEqualTo("Oak Street");
    assertThat(address.number()).isEqualTo("456");
    assertThat(address.complement()).isEqualTo("Suite 100");
    assertThat(address.neighborhood()).isEqualTo("Copacabana");
    assertThat(address.city()).isEqualTo("Rio de Janeiro");
    assertThat(address.state()).isEqualTo("RJ");
    assertThat(address.zipCode()).isEqualTo(new ZipCode("22070-001"));
  }

  @Test
  @DisplayName("Should convert CustomerPersistenceEntity to Customer with zero loyalty points")
  void shouldConvertCustomerPersistenceEntityToCustomerWithZeroLoyaltyPoints() {
    UUID customerId = UUID.randomUUID();
    CustomerPersistenceEntity entity = new CustomerPersistenceEntity();
    entity.setId(customerId);
    entity.setFirstName("Alice");
    entity.setLastName("Williams");
    entity.setBirthDate(LocalDate.of(1995, 3, 10));
    entity.setEmail("alice.williams@example.com");
    entity.setPhone("41666666666");
    entity.setDocument("55566677788");
    entity.setPromotionNotificationsAllowed(true);
    entity.setArchived(false);
    entity.setArchivedAt(OffsetDateTime.now());
    entity.setRegisteredAt(OffsetDateTime.now());
    entity.setLoyaltyPoints(0);

    AddressEmbeddable addressEmbeddable = AddressEmbeddable.builder()
        .street("Pine Street")
        .number("101")
        .complement("Suite 500")
        .neighborhood("Financial District")
        .city("Chicago")
        .state("IL")
        .zipCode("60601-000")
        .build();
    entity.setAddressEmbeddable(addressEmbeddable);

    Customer result = disassembler.toDomainEntity(entity);

    assertThat(result).isNotNull();
    assertThat(result.id()).isEqualTo(new CustomerId(customerId));
    assertThat(result.fullName()).isEqualTo(new FullName("Alice", "Williams"));
    assertThat(result.birthDate()).isEqualTo(LocalDate.of(1995, 3, 10));
    assertThat(result.email()).isEqualTo("alice.williams@example.com");
    assertThat(result.phone()).isEqualTo("41666666666");
    assertThat(result.document()).isEqualTo("55566677788");
    assertThat(result.promotionNotificationsAllowed()).isTrue();
    assertThat(result.archived()).isFalse();
    assertThat(result.archivedAt()).isNotNull();
    assertThat(result.registeredAt()).isNotNull();
    assertThat(result.loyaltyPoints()).isEqualTo(new LoyaltyPoints(0));

    Address address = result.address();
    assertThat(address).isNotNull();
    assertThat(address.street()).isEqualTo("Pine Street");
    assertThat(address.number()).isEqualTo("101");
    assertThat(address.complement()).isEqualTo("Suite 500");
    assertThat(address.neighborhood()).isEqualTo("Financial District");
    assertThat(address.city()).isEqualTo("Chicago");
    assertThat(address.state()).isEqualTo("IL");
    assertThat(address.zipCode()).isEqualTo(new ZipCode("60601-000"));
  }

  @Test
  @DisplayName("Should convert CustomerPersistenceEntity to Customer with promotion notifications disabled")
  void shouldConvertCustomerPersistenceEntityToCustomerWithPromotionNotificationsDisabled() {
    UUID customerId = UUID.randomUUID();
    CustomerPersistenceEntity entity = new CustomerPersistenceEntity();
    entity.setId(customerId);
    entity.setFirstName("Charlie");
    entity.setLastName("Brown");
    entity.setBirthDate(LocalDate.of(1988, 7, 20));
    entity.setEmail("charlie.brown@example.com");
    entity.setPhone("51555555555");
    entity.setDocument("99988877766");
    entity.setPromotionNotificationsAllowed(false);
    entity.setArchived(false);
    entity.setArchivedAt(OffsetDateTime.now());
    entity.setRegisteredAt(OffsetDateTime.now());
    entity.setLoyaltyPoints(100);

    AddressEmbeddable addressEmbeddable = AddressEmbeddable.builder()
        .street("Cedar Lane")
        .number("202")
        .complement("House")
        .neighborhood("Suburb")
        .city("Austin")
        .state("TX")
        .zipCode("78701-000")
        .build();
    entity.setAddressEmbeddable(addressEmbeddable);

    Customer result = disassembler.toDomainEntity(entity);

    assertThat(result).isNotNull();
    assertThat(result.id()).isEqualTo(new CustomerId(customerId));
    assertThat(result.fullName()).isEqualTo(new FullName("Charlie", "Brown"));
    assertThat(result.birthDate()).isEqualTo(LocalDate.of(1988, 7, 20));
    assertThat(result.email()).isEqualTo("charlie.brown@example.com");
    assertThat(result.phone()).isEqualTo("51555555555");
    assertThat(result.document()).isEqualTo("99988877766");
    assertThat(result.promotionNotificationsAllowed()).isFalse();
    assertThat(result.archived()).isFalse();
    assertThat(result.archivedAt()).isNotNull();
    assertThat(result.registeredAt()).isNotNull();
    assertThat(result.loyaltyPoints()).isEqualTo(new LoyaltyPoints(100));

    Address address = result.address();
    assertThat(address).isNotNull();
    assertThat(address.street()).isEqualTo("Cedar Lane");
    assertThat(address.number()).isEqualTo("202");
    assertThat(address.complement()).isEqualTo("House");
    assertThat(address.neighborhood()).isEqualTo("Suburb");
    assertThat(address.city()).isEqualTo("Austin");
    assertThat(address.state()).isEqualTo("TX");
    assertThat(address.zipCode()).isEqualTo(new ZipCode("78701-000"));
  }



  @Test
  @DisplayName("Should build address with optional complement field")
  void shouldBuildAddressWithOptionalComplementField() {
    UUID customerId = UUID.randomUUID();
    CustomerPersistenceEntity entity = new CustomerPersistenceEntity();
    entity.setId(customerId);
    entity.setFirstName("Frank");
    entity.setLastName("Anderson");
    entity.setBirthDate(LocalDate.of(1987, 4, 8));
    entity.setEmail("frank.anderson@example.com");
    entity.setPhone("81222222222");
    entity.setDocument("11122233344");
    entity.setPromotionNotificationsAllowed(true);
    entity.setArchived(false);
    entity.setArchivedAt(OffsetDateTime.now());
    entity.setRegisteredAt(OffsetDateTime.now());
    entity.setLoyaltyPoints(150);

    AddressEmbeddable addressEmbeddable = AddressEmbeddable.builder()
        .street("Oak Avenue")
        .number("999")
        .complement(null)
        .neighborhood("Downtown")
        .city("Seattle")
        .state("WA")
        .zipCode("98101-000")
        .build();
    entity.setAddressEmbeddable(addressEmbeddable);

    Customer result = disassembler.toDomainEntity(entity);

    assertThat(result).isNotNull();
    Address address = result.address();
    assertThat(address).isNotNull();
    assertThat(address.street()).isEqualTo("Oak Avenue");
    assertThat(address.number()).isEqualTo("999");
    assertThat(address.complement()).isNull();
    assertThat(address.neighborhood()).isEqualTo("Downtown");
    assertThat(address.city()).isEqualTo("Seattle");
    assertThat(address.state()).isEqualTo("WA");
    assertThat(address.zipCode()).isEqualTo(new ZipCode("98101-000"));
  }
}
