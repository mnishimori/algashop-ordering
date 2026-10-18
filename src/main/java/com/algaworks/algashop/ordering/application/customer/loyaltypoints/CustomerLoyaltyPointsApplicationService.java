package com.algaworks.algashop.ordering.application.customer.loyaltypoints;

import com.algaworks.algashop.ordering.domain.model.customer.CustomerId;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerLoyaltyPointsService;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerNotFoundException;
import com.algaworks.algashop.ordering.domain.model.customer.Customers;
import com.algaworks.algashop.ordering.domain.model.order.OrderId;
import com.algaworks.algashop.ordering.domain.model.order.OrderNotFoundException;
import com.algaworks.algashop.ordering.domain.model.order.Orders;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomerLoyaltyPointsApplicationService {

  private final Customers customers;
  private final Orders orders;
  private final CustomerLoyaltyPointsService customerLoyaltyPointsService;

  @Transactional
  public void addLoyaltyPoints(UUID rawCustomerId, String rawOrderid) {
    var customerId = new CustomerId(rawCustomerId);
    var customer = customers.findById(customerId).orElseThrow(() -> new CustomerNotFoundException(customerId));
    var orderId = new OrderId(rawOrderid);
    var order = orders.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
    customerLoyaltyPointsService.addPoints(customer, order);
    customers.add(customer);
  }

}
