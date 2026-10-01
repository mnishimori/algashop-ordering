package com.algaworks.algashop.ordering.infrastructure.client.rapidex;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.algaworks.algashop.ordering.IntegrationTest;
import com.algaworks.algashop.ordering.domain.model.service.ShippingCostService;
import com.algaworks.algashop.ordering.domain.model.valueobject.ZipCode;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.TestPropertySource;

@IntegrationTest
@TestPropertySource(properties = "algashop.integrations.shipping.provider=RAPIDEX")
class ShippingCostServiceRapiDexImplIntegrationTest {

  @Autowired
  private ShippingCostService shippingCostService;

  @MockitoBean
  private RapiDexApiClient rapiDexApiClient;

  @Test
  void shouldCalculateShippingCost() {
    var request = new DeliveryCostRequest("01001-000", "20040-020");
    when(rapiDexApiClient.calculate(request))
        .thenReturn(new DeliveryCostResponse("12.34", "5"));

    var result = shippingCostService.calculate(ShippingCostService.CalculationRequest.builder()
        .origin(new ZipCode("01001-000"))
        .destination(new ZipCode("20040-020"))
        .build());

    assertThat(result.cost().value()).isEqualByComparingTo(new BigDecimal("12.34"));
    assertThat(result.expectedDate()).isEqualTo(LocalDate.now().plusDays(5));
  }

  @Test
  void shouldPropagateExceptionWhenShippingProviderFails() {
    var request = new DeliveryCostRequest("01001-000", "20040-020");
    when(rapiDexApiClient.calculate(request)).thenThrow(new IllegalStateException("Rapidex unavailable"));

    var calculationRequest = ShippingCostService.CalculationRequest.builder()
        .origin(new ZipCode("01001-000"))
        .destination(new ZipCode("20040-020"))
        .build();

    assertThatThrownBy(() -> shippingCostService.calculate(calculationRequest))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Rapidex unavailable");
  }
}
