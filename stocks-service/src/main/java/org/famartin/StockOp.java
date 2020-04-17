package org.famartin;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StockOp {

    @JsonProperty("item-id")
    public String itemId;
    public Integer quantity;

}