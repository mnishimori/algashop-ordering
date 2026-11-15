package com.algaworks.algashop.ordering.application.utility;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;


@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public abstract class SortablePageFilter<T> extends PageFilter {

  private T sortByProperty;
  private Sort.Direction sortDirection;

  protected SortablePageFilter(int size, int page) {
    super(size, page);
  }

  public abstract T getSortByPropertyOrDefault();

  public abstract Sort.Direction getSortDirectionOrDefault();
}
