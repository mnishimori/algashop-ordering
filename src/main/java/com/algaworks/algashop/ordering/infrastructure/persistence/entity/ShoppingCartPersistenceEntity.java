package com.algaworks.algashop.ordering.infrastructure.persistence.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "shopping_carts")
@NoArgsConstructor
@Getter
@Setter
@ToString(of = "id")
@EntityListeners(AuditingEntityListener.class)
public class ShoppingCartPersistenceEntity {

  @Id
  private UUID id;
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
  @JoinColumn(name = "customer_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
  private CustomerPersistenceEntity customer;
  @OneToMany(mappedBy = "shoppingCart", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<ShoppingCartItemPersistenceEntity> items;
  private BigDecimal totalAmount;
  private Integer totalItems;
  @CreatedBy
  private UUID createdByUserId;
  @CreatedDate
  private OffsetDateTime createdAt;
  @LastModifiedDate
  private OffsetDateTime lastModifiedAt;
  @LastModifiedBy
  private UUID lastModifiedByUserId;
  @Version
  private Long version;


}
