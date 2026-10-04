package com.algaworks.algashop.ordering.domain.model.customer;

import com.algaworks.algashop.ordering.domain.model.order.Order;
import com.algaworks.algashop.ordering.domain.model.order.OrderNotBelongsToCustomerException;
import com.algaworks.algashop.ordering.domain.model.DomainService;
import com.algaworks.algashop.ordering.domain.model.commons.Money;
import java.util.Objects;

@DomainService
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
