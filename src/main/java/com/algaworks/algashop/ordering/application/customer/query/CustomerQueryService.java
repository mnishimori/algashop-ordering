package com.algaworks.algashop.ordering.application.customer.query;

import java.util.UUID;
import org.springframework.data.domain.Page;

public interface CustomerQueryService {

  CustomerOutput findById(UUID id);

  Page<CustomerSummaryOutput> filter(CustomerFilter filter);
}
