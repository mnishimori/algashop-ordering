package com.algaworks.algashop.ordering.domain.model.service;

import com.algaworks.algashop.ordering.domain.model.entity.Customer;
import com.algaworks.algashop.ordering.domain.model.entity.Order;
import com.algaworks.algashop.ordering.domain.model.exception.CantAddLoyantyPointsOrderIsNotReady;
import com.algaworks.algashop.ordering.domain.model.exception.OrderNotBelongsToCustomerException;
import com.algaworks.algashop.ordering.domain.model.valueobject.LoyaltyPoints;
import com.algaworks.algashop.ordering.domain.model.valueobject.Money;
import java.util.Objects;

public class CustomerLoyaltyPointsService {

  private static final LoyaltyPoints baseLoyaltyPoints = new LoyaltyPoints(5);
  private static final Money expectedAmountToGivePoints = new Money("1000");

  public void addPoints(Customer customer, Order order) {
    Objects.requireNonNull(customer);
    Objects.requireNonNull(order);
    if (!customer.id().equals(order.customerId())) {
      throw new OrderNotBelongsToCustomerException();
    }
    if (!order.isReady()) {
      throw new CantAddLoyantyPointsOrderIsNotReady();
    }
    var loyaltyPoints = this.calculateLoyaltyPoints(order);
    if (loyaltyPoints.value() > 0) {
      customer.addLoyaltyPoints(loyaltyPoints.value());
    }
  }

  private LoyaltyPoints calculateLoyaltyPoints(Order order) {
    if (!shouldGivePointsByAmount(order.totalAmount())){
      return LoyaltyPoints.ZERO;
    }
    var result = order.totalAmount().divide(expectedAmountToGivePoints);
    var loyaltyPointsValue = result.value().intValue() * baseLoyaltyPoints.value();
    return new LoyaltyPoints(loyaltyPointsValue);
  }

  private boolean shouldGivePointsByAmount(Money totalAmount) {
    return totalAmount.compareTo(expectedAmountToGivePoints) >= 0;
  }
}
