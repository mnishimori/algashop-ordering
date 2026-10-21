package com.algaworks.algashop.ordering.application.customer.notification;

import java.util.UUID;

public interface CustomerNotificationApplicationService {
  void notifyNewCustomerRegistration(NotifyNewRegistrationInput input);

  record NotifyNewRegistrationInput(UUID customerId, String firstname, String email){}
}
