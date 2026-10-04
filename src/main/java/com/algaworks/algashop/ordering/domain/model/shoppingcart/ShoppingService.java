package com.algaworks.algashop.ordering.domain.model.shoppingcart;

import com.algaworks.algashop.ordering.domain.model.customer.CustomerAlreadyHaveShoppingCartException;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerNotFoundException;
import com.algaworks.algashop.ordering.domain.model.customer.Customers;
import com.algaworks.algashop.ordering.domain.model.DomainService;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerId;
import java.util.Objects;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@DomainService
public class ShoppingService {

  private final Customers customers;
  private final ShoppingCarts shoppingCarts;

  public ShoppingCart startShopping(CustomerId customerId) {
    Objects.requireNonNull(customerId);
    if (!customers.existsById(customerId)) {
      throw new CustomerNotFoundException(customerId);
    }
    if (shoppingCarts.ofCustomer(customerId).isPresent()) {
      throw new CustomerAlreadyHaveShoppingCartException(customerId);
    }
    return ShoppingCart.newShoppingCart(customerId);
  }

}
