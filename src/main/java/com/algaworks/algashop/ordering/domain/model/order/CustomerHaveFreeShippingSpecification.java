package com.algaworks.algashop.ordering.domain.model.order;

import com.algaworks.algashop.ordering.domain.model.Specification;
import com.algaworks.algashop.ordering.domain.model.customer.Customer;
import com.algaworks.algashop.ordering.domain.model.customer.LoyaltyPoints;

public class CustomerHaveFreeShippingSpecification implements Specification<Customer> {

  private final CustomerHasOrderedEnouthAtYearSpecification hasOrderedEnoughAtYear;
  private final CustomerHasEnoughLoyaltyPointsSpecification hasEnoughBasicLoyaltyPoints;
  private final CustomerHasEnoughLoyaltyPointsSpecification hasEnoughPremiumLoyaltyPoints;

  public CustomerHaveFreeShippingSpecification(Orders orders, LoyaltyPoints basicLoyaltyPoints,
      long salesQuantityForFreeShipping, LoyaltyPoints premiumLoyaltyPoints) {
    this.hasEnoughBasicLoyaltyPoints = new CustomerHasEnoughLoyaltyPointsSpecification(basicLoyaltyPoints);
    this.hasEnoughPremiumLoyaltyPoints = new CustomerHasEnoughLoyaltyPointsSpecification(premiumLoyaltyPoints);
    this.hasOrderedEnoughAtYear = new CustomerHasOrderedEnouthAtYearSpecification(orders, salesQuantityForFreeShipping);
  }

  @Override
  public boolean isSatisfiedBy(Customer customer) {
    return hasEnoughBasicLoyaltyPoints.and(hasOrderedEnoughAtYear).or(hasEnoughPremiumLoyaltyPoints)
        .isSatisfiedBy(customer);
  }
}
