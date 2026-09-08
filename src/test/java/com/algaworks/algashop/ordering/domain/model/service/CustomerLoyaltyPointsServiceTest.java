package com.algaworks.algashop.ordering.domain.model.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.algaworks.algashop.ordering.domain.entity.CustomerTestDataBuilder;
import com.algaworks.algashop.ordering.domain.entity.OrderTestDataBuilder;
import com.algaworks.algashop.ordering.domain.model.entity.Customer;
import com.algaworks.algashop.ordering.domain.model.entity.Order;
import com.algaworks.algashop.ordering.domain.model.entity.OrderStatus;
import com.algaworks.algashop.ordering.domain.model.valueobject.LoyaltyPoints;
import com.algaworks.algashop.ordering.domain.model.valueobject.Money;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CustomerLoyaltyPointsServiceTest {

  private final CustomerLoyaltyPointsService customerLoyaltyPointsService = new CustomerLoyaltyPointsService();

  @Test
  @DisplayName("Should accumulate loyalty points for a ready order above the minimum amount")
  void givenValidCustomerAndOrder_WhenAddingPoints_ShouldAccumulate() {
    var customerId = new CustomerId(UUID.randomUUID());
    Customer customer = CustomerTestDataBuilder.existedCustomer().customerId(customerId).build();
    Order order = OrderTestDataBuilder.anOrder()
        .customerId(customerId)
        .status(OrderStatus.READY)
        .totalAmount(new Money("6000"))
        .build();

    customerLoyaltyPointsService.addPoints(customer, order);

    assertThat(customer.loyaltyPoints()).isEqualTo(new LoyaltyPoints(30));
  }

  @Test
  @DisplayName("Should not accumulate loyalty points for a ready order below the minimum amount")
  void givenValidCustomerAndOrderWithLowValue_WhenAddingPoints_ShouldNotAccumulate() {
    var customerId = new CustomerId(UUID.randomUUID());
    Customer customer = CustomerTestDataBuilder.existedCustomer().customerId(customerId).build();
    Order order = OrderTestDataBuilder.anOrder()
        .customerId(customerId)
        .status(OrderStatus.READY)
        .totalAmount(new Money("500"))
        .build();

    customerLoyaltyPointsService.addPoints(customer, order);

    assertThat(customer.loyaltyPoints()).isEqualTo(LoyaltyPoints.ZERO);
  }
}
