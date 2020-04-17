package org.famartin;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.dapr.client.DaprClient;
import io.dapr.client.domain.State;
import io.dapr.client.domain.StateOptions;
import io.dapr.client.domain.Verb;
import io.dapr.client.domain.StateOptions.Concurrency;
import io.dapr.client.domain.StateOptions.Consistency;
import io.dapr.client.domain.StateOptions.RetryPolicy;
import io.dapr.client.domain.StateOptions.RetryPolicy.Pattern;
import io.vertx.core.json.JsonObject;

@Path("/stocks")
public class StocksService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String stateStoreName = "stocks-store";

    @Inject
    DaprClient dapr;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("request")
    public Map<String, Object> stockRequest(StockOp order) {
        logger.info("Stock req received");
        Map<String, Object> result = new HashMap<>();

        StateOptions options = new StateOptions(Consistency.STRONG, Concurrency.FIRST_WRITE, new RetryPolicy(Duration.ofSeconds(1), 3, Pattern.LINEAR));
        State<StockEntry> state = dapr.getState(stateStoreName, order.itemId, null, options, StockEntry.class).block();
        StockEntry currentStock = state.getValue();
        if (currentStock == null) {
            result.put("approved", false);
            result.put("message", "There is not stock for that item");
        } else if (currentStock.stock >= order.quantity) {
            result.put("approved", true);
            StockEntry newStock = new StockEntry();
            newStock.itemId = currentStock.itemId;
            newStock.stock = currentStock.stock - order.quantity;
            result.put("message", "New stock "+newStock.stock);
            dapr.saveState(stateStoreName, state.getKey(), state.getEtag(), newStock, options).block();
        } else if (currentStock.stock < order.quantity) {
            result.put("approved", false);
            result.put("message", "Stock request exceeded current stock");
        }
        logger.info("Stock req processed, {}", result.get("message"));
        return result;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("add")
    public StockEntry stockAdd(StockOp op) {

        StateOptions options = new StateOptions(Consistency.STRONG, Concurrency.FIRST_WRITE, new RetryPolicy(Duration.ofSeconds(1), 3, Pattern.LINEAR));
        State<StockEntry> state = dapr.getState(stateStoreName, op.itemId, null, options, StockEntry.class).block();
        StockEntry currentStock = state.getValue();
        if (currentStock == null) {
            currentStock = new StockEntry();
            currentStock.itemId = op.itemId;
            currentStock.stock = 0;
        }
        currentStock.stock = currentStock.stock + op.quantity;

        dapr.saveState(stateStoreName, state.getKey(), /*state.getEtag() == null ? "0" :*/ state.getEtag(), currentStock, options).block();

        state = dapr.getState(stateStoreName, op.itemId, null, options, StockEntry.class).block();

        logger.info("New etag: " + state.getEtag());

        return currentStock;
    }

}