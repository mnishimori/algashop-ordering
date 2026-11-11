package com.algaworks.algashop.ordering.infrastructure.persistence.customer;

import com.algaworks.algashop.ordering.application.customer.query.CustomerOutput;
import com.algaworks.algashop.ordering.application.customer.query.CustomerQueryService;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerId;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomerQueryServiceImpl implements CustomerQueryService {

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
  public CustomerOutput findById(UUID id) {
    Objects.requireNonNull(id);
    try {
      var query = entityManager.createQuery(findByIdAsOutputJPQL, CustomerOutput.class);
      query.setParameter("id", id);
      return query.getSingleResult();
    } catch (NoResultException e) {
      throw new CustomerNotFoundException(new CustomerId(id));
    }
  }
}
