package com.algaworks.algashop.ordering.infrastructure.listener.shoppingcart;

import com.algaworks.algashop.ordering.domain.model.shoppingcart.ShoppingCartCreatedEvent;
import com.algaworks.algashop.ordering.domain.model.shoppingcart.ShoppingCartEmptyEvent;
import com.algaworks.algashop.ordering.domain.model.shoppingcart.ShoppingCartItemAddedEvent;
import com.algaworks.algashop.ordering.domain.model.shoppingcart.ShoppingCartItemRemovedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ShoppingCartEventListener {

  @EventListener
  public void listen(ShoppingCartCreatedEvent shoppingCartCreatedEvent){
    log.info("Shopping cart created: {}", shoppingCartCreatedEvent.shoppingCartId());
  }

  @EventListener
  public void listen(ShoppingCartItemAddedEvent shoppingCartItemAddedEvent){
    log.info("Shopping cart item added: {}", shoppingCartItemAddedEvent.shoppingCartItemId());
  }

  @EventListener
  public void listen(ShoppingCartItemRemovedEvent shoppingCartItemRemovedEvent){
    log.info("Shopping cart item removed: {}", shoppingCartItemRemovedEvent.shoppingCartItemId());
  }

  @EventListener
  public void listen(ShoppingCartEmptyEvent shoppingCartEmptyEvent) {
    log.info("Shopping cart empty: {}", shoppingCartEmptyEvent.shoppingCartId());
  }
}
