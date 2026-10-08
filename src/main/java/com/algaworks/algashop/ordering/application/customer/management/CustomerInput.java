package com.algaworks.algashop.ordering.application.customer.management;

import com.algaworks.algashop.ordering.application.commons.AddressData;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerInput {

  private String firstName;
  private String lastName;
  private String email;
  private String phone;
  private String document;
  private LocalDate birthDate;
  private Boolean promotionNotificationsAllowed;
  private AddressData address;

}
