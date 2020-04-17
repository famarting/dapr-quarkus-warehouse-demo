package org.famartin;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StockEntry {

    @JsonProperty("item-id")
    public String itemId;
    public Integer stock;

}