package com.algaworks.algashop.ordering.infrastructure.persistence.entity;

import com.algaworks.algashop.ordering.domain.model.valueobject.Address;
import com.algaworks.algashop.ordering.domain.model.valueobject.LoyaltyPoints;
import com.algaworks.algashop.ordering.infrastructure.persistence.embeddable.AddressEmbeddable;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "customers")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(of = "id")
@EntityListeners(AuditingEntityListener.class)
public class CustomerPersistenceEntity {

  @Id
  private UUID id;
  private String firstName;
  private String lastName;
  private LocalDate birthDate;
  private String document;
  private String email;
  private String phone;
  private Boolean promotionNotificationsAllowed;
  private Boolean archived;
  @CreatedDate
  private OffsetDateTime registeredAt;
  private OffsetDateTime archivedAt;
  private Integer loyaltyPoints;
  @Embedded
  @AttributeOverrides({
      @AttributeOverride(name = "street", column = @Column(name = "address_street")),
      @AttributeOverride(name = "number", column = @Column(name = "address_number")),
      @AttributeOverride(name = "complement", column = @Column(name = "address_complement")),
      @AttributeOverride(name = "neighborhood", column = @Column(name = "address_neighborhood")),
      @AttributeOverride(name = "city", column = @Column(name = "address_city")),
      @AttributeOverride(name = "state", column = @Column(name = "address_state")),
      @AttributeOverride(name = "zipCode", column = @Column(name = "address_zipCode"))
  })
  private AddressEmbeddable addressEmbeddable;
  @CreatedBy
  private UUID createdByUserId;
  @LastModifiedBy
  private UUID lastModifiedByUserId;
  @LastModifiedDate
  private OffsetDateTime lastModifiedAt;
  @Version
  private Long version;

}
