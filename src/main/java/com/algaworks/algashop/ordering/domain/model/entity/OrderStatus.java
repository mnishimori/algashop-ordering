package com.algaworks.algashop.ordering.domain.model.entity;

import java.util.List;

public enum OrderStatus {
  DRAFT,
  PLACED(DRAFT),
  PAID(PLACED),
  READY(PAID),
  CANCELED(PAID, READY, PLACED, DRAFT);

  OrderStatus(OrderStatus... previousStatuses) {
    this.previousOrderStatuses = List.of(previousStatuses);
  }

  private final List<OrderStatus> previousOrderStatuses;

  public boolean canChangeTo(OrderStatus nextStatus) {
    var currentStatus = this;
    return nextStatus.previousOrderStatuses.contains(currentStatus);
  }

  public boolean canNotChangeTo(OrderStatus nextStatus) {
    return !canChangeTo(nextStatus);
  }
}
