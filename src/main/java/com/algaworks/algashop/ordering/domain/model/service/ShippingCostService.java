package com.algaworks.algashop.ordering.domain.model.service;

import com.algaworks.algashop.ordering.domain.model.valueobject.Money;
import com.algaworks.algashop.ordering.domain.model.valueobject.ZipCode;
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
