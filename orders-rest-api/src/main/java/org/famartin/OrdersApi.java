package org.famartin;

import java.util.UUID;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.dapr.client.DaprClient;
import io.dapr.client.domain.Verb;

@Path("/")
public class OrdersApi {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Inject
    DaprClient dapr;

    @Path("/create-order")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response enqueueOrder(@Valid Order order) {
        order.orderId = UUID.randomUUID().toString();

        dapr.invokeBinding("orders", order).block();

        return Response.ok(order).build();
    }

    @Path("/add-stock")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addStock(@Valid Order stock) {
        Object result = dapr.invokeService(Verb.POST, "stocks-service", "stocks/add", stock, Object.class).block();
        return Response.ok(result).build();
    }

}