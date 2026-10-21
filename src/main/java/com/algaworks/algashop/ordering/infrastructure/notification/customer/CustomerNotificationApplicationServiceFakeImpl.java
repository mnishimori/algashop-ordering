package com.algaworks.algashop.ordering.infrastructure.notification.customer;

import com.algaworks.algashop.ordering.application.customer.notification.CustomerNotificationApplicationService;
import com.algaworks.algashop.ordering.domain.model.customer.Customers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerNotificationApplicationServiceFakeImpl implements CustomerNotificationApplicationService {

  private final Customers customers;

  @Override
  public void notifyNewCustomerRegistration(NotifyNewRegistrationInput input) {
    log.info("Welcome {}", input.firstname());
    log.info("User your email to access your account {}", input.email());
  }
}
