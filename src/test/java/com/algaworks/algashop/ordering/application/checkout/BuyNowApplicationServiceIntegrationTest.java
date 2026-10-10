package com.algaworks.algashop.ordering.application.checkout;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.algaworks.algashop.ordering.IntegrationTest;
import com.algaworks.algashop.ordering.application.commons.AddressData;
import com.algaworks.algashop.ordering.domain.entity.CustomerTestDataBuilder;
import com.algaworks.algashop.ordering.domain.model.commons.Address;
import com.algaworks.algashop.ordering.domain.model.commons.Money;
import com.algaworks.algashop.ordering.domain.model.commons.ZipCode;
import com.algaworks.algashop.ordering.domain.model.order.shipping.OriginAddressService;
import com.algaworks.algashop.ordering.domain.model.order.shipping.ShippingCostService;
import com.algaworks.algashop.ordering.domain.model.order.shipping.ShippingCostService.CalculationResult;
import com.algaworks.algashop.ordering.domain.model.product.Product;
import com.algaworks.algashop.ordering.domain.model.product.ProductCatalogService;
import com.algaworks.algashop.ordering.domain.model.product.ProductId;
import com.algaworks.algashop.ordering.domain.model.product.ProductName;
import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@IntegrationTest
class BuyNowApplicationServiceIntegrationTest {

  private static final UUID PRODUCT_ID = UUID.fromString("550e8400-e29b-41d4-a716-446655440010");

  @Autowired
  private BuyNowApplicationService buyNowApplicationService;

  @MockitoBean
  private ProductCatalogService productCatalogService;

  @MockitoBean
  private ShippingCostService shippingCostService;

  @MockitoBean
  private OriginAddressService originAddressService;

  @Test
  void shouldBuyNowSuccessfully() {
    var input = aBuyNowInput();
    when(productCatalogService.ofId(new ProductId(PRODUCT_ID))).thenReturn(Optional.of(aProduct()));
    when(originAddressService.originAddress()).thenReturn(anOriginAddress());
    when(shippingCostService.calculate(any())).thenReturn(
        new CalculationResult(new Money("15.00"), LocalDate.now().plusDays(5)));

    var orderId = buyNowApplicationService.buyNow(input);

    assertThat(orderId).isNotBlank();
    verify(shippingCostService).calculate(any());
  }

  @Test
  void shouldThrowExceptionWhenProductDoesNotExist() {
    var input = aBuyNowInput();
    when(productCatalogService.ofId(new ProductId(PRODUCT_ID))).thenReturn(Optional.empty());

    assertThatThrownBy(() -> buyNowApplicationService.buyNow(input))
        .isInstanceOf(NoSuchElementException.class);
    verify(shippingCostService, never()).calculate(any());
  }

  @Test
  void shouldThrowExceptionWhenPaymentMethodIsInvalid() {
    var input = aBuyNowInput();
    input.setPaymentMethod("BITCOIN");
    when(productCatalogService.ofId(new ProductId(PRODUCT_ID))).thenReturn(Optional.of(aProduct()));
    when(originAddressService.originAddress()).thenReturn(anOriginAddress());
    when(shippingCostService.calculate(any())).thenReturn(
        new CalculationResult(new Money("15.00"), LocalDate.now().plusDays(5)));

    assertThatThrownBy(() -> buyNowApplicationService.buyNow(input))
        .isInstanceOf(IllegalArgumentException.class);
  }

  private static BuyNowInput aBuyNowInput() {
    return BuyNowInput.builder()
        .customerId(CustomerTestDataBuilder.existedCustomer().build().id().value())
        .productId(PRODUCT_ID)
        .quantity(2)
        .paymentMethod("CREDIT_CARD")
        .billing(BillingData.builder()
            .firstName(CustomerTestDataBuilder.FIRST_NAME)
            .lastName(CustomerTestDataBuilder.LAST_NAME)
            .document(CustomerTestDataBuilder.DOCUMENT)
            .email(CustomerTestDataBuilder.EMAIL)
            .phone(CustomerTestDataBuilder.PHONE)
            .address(anAddressData())
            .build())
        .shipping(ShippingInput.builder()
            .recipient(RecipientData.builder()
                .firstName(CustomerTestDataBuilder.FIRST_NAME)
                .lastName(CustomerTestDataBuilder.LAST_NAME)
                .document(CustomerTestDataBuilder.DOCUMENT)
                .phone(CustomerTestDataBuilder.PHONE)
                .build())
            .address(anAddressData())
            .build())
        .build();
  }

  private static AddressData anAddressData() {
    return AddressData.builder()
        .street(CustomerTestDataBuilder.BOURBON_STREET)
        .number(CustomerTestDataBuilder.NUMBER)
        .neighbourhood(CustomerTestDataBuilder.NORTH_VALLEY)
        .city(CustomerTestDataBuilder.NEW_YORK)
        .state(CustomerTestDataBuilder.NEW_YORK)
        .zipCode(CustomerTestDataBuilder.ZIP_CODE)
        .build();
  }

  private static Product aProduct() {
    return Product.builder()
        .id(new ProductId(PRODUCT_ID))
        .name(new ProductName("Notebook Pro"))
        .price(new Money("3000.00"))
        .inStock(true)
        .build();
  }

  private static Address anOriginAddress() {
    return Address.builder()
        .street("Rua de Origem")
        .number("100")
        .neighborhood("Centro")
        .city("São Paulo")
        .state("SP")
        .zipCode(new ZipCode("01000-000"))
        .build();
  }
}
