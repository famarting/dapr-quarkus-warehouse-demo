package org.famartin;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Order
 */
public class Order {

    @JsonProperty("order-id")
    public String orderId;

    @NotNull
    @NotEmpty
    @JsonProperty("item-id")
    public String itemId;

    @NotNull
    @Positive
    public Integer quantity;

}