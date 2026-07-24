package com.algaworks.algashop.ordering.infrastructure.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "order_items")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(of = "id")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OrderItemPersistenceEntity {

  @Id
  @EqualsAndHashCode.Include
  private Long id;
  private UUID productId;
  private String productName;
  private BigDecimal price;
  private Integer quantity;
  private BigDecimal totalAmount;
  @JoinColumn(name = "order_id", nullable = false)
  @ManyToOne(optional = false)
  private OrderPersistenceEntity order;

  public Long getOrderId() {
    if (this.getOrder() == null) {
      return null;
    }
    return this.getOrder().getId();
  }
}
