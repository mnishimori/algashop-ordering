package com.algaworks.algashop.ordering.infrastructure.listener.customer;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import com.algaworks.algashop.ordering.IntegrationTest;
import com.algaworks.algashop.ordering.application.customer.loyaltypoints.CustomerLoyaltyPointsApplicationService;
import com.algaworks.algashop.ordering.application.customer.notification.CustomerNotificationApplicationService;
import com.algaworks.algashop.ordering.application.customer.notification.CustomerNotificationApplicationService.NotifyNewRegistrationInput;
import com.algaworks.algashop.ordering.domain.entity.CustomerTestDataBuilder;
import com.algaworks.algashop.ordering.domain.model.commons.Email;
import com.algaworks.algashop.ordering.domain.model.commons.FullName;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerArchivedEvent;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerId;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerRegisteredEvent;
import com.algaworks.algashop.ordering.domain.model.order.OrderId;
import com.algaworks.algashop.ordering.domain.model.order.OrderReadyEvent;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

@IntegrationTest
class CustomerEventListenerIT {

  @Autowired
  private ApplicationEventPublisher eventPublisher;
  @MockitoSpyBean
  private CustomerEventListener customerEventListener;
  @MockitoBean
  private CustomerLoyaltyPointsApplicationService customerLoyaltyPointsApplicationService;
  @MockitoBean
  private CustomerNotificationApplicationService customerNotificationApplicationService;

  @Test
  void shouldListenOrderReadyEvent() {
    var orderId = new OrderId();
    var customerId = new CustomerId();
    var orderReadyEvent = new OrderReadyEvent(orderId, customerId, OffsetDateTime.now());
    eventPublisher.publishEvent(orderReadyEvent);

    verify(customerEventListener).listen(any(OrderReadyEvent.class));
    verify(customerLoyaltyPointsApplicationService).addLoyaltyPoints(customerId.value(), orderId.toString());
  }

  @Test
  void shouldListenCustomerRegisteredEvent() {
    var customerId = new CustomerId();
    var registeredEvent = new CustomerRegisteredEvent(customerId, OffsetDateTime.now(),
        new FullName(CustomerTestDataBuilder.FIRST_NAME, CustomerTestDataBuilder.LAST_NAME),
        new Email(CustomerTestDataBuilder.EMAIL));
    eventPublisher.publishEvent(registeredEvent);

    verify(customerEventListener).listen(any(CustomerRegisteredEvent.class));
    verify(customerNotificationApplicationService).notifyNewCustomerRegistration(
        new NotifyNewRegistrationInput(customerId.value(), CustomerTestDataBuilder.FIRST_NAME,
            CustomerTestDataBuilder.EMAIL));
  }

  @Test
  void shouldListenCustomerArchivedEvent() {
    var archivedEvent = new CustomerArchivedEvent(new CustomerId(), OffsetDateTime.now());
    eventPublisher.publishEvent(archivedEvent);

    verify(customerEventListener).listen(any(CustomerArchivedEvent.class));
    verifyNoInteractions(customerNotificationApplicationService, customerLoyaltyPointsApplicationService);
  }

}