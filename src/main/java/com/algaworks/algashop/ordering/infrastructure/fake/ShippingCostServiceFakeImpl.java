package com.algaworks.algashop.ordering.infrastructure.fake;

import com.algaworks.algashop.ordering.domain.model.service.ShippingCostService;
import com.algaworks.algashop.ordering.domain.model.valueobject.Money;
import java.time.LocalDate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "algashop.integrations.shipping.provider", havingValue = "FAKE")
public class ShippingCostServiceFakeImpl implements ShippingCostService {

  @Override
  public CalculationResult calculate(CalculationRequest calculationRequest) {
    return new CalculationResult(new Money("20"), LocalDate.now().plusDays(5));
  }
}
