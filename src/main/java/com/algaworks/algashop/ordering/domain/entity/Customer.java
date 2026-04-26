package com.algaworks.algashop.ordering.domain.entity;

import static com.algaworks.algashop.ordering.domain.messages.ErrorMessages.DOCUMENT_CANNOT_BE_BLANK;
import static com.algaworks.algashop.ordering.domain.messages.ErrorMessages.PHONE_CANNOT_BE_BLANK;
import static com.algaworks.algashop.ordering.domain.messages.ErrorMessages.BIRTHDATE_MUST_IN_PAST;
import static com.algaworks.algashop.ordering.domain.messages.ErrorMessages.FULLNAME_IS_BLANK;

import com.algaworks.algashop.ordering.domain.exception.CustomerArchivedException;
import com.algaworks.algashop.ordering.domain.validator.EmailFormatValidator;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

public class Customer {

  private UUID id;
  private String fullName;
  private LocalDate birthDate;
  private String email;
  private String phone;
  private String document;
  private Boolean promotionNotificationsAllowed;
  private Boolean archived;
  private OffsetDateTime registeredAt;
  private OffsetDateTime archivedAt;
  private Integer loyaltyPoints;

  public Customer(UUID id, String fullName, LocalDate birthDate, String email, String phone, String document,
      Boolean promotionNotificationsAllowed, Boolean archived, OffsetDateTime registeredAt, OffsetDateTime archivedAt,
      Integer loyaltyPoints) {
    this.setId(id);
    this.setFullName(fullName);
    this.setBirthDate(birthDate);
    this.setEmail(email);
    this.setPhone(phone);
    this.setDocument(document);
    this.setPromotionNotificationsAllowed(promotionNotificationsAllowed);
    this.setRegisteredAt(registeredAt);
    this.setArchived(false);
    this.setArchivedAt(archivedAt);
    this.setLoyaltyPoints(0);
  }

  public Customer(UUID id, String fullName, LocalDate birthDate, String email, String phone, String document,
      Boolean promotionNotificationsAllowed, OffsetDateTime registeredAt) {
    this.setId(id);
    this.setFullName(fullName);
    this.setBirthDate(birthDate);
    this.setEmail(email);
    this.setPhone(phone);
    this.setDocument(document);
    this.setPromotionNotificationsAllowed(promotionNotificationsAllowed);
    this.setRegisteredAt(registeredAt);
    this.setArchived(false);
  }

  public void addLoyaltyPoints(Integer points) {
    this.loyaltyPoints = points;
  }

  public void archive() {
    customerCanBeModified();
    this.setArchived(true);
    this.setArchivedAt(OffsetDateTime.now());
    this.setFullName(this.fullName() + " (Archived)");
    this.setPromotionNotificationsAllowed(false);
    this.setLoyaltyPoints(0);
    this.setPhone("0000-0000");
    this.setDocument("00000000000");
    this.setBirthDate(null);
    this.setRegisteredAt(null);
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

  public void changeName(String fullName) {
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

  public UUID id() {
    return id;
  }

  public String fullName() {
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

  public Integer loyaltyPoints() {
    return loyaltyPoints;
  }

  private void setId(UUID id) {
    this.id = id;
  }

  private void setFullName(String fullName) {
    Objects.requireNonNull(fullName);
    if (fullName.isBlank()) {
      throw new IllegalArgumentException(FULLNAME_IS_BLANK);
    }
    this.fullName = fullName;
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

  private void setLoyaltyPoints(Integer loyaltyPoints) {
    this.loyaltyPoints = loyaltyPoints;
  }

  private void setArchivedAt(OffsetDateTime archivedAt) {
    this.archivedAt = archivedAt;
  }

  private void setRegisteredAt(OffsetDateTime registeredAt) {
    this.registeredAt = registeredAt;
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
