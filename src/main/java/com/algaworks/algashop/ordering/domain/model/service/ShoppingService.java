package com.algaworks.algashop.ordering.domain.model.service;

import com.algaworks.algashop.ordering.domain.model.entity.ShoppingCart;
import com.algaworks.algashop.ordering.domain.model.exception.CustomerAlreadyHaveShoppingCartException;
import com.algaworks.algashop.ordering.domain.model.exception.CustomerNotFoundException;
import com.algaworks.algashop.ordering.domain.model.repository.Customers;
import com.algaworks.algashop.ordering.domain.model.repository.ShoppingCarts;
import com.algaworks.algashop.ordering.domain.model.utility.DomainService;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
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
