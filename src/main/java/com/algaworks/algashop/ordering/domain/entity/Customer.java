package com.algaworks.algashop.ordering.domain.entity;

import static com.algaworks.algashop.ordering.domain.messages.ErrorMessages.BIRTHDATE_MUST_IN_PAST;
import static com.algaworks.algashop.ordering.domain.messages.ErrorMessages.DOCUMENT_CANNOT_BE_BLANK;
import static com.algaworks.algashop.ordering.domain.messages.ErrorMessages.PHONE_CANNOT_BE_BLANK;

import com.algaworks.algashop.ordering.domain.exception.CustomerArchivedException;
import com.algaworks.algashop.ordering.domain.validator.EmailFormatValidator;
import com.algaworks.algashop.ordering.domain.valueobject.Address;
import com.algaworks.algashop.ordering.domain.valueobject.CustomerId;
import com.algaworks.algashop.ordering.domain.valueobject.FullName;
import com.algaworks.algashop.ordering.domain.valueobject.LoyaltyPoints;
import com.algaworks.algashop.ordering.domain.valueobject.ZipCode;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

public class Customer {

  private CustomerId id;
  private FullName fullName;
  private LocalDate birthDate;
  private String email;
  private String phone;
  private String document;
  private Boolean promotionNotificationsAllowed;
  private Boolean archived;
  private OffsetDateTime registeredAt;
  private OffsetDateTime archivedAt;
  private LoyaltyPoints loyaltyPoints;
  private Address address;

  public static Customer brandnew(FullName fullName, LocalDate birthDate, String email, String phone, String document,
      Boolean promotionNotificationsAllowed, Address address) {
    var customerId = new CustomerId(UUID.randomUUID());
    var registeredAt = OffsetDateTime.now();
    return new Customer(customerId, fullName, birthDate, email, phone, document, promotionNotificationsAllowed, false,
        null, registeredAt, LoyaltyPoints.ZERO, address);
  }

  public static Customer existed(CustomerId customerId, FullName fullName, LocalDate birthDate, String email,
      String phone, String document, Boolean promotionNotificationsAllowed, Boolean archived, OffsetDateTime archivedAt,
      OffsetDateTime registeredAt, LoyaltyPoints loyaltyPoints, Address address) {
    return new Customer(customerId, fullName, birthDate, email, phone, document, promotionNotificationsAllowed,
        archived, registeredAt, archivedAt, loyaltyPoints, address);
  }

  private Customer(CustomerId customerId, FullName fullName, LocalDate birthDate, String email, String phone,
      String document, Boolean promotionNotificationsAllowed, Boolean archived, OffsetDateTime archivedAt,
      OffsetDateTime registeredAt, LoyaltyPoints loyaltyPoints, Address address) {
    this.setId(customerId);
    this.setFullName(fullName);
    this.setBirthDate(birthDate);
    this.setEmail(email);
    this.setPhone(phone);
    this.setDocument(document);
    this.setPromotionNotificationsAllowed(promotionNotificationsAllowed);
    this.setRegisteredAt(registeredAt);
    this.setArchived(archived);
    this.setArchivedAt(archivedAt);
    this.setLoyaltyPoints(loyaltyPoints);
    this.setAddress(address);
  }

  public void addLoyaltyPoints(Integer points) {
    this.customerCanBeModified();
    this.setLoyaltyPoints(this.loyaltyPoints().add(points));
  }

  public void archive() {
    customerCanBeModified();
    this.setArchived(true);
    this.setArchivedAt(OffsetDateTime.now());
    this.setFullName(new FullName("Anounymous", "Anounymous"));
    this.setEmail(UUID.randomUUID().toString() + "@anonymous.com");
    this.setPhone("0000-0000");
    this.setDocument("00000000000");
    this.setBirthDate(null);
    this.setPromotionNotificationsAllowed(false);
    var address = this.address().toBuilder();
    address.number("Anounymous");
    address.complement("Anounymous");
    this.setAddress(address.build());
  }

  private void customerCanBeModified() {
    if (this.archived()) {
      throw new CustomerArchivedException();
    }
  }

  public void enablePromotionNotifications() {
    customerCanBeModified();
    this.setPromotionNotificationsAllowed(true);
  }

  public void disablePromotionNotifications() {
    customerCanBeModified();
    this.setPromotionNotificationsAllowed(false);
  }

  public void changeName(FullName fullName) {
    customerCanBeModified();
    this.setFullName(fullName);
  }

  public void changeEmail(String email) {
    customerCanBeModified();
    this.setEmail(email);
  }

  public void changePhone(String phone) {
    customerCanBeModified();
    this.setPhone(phone);
  }

  public void changeAddress(Address address) {
    customerCanBeModified();
    this.setAddress(address);
  }

  public CustomerId id() {
    return id;
  }

  public FullName fullName() {
    return fullName;
  }

  public LocalDate birthDate() {
    return birthDate;
  }

  public String email() {
    return email;
  }

  public String phone() {
    return phone;
  }

  public String document() {
    return document;
  }

  public Boolean promotionNotificationsAllowed() {
    return promotionNotificationsAllowed;
  }

  public Boolean archived() {
    return archived;
  }

  public OffsetDateTime registeredAt() {
    return registeredAt;
  }

  public OffsetDateTime archivedAt() {
    return archivedAt;
  }

  public LoyaltyPoints loyaltyPoints() {
    return loyaltyPoints;
  }

  public Address address() {
    return address;
  }

  private void setId(CustomerId customerId) {
    this.id = customerId;
  }

  private void setFullName(FullName fullName) {
    this.fullName = Objects.requireNonNull(fullName);
  }

  private void setBirthDate(LocalDate birthDate) {
    if (birthDate == null) {
      this.birthDate = null;
      return;
    }
    if (birthDate.isAfter(LocalDate.now())) {
      throw new IllegalArgumentException(BIRTHDATE_MUST_IN_PAST);
    }
    this.birthDate = birthDate;
  }

  private void setEmail(String email) {
    Objects.requireNonNull(email);
    EmailFormatValidator.validate(email);
    this.email = email;
  }

  private void setDocument(String document) {
    Objects.requireNonNull(document);
    if (document.isBlank()) {
      throw new IllegalArgumentException(DOCUMENT_CANNOT_BE_BLANK);
    }
    this.document = document;
  }

  private void setPhone(String phone) {
    Objects.requireNonNull(phone);
    if (phone.isBlank()) {
      throw new IllegalArgumentException(PHONE_CANNOT_BE_BLANK);
    }
    this.phone = phone;
  }

  private void setArchived(Boolean archived) {
    this.archived = archived;
  }

  private void setPromotionNotificationsAllowed(Boolean promotionNotificationsAllowed) {
    this.promotionNotificationsAllowed = promotionNotificationsAllowed;
  }

  private void setLoyaltyPoints(LoyaltyPoints loyaltyPoints) {
    this.loyaltyPoints = loyaltyPoints;
  }

  private void setArchivedAt(OffsetDateTime archivedAt) {
    this.archivedAt = archivedAt;
  }

  private void setRegisteredAt(OffsetDateTime registeredAt) {
    this.registeredAt = registeredAt;
  }

  private void setAddress(Address address) {
    Objects.requireNonNull(address);
    this.address = address;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Customer customer = (Customer) o;
    return Objects.equals(id, customer.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
