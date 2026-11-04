package com.algaworks.algashop.ordering.domain.model.order;

import com.algaworks.algashop.ordering.domain.model.Specification;
import com.algaworks.algashop.ordering.domain.model.customer.Customer;
import com.algaworks.algashop.ordering.domain.model.customer.LoyaltyPoints;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomerHasEnoughLoyaltyPointsSpecification implements Specification<Customer> {

  private final LoyaltyPoints expectedloyaltyPoints;

  @Override
  public boolean isSatisfiedBy(Customer customer) {
    return customer.loyaltyPoints().compareTo(expectedloyaltyPoints) >= 0;
  }
}
