package com.algaworks.algashop.ordering.application.customer.management;

import java.util.UUID;

public interface CustomerQueryService {

  CustomerOutput findById(UUID id);
}
