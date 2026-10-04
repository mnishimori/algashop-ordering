package com.algaworks.algashop.ordering.infrastructure.persistence.commons;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class RecipientEmbeddable {

  private String firstName;
  private String LastName;
  private String document;
  private String phone;
}
