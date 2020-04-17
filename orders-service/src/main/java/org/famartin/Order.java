package org.famartin;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Order
 */
public class Order {

    @JsonProperty("order-id")
    public String orderId;
    @JsonProperty("item-id")
    public String itemId;
    public Integer quantity;

}