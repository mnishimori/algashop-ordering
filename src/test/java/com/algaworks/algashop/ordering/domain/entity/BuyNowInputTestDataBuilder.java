package com.algaworks.algashop.ordering.domain.entity;

import com.algaworks.algashop.ordering.application.checkout.BillingData;
import com.algaworks.algashop.ordering.application.checkout.BuyNowInput;
import com.algaworks.algashop.ordering.application.checkout.RecipientData;
import com.algaworks.algashop.ordering.application.checkout.ShippingInput;
import com.algaworks.algashop.ordering.application.commons.AddressData;

public class BuyNowInputTestDataBuilder {

    public static BuyNowInput.BuyNowInputBuilder aBuyNowInput() {
        return BuyNowInput.builder()
                .productId(ProductTestDataBuilder.DEFAULT_PRODUCT_ID.value())
                .customerId(ProductTestDataBuilder.DEFAULT_CUSTOMER_ID)
                .quantity(2)
                .paymentMethod("CREDIT_CARD")
                .shipping(ShippingInput.builder()
                        .recipient(RecipientData.builder()
                                .firstName("John")
                                .lastName("Doe")
                                .document("255-08-0578")
                                .phone("478-256-2604")
                                .build())
                        .address(AddressData.builder()
                                .street("Elm Street")
                                .number("456")
                                .complement("House A")
                                .neighbourhood("Central Park")
                                .city("Springfield")
                                .state("Illinois")
                                .zipCode("62704")
                                .build())
                        .build())
                .billing(BillingData.builder()
                        .firstName("Matt")
                        .lastName("Damon")
                        .phone("123-321-1112")
                        .document("123-45-6789")
                        .email("matt.damon@email.com")
                        .address(AddressData.builder()
                                .street("Amphitheatre Parkway")
                                .number("1600")
                                .complement("")
                                .neighbourhood("Mountain View")
                                .city("Mountain View")
                                .state("California")
                                .zipCode("94043")
                                .build())
                        .build());
    }

}