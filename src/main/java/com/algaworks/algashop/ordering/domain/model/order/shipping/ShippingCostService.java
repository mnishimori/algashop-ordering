package com.algaworks.algashop.ordering.domain.model.order.shipping;

import com.algaworks.algashop.ordering.domain.model.commons.Money;
import com.algaworks.algashop.ordering.domain.model.commons.ZipCode;
import java.time.LocalDate;
import lombok.Builder;

public interface ShippingCostService {

  CalculationResult calculate(CalculationRequest calculationRequest);

  @Builder
  record CalculationRequest(ZipCode origin, ZipCode destination){

  }

  @Builder
  record CalculationResult(Money cost, LocalDate expectedDate) {

  }

}
