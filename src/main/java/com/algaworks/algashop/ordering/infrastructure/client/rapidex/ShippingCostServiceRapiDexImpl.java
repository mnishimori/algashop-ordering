package com.algaworks.algashop.ordering.infrastructure.client.rapidex;

import com.algaworks.algashop.ordering.domain.model.service.ShippingCostService;
import com.algaworks.algashop.ordering.domain.model.valueobject.Money;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "algashop.integrations.shipping.provider", havingValue = "RAPIDEX")
public class ShippingCostServiceRapiDexImpl implements ShippingCostService {

  private final RapiDexApiClient rapiDexApiClient;

  @Override
  public CalculationResult calculate(CalculationRequest calculationRequest) {
    var deliveryCostRequest = new DeliveryCostRequest(calculationRequest.origin().value(),
        calculationRequest.destination().value());
    var response = rapiDexApiClient.calculate(deliveryCostRequest);
    var expectedDeliveryDate = LocalDate.now().plusDays(Long.parseLong(response.getEstimatedDaysToDeliver()));
    var money = new Money(response.getDeliveryCost());
    return CalculationResult.builder().cost(money).expectedDate(expectedDeliveryDate).build();
  }
}
