package com.algaworks.algashop.ordering.domain.model.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.algaworks.algashop.ordering.domain.entity.CustomerTestDataBuilder;
import com.algaworks.algashop.ordering.domain.entity.ShoppingCartTestDataBuilder;
import com.algaworks.algashop.ordering.domain.model.shoppingcart.ShoppingCart;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerAlreadyHaveShoppingCartException;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerNotFoundException;
import com.algaworks.algashop.ordering.domain.model.customer.Customers;
import com.algaworks.algashop.ordering.domain.model.shoppingcart.ShoppingCarts;
import com.algaworks.algashop.ordering.domain.model.shoppingcart.ShoppingService;
import com.algaworks.algashop.ordering.domain.model.commons.Money;
import com.algaworks.algashop.ordering.domain.model.commons.Quantity;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerId;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ShoppingServiceTest {

  @Mock
  private Customers customers;

  @Mock
  private ShoppingCarts shoppingCarts;

  @InjectMocks
  private ShoppingService shoppingService;

  @Test
  void shouldStartShoppingSuccessfully() {
    var customerId = aCustomerId();
    when(customers.existsById(eq(customerId))).thenReturn(true);
    when(shoppingCarts.ofCustomer(eq(customerId))).thenReturn(Optional.empty());

    ShoppingCart shoppingCart = shoppingService.startShopping(customerId);

    assertThat(shoppingCart).isNotNull();
    assertThat(shoppingCart.customerId()).isEqualTo(customerId);
    assertThat(shoppingCart.totalAmount()).isEqualTo(Money.ZERO);
    assertThat(shoppingCart.totalItems()).isEqualTo(Quantity.ZERO);
    assertThat(shoppingCart.items()).isEmpty();
    assertThat(shoppingCart.createdAt()).isNotNull();

    verify(customers).existsById(customerId);
    verify(shoppingCarts).ofCustomer(customerId);
  }

  @Test
  void shouldThrowCustomerNotFoundExceptionWhenCustomerDoesNotExist() {
    var customerId = aCustomerId();
    when(customers.existsById(eq(customerId))).thenReturn(false);

    assertThatThrownBy(() -> shoppingService.startShopping(customerId))
        .isInstanceOf(CustomerNotFoundException.class);

    verify(customers).existsById(customerId);
    verify(shoppingCarts, never()).ofCustomer(customerId);
  }

  @Test
  void shouldThrowCustomerAlreadyHaveShoppingCartExceptionWhenCustomerAlreadyHasCart() {
    var customerId = aCustomerId();
    var existingShoppingCart = ShoppingCartTestDataBuilder.aShoppingCart()
        .customerId(customerId)
        .build();

    when(customers.existsById(eq(customerId))).thenReturn(true);
    when(shoppingCarts.ofCustomer(eq(customerId))).thenReturn(Optional.of(existingShoppingCart));

    assertThatThrownBy(() -> shoppingService.startShopping(customerId))
        .isInstanceOf(CustomerAlreadyHaveShoppingCartException.class);

    verify(customers).existsById(customerId);
    verify(shoppingCarts).ofCustomer(customerId);
  }

  private CustomerId aCustomerId() {
    return CustomerTestDataBuilder.existedCustomer().build().id();
  }
}
