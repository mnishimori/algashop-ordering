package com.algaworks.algashop.ordering.infrastructure.persistence.shoppingcart;

import com.algaworks.algashop.ordering.application.shoppingcart.query.ShoppingCartItemOutput;
import com.algaworks.algashop.ordering.application.shoppingcart.query.ShoppingCartOutput;
import com.algaworks.algashop.ordering.application.shoppingcart.query.ShoppingCartQueryService;
import com.algaworks.algashop.ordering.domain.model.shoppingcart.ShoppingCartId;
import com.algaworks.algashop.ordering.domain.model.shoppingcart.ShoppingCartNotFoundException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShoppingCartQueryServiceImpl implements ShoppingCartQueryService {

  private final ShoppingCartPersistenceEntityRepository repository;

  @Override
  public ShoppingCartOutput findById(UUID shoppingCartId) {
    var shoppingCart = repository.findById(shoppingCartId)
        .orElseThrow(() -> new ShoppingCartNotFoundException(new ShoppingCartId(shoppingCartId)));
    return toOutput(shoppingCart);
  }

  @Override
  public ShoppingCartOutput findByCustomerId(UUID customerId) {
    var shoppingCart = repository.findByCustomer_Id(customerId)
        .orElseThrow(() -> new ShoppingCartNotFoundException(new ShoppingCartId(customerId)));
    return toOutput(shoppingCart);
  }

  private ShoppingCartOutput toOutput(ShoppingCartPersistenceEntity entity) {
    var output = new ShoppingCartOutput();
    output.setId(entity.getId());
    output.setCustomerId(entity.getCustomer().getId());
    output.setTotalItems(entity.getTotalItems());
    output.setTotalAmount(entity.getTotalAmount());
    if (entity.getItems() != null) {
      entity.getItems().forEach(item -> output.getItems().add(toItemOutput(item)));
    }
    return output;
  }

  private ShoppingCartItemOutput toItemOutput(ShoppingCartItemPersistenceEntity item) {
    var output = new ShoppingCartItemOutput();
    output.setId(item.getId());
    output.setProductId(item.getProductId());
    output.setName(item.getProductName());
    output.setPrice(item.getPrice());
    output.setQuantity(item.getQuantity());
    output.setTotalAmount(item.getTotalAmount());
    output.setAvailable(item.getAvailable());
    return output;
  }
}
