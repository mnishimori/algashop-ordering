package com.algaworks.algashop.ordering.infrastructure.persistence.customer;

import com.algaworks.algashop.ordering.application.customer.query.CustomerOutput;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomerPersistenceEntityQueriesImpl implements CustomerPersistenceEntityQueries {

  private final EntityManager entityManager;
  private static final String findByIdAsOutputJPQL = """
      SELECT new com.algaworks.algashop.ordering.application.customer.query.CustomerOutput(
          c.id,
          c.firstName,
          c.lastName,
          c.email,
          c.document,
          c.phone,
          c.birthDate,
          c.loyaltyPoints,
          c.registeredAt,
          c.archivedAt,
          c.promotionNotificationsAllowed,
          c.archived,
          new com.algaworks.algashop.ordering.application.commons.AddressData(
              c.addressEmbeddable.street,
              c.addressEmbeddable.number,
              c.addressEmbeddable.complement,
              c.addressEmbeddable.neighborhood,
              c.addressEmbeddable.city,
              c.addressEmbeddable.state,
              c.addressEmbeddable.zipCode
          )
      )
      FROM CustomerPersistenceEntity c
      WHERE c.id = :id""";

  @Override
  public Optional<CustomerOutput> findByIdAsOutput(UUID id) {
    try {
      var query = entityManager.createQuery(findByIdAsOutputJPQL, CustomerOutput.class);
      query.setParameter("id", id);
      return Optional.of(query.getSingleResult());
    } catch (NoResultException e) {
      return Optional.empty();
    }
  }
}
