package com.algaworks.algashop.ordering.application.checkout;

import com.algaworks.algashop.ordering.application.commons.AddressData;
import com.algaworks.algashop.ordering.domain.model.commons.Address;
import com.algaworks.algashop.ordering.domain.model.commons.Document;
import com.algaworks.algashop.ordering.domain.model.commons.FullName;
import com.algaworks.algashop.ordering.domain.model.commons.Phone;
import com.algaworks.algashop.ordering.domain.model.commons.ZipCode;
import com.algaworks.algashop.ordering.domain.model.order.Recipient;
import com.algaworks.algashop.ordering.domain.model.order.Shipping;
import com.algaworks.algashop.ordering.domain.model.order.shipping.ShippingCostService;
import org.springframework.stereotype.Component;

@Component
class ShippingInputDisassembler {

  public Shipping toDomainModel(ShippingInput shippingInput,
      ShippingCostService.CalculationResult shippingCalculationResult) {
    AddressData address = shippingInput.getAddress();
    return Shipping.builder()
        .shippingCost(shippingCalculationResult.cost())
        .expectedDeliveryDate(shippingCalculationResult.expectedDate())
        .recipient(Recipient.builder()
            .fullName(new FullName(
                shippingInput.getRecipient().getFirstName(),
                shippingInput.getRecipient().getLastName()))
            .document(new Document(shippingInput.getRecipient().getDocument()))
            .phone(new Phone(shippingInput.getRecipient().getPhone()))
            .build())
        .address(Address.builder()
            .street(address.getStreet())
            .number(address.getNumber())
            .complement(address.getComplement())
            .neighborhood(address.getNeighbourhood())
            .city(address.getCity())
            .state(address.getState())
            .zipCode(new ZipCode(address.getZipCode()))
            .build())
        .build();
  }
}