package org.famartin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.dapr.client.DaprClient;
import io.dapr.client.domain.Verb;
import io.vertx.core.json.JsonObject;

@Path("/orders")
public class OrdersService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Inject
    DaprClient dapr;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response processOrder(Order order) {
        logger.info("Processing order, item {} quantity {}", order.itemId, order.quantity);
        
        order.orderId = UUID.randomUUID().toString();

        Map<String, Object> result = dapr.invokeService(Verb.POST, "stocks-service", "stocks/request", order, Map.class).block();
        logger.info("Stock req result {}", result.toString());

        Map<String, Object> processedOrder = new HashMap<>();
        processedOrder.put("order-id", order.orderId);
        processedOrder.put("approved", result.get("approved"));
        processedOrder.put("message", result.get("message"));
        
        logger.info("Sending result to processed-orders binding");
        dapr.invokeBinding("processed-orders", processedOrder).block();
        
        logger.info("Order successfully processed");
        return Response.noContent().build();
    }

}