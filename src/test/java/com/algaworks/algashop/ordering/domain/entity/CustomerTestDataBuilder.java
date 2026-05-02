package com.algaworks.algashop.ordering.domain.entity;


import com.algaworks.algashop.ordering.domain.valueobject.Address;
import com.algaworks.algashop.ordering.domain.valueobject.FullName;
import com.algaworks.algashop.ordering.domain.valueobject.LoyaltyPoints;
import com.algaworks.algashop.ordering.domain.valueobject.ZipCode;
import java.time.LocalDate;

public class CustomerTestDataBuilder {

  public static final String FIRST_NAME = "João";
  public static final String LAST_NAME = "Silva";
  public static final LocalDate BIRTH_DATE = LocalDate.of(1990, 5, 15);
  public static final String EMAIL = "joao.silva@example.com";
  public static final String PHONE = "11999999999";
  public static final String DOCUMENT = "12345678901";
  public static final String BOURBON_STREET = "Bourbon Street";
  public static final String NUMBER = "1234";
  public static final String NORTH_VALLEY = "North Valley";
  public static final String NEW_YORK = "New York";
  public static final String ZIP_CODE = "12345-678";

  private CustomerTestDataBuilder() {
  }

  public static Customer.BrandNewCustomerBuilder brandNewCustomer() {
    var address = createCustomerAddress();
    return Customer.brandnew().fullName(new FullName(FIRST_NAME, LAST_NAME)).birthDate(BIRTH_DATE).email(EMAIL)
        .phone(PHONE).document(DOCUMENT).promotionNotificationsAllowed(false).address(address);
  }

  public static Customer.ExistedCustomerBuilder existedCustomer() {
    var address = createCustomerAddress();
    return Customer.existed().fullName(new FullName(FIRST_NAME, LAST_NAME)).birthDate(BIRTH_DATE).email(EMAIL)
        .phone(PHONE).document(DOCUMENT).promotionNotificationsAllowed(true).address(address)
        .loyaltyPoints(LoyaltyPoints.ZERO).archived(false);
  }

  private static Address createCustomerAddress() {
    return Address.builder()
        .street(BOURBON_STREET)
        .number(NUMBER)
        .neighborhood(NORTH_VALLEY)
        .city(NEW_YORK)
        .state(NEW_YORK)
        .zipCode(new ZipCode(ZIP_CODE))
        .build();
  }
}
