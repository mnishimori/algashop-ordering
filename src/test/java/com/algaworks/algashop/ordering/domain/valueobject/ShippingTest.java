package com.algaworks.algashop.ordering.domain.valueobject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.algaworks.algashop.ordering.domain.model.valueobject.Address;
import com.algaworks.algashop.ordering.domain.model.valueobject.Document;
import com.algaworks.algashop.ordering.domain.model.valueobject.FullName;
import com.algaworks.algashop.ordering.domain.model.valueobject.Money;
import com.algaworks.algashop.ordering.domain.model.valueobject.Phone;
import com.algaworks.algashop.ordering.domain.model.valueobject.Recipient;
import com.algaworks.algashop.ordering.domain.model.valueobject.Shipping;
import com.algaworks.algashop.ordering.domain.model.valueobject.ZipCode;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ShippingTest {

  private static final FullName FULL_NAME = new FullName("John", "Doe");
  private static final Document DOCUMENT = new Document("12345678900");
  private static final Phone PHONE = new Phone("11999999999");
  private static final ZipCode ZIP_CODE = new ZipCode("12345-678");
  private static final Address ADDRESS = new Address("Main Street", "123", "Apt 1", "Downtown", "New York", "NY", ZIP_CODE);
  private static final Recipient RECIPIENT = new Recipient(FULL_NAME, DOCUMENT, PHONE);
  private static final Money SHIPPING_COST = new Money("25.00");
  private static final LocalDate EXPECTED_DELIVERY_DATE = LocalDate.now().plusDays(7);

  @Test
  @DisplayName("Should create Shipping with all valid components")
  void shouldCreateShippingWithAllValidComponents() {
    var shipping = new Shipping(SHIPPING_COST, EXPECTED_DELIVERY_DATE, RECIPIENT, ADDRESS);

    assertThat(shipping.shippingCost()).isEqualTo(SHIPPING_COST);
    assertThat(shipping.expectedDeliveryDate()).isEqualTo(EXPECTED_DELIVERY_DATE);
    assertThat(shipping.recipient()).isEqualTo(RECIPIENT);
  }

  @Test
  @DisplayName("Should create Shipping using builder")
  void shouldCreateShippingUsingBuilder() {
    var shipping = Shipping.builder()
        .shippingCost(SHIPPING_COST)
        .expectedDeliveryDate(EXPECTED_DELIVERY_DATE)
        .recipient(RECIPIENT)
        .address(ADDRESS)
        .build();

    assertThat(shipping.shippingCost()).isEqualTo(SHIPPING_COST);
    assertThat(shipping.expectedDeliveryDate()).isEqualTo(EXPECTED_DELIVERY_DATE);
    assertThat(shipping.recipient()).isEqualTo(RECIPIENT);
  }

  @Test
  @DisplayName("Should throw NullPointerException when shippingCost is null")
  void shouldThrowNullPointerExceptionWhenShippingCostIsNull() {
    assertThatThrownBy(() -> new Shipping(null, EXPECTED_DELIVERY_DATE, RECIPIENT, ADDRESS))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should throw NullPointerException when expectedDeliveryDate is null")
  void shouldThrowNullPointerExceptionWhenExpectedDeliveryDateIsNull() {
    assertThatThrownBy(() -> new Shipping(SHIPPING_COST, null, RECIPIENT, ADDRESS))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should throw NullPointerException when recepient is null")
  void shouldThrowNullPointerExceptionWhenRecepientIsNull() {
    assertThatThrownBy(() -> new Shipping(SHIPPING_COST, EXPECTED_DELIVERY_DATE, null, ADDRESS))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should be equal when same components")
  void shouldBeEqualWhenSameComponents() {
    var shipping1 = new Shipping(SHIPPING_COST, EXPECTED_DELIVERY_DATE, RECIPIENT, ADDRESS);
    var shipping2 = new Shipping(SHIPPING_COST, EXPECTED_DELIVERY_DATE, RECIPIENT, ADDRESS);

    assertThat(shipping1).isEqualTo(shipping2);
    assertThat(shipping1.hashCode()).isEqualTo(shipping2.hashCode());
  }

  @Test
  @DisplayName("Should not be equal when different shippingCost")
  void shouldNotBeEqualWhenDifferentShippingCost() {
    var shipping1 = new Shipping(SHIPPING_COST, EXPECTED_DELIVERY_DATE, RECIPIENT, ADDRESS);
    var differentCost = new Money("30.00");
    var shipping2 = new Shipping(differentCost, EXPECTED_DELIVERY_DATE, RECIPIENT, ADDRESS);

    assertThat(shipping1).isNotEqualTo(shipping2);
  }

  @Test
  @DisplayName("Should not be equal when different expectedDeliveryDate")
  void shouldNotBeEqualWhenDifferentExpectedDeliveryDate() {
    var shipping1 = new Shipping(SHIPPING_COST, EXPECTED_DELIVERY_DATE, RECIPIENT, ADDRESS);
    var differentDate = LocalDate.now().plusDays(14);
    var shipping2 = new Shipping(SHIPPING_COST, differentDate, RECIPIENT, ADDRESS);

    assertThat(shipping1).isNotEqualTo(shipping2);
  }

  @Test
  @DisplayName("Should not be equal when different recepient")
  void shouldNotBeEqualWhenDifferentRecepient() {
    var shipping1 = new Shipping(SHIPPING_COST, EXPECTED_DELIVERY_DATE, RECIPIENT, ADDRESS);
    var differentName = new FullName("Jane", "Smith");
    var differentRecepient = new Recipient(differentName, DOCUMENT, PHONE);
    var shipping2 = new Shipping(SHIPPING_COST, EXPECTED_DELIVERY_DATE, differentRecepient, ADDRESS);

    assertThat(shipping1).isNotEqualTo(shipping2);
  }

  @Test
  @DisplayName("Should not be equal to null")
  void shouldNotBeEqualToNull() {
    var shipping = new Shipping(SHIPPING_COST, EXPECTED_DELIVERY_DATE, RECIPIENT, ADDRESS);

    assertThat(shipping).isNotEqualTo(null);
  }

  @Test
  @DisplayName("Should not be equal to different type")
  void shouldNotBeEqualToDifferentType() {
    var shipping = new Shipping(SHIPPING_COST, EXPECTED_DELIVERY_DATE, RECIPIENT, ADDRESS);

    assertThat(shipping).isNotEqualTo("not a shipping");
  }
}
