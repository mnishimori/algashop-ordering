package com.algaworks.algashop.ordering.infrastructure.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "orders")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(of = "id")
@EntityListeners(AuditingEntityListener.class)
public class OrderPersistenceEntity {

  @Id
  private Long id;
  private UUID customerId;
  private BigDecimal totalAmount;
  private Integer totalItems;
  private String status;
  private String paymentMethod;
  private OffsetDateTime placedAt;
  private OffsetDateTime paidAt;
  private OffsetDateTime canceledAt;
  private OffsetDateTime deliveredAt;
  private OffsetDateTime readyAt;

  @CreatedBy
  private UUID createdByUserId;
  @LastModifiedBy
  private UUID lastModifiedByUserId;
  @LastModifiedDate
  private OffsetDateTime lastModifiedAt;
}
