package com.algaworks.algashop.ordering.domain.messages;

public class ErrorMessages {
  public static final String BIRTHDATE_MUST_IN_PAST = "BirthDate must be a past date";
  public static final String DOCUMENT_CANNOT_BE_BLANK = "Document cannot be blank";
  public static final String PHONE_CANNOT_BE_BLANK = "Phone cannot be blank";
  public static final String EMAIL_IS_INVALID = "Email is invalid";
  public static final String EMAIL_CANNOT_BE_BLANK = "Email cannot be blank";
  public static final String CUSTOMER_ARCHIVED = "Customer is archived";
  public static final String LOYALTY_POINTS_CANNOT_BE_NEGATIVE_OR_ZERO = "Loyalty points cannot be negative or zero";
  public static final String FULL_NAME_CANNOT_BE_BLANK = "Full name cannot be blank";
  public static final String ZIP_CODE_IS_INVALID = "Zip code is invalid";
  public static final String QUANTITY_MUST_BE_GREATER_THAN_ZERO = "Quantity must be greater than zero";
  public static final String VALUE_CANNOT_BE_NULL_OR_EMPTY = "Value cannot be null or empty";
  public static final String VALUE_CANNOT_BE_NEGATIVE = "Value cannot be negative";
  public static final String PRODUCT_NAME_CANNOT_BE_NULL_OR_EMPTY = "Product name cannot be null or empty";
  public static final String ORDER_STATUS_CANNOT_BE_CHANGED = "Order status cannot be changed. Order ID: %s, Order status: %s, New status: %s";
  public static final String ORDER_INVALID_SHIPPING_DELIVERY_DATE = "Order delivery date cannot be in the past. Order ID: %s, Delivery date: %s";
}
