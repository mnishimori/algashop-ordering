package com.algaworks.algashop.ordering.infrastructure.persistence.customer;

import com.algaworks.algashop.ordering.application.customer.query.CustomerFilter;
import com.algaworks.algashop.ordering.application.customer.query.CustomerOutput;
import com.algaworks.algashop.ordering.application.customer.query.CustomerQueryService;
import com.algaworks.algashop.ordering.application.customer.query.CustomerSummaryOutput;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerId;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

  @Override
  public Page<CustomerSummaryOutput> filter(CustomerFilter filter) {
    var pageRequest = PageRequest.of(filter.getPage(), filter.getSize());
    var total = countTotalQueryResults(filter);

    if (total.equals(0L)) {
      return new PageImpl<>(new ArrayList<>(), pageRequest, total);
    }

    return filterQuery(filter, pageRequest, total);
  }

  private Long countTotalQueryResults(CustomerFilter filter) {
    var builder = entityManager.getCriteriaBuilder();
    var criteriaQuery = builder.createQuery(Long.class);
    var root = criteriaQuery.from(CustomerPersistenceEntity.class);
    criteriaQuery.select(builder.count(root));
    criteriaQuery.where(toPredicates(builder, root, filter));
    return entityManager.createQuery(criteriaQuery).getSingleResult();
  }

  private Page<CustomerSummaryOutput> filterQuery(CustomerFilter filter, PageRequest pageRequest,
      Long total) {
    var builder = entityManager.getCriteriaBuilder();
    var criteriaQuery = builder.createQuery(CustomerSummaryOutput.class);
    var root = criteriaQuery.from(CustomerPersistenceEntity.class);

    criteriaQuery.select(builder.construct(CustomerSummaryOutput.class,
        root.get("id"),
        root.get("firstName"),
        root.get("lastName"),
        root.get("email"),
        root.get("document"),
        root.get("phone"),
        root.get("birthDate"),
        root.get("loyaltyPoints"),
        root.get("registeredAt"),
        root.get("archivedAt"),
        root.get("promotionNotificationsAllowed"),
        root.get("archived")));
    criteriaQuery.where(toPredicates(builder, root, filter));

    var sortPath = root.get(filter.getSortByPropertyOrDefault().getPropertyName());
    criteriaQuery.orderBy(filter.getSortDirectionOrDefault() == Sort.Direction.DESC
        ? builder.desc(sortPath)
        : builder.asc(sortPath));

    var typedQuery = entityManager.createQuery(criteriaQuery);
    typedQuery.setFirstResult(filter.getSize() * filter.getPage());
    typedQuery.setMaxResults(filter.getSize());
    return new PageImpl<>(typedQuery.getResultList(), pageRequest, total);
  }

  private Predicate[] toPredicates(CriteriaBuilder builder, Root<CustomerPersistenceEntity> root,
      CustomerFilter filter) {
    var predicates = new ArrayList<Predicate>();
    if (filter.getEmail() != null && !filter.getEmail().isBlank()) {
      predicates.add(builder.like(builder.lower(root.get("email")),
          "%" + filter.getEmail().toLowerCase() + "%"));
    }
    if (filter.getFirstName() != null && !filter.getFirstName().isBlank()) {
      predicates.add(builder.like(builder.lower(root.get("firstName")),
          "%" + filter.getFirstName().toLowerCase() + "%"));
    }
    return predicates.toArray(new Predicate[]{});
  }
}
