package com.algaworks.algashop.ordering.infrastructure.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;

@Entity
@Table(name = "shopping_cart_items")
@NoArgsConstructor
@Getter
@Setter
@ToString(of = "id")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ShoppingCartItemPersistenceEntity {

  @Id
  @EqualsAndHashCode.Include
  private UUID id;
  @JoinColumn(name = "shopping_cart_id", nullable = false)
  @ManyToOne(optional = false)
  private ShoppingCartPersistenceEntity shoppingCart;
  private UUID productId;
  private String productName;
  private BigDecimal price;
  private Integer quantity;
  private BigDecimal totalAmount;
  private Boolean available;
  @CreatedBy
  private UUID createdByUserId;
  @CreatedDate
  private OffsetDateTime createdAt;
  @LastModifiedDate
  private OffsetDateTime lastModifiedAt;
  @LastModifiedBy
  private UUID lastModifiedByUserId;
  @Version
  private Long Version;
}
