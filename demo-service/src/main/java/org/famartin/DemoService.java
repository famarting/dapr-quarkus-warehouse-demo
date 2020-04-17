package org.famartin;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.dapr.client.DaprClient;
import io.vertx.core.json.JsonObject;

@Path("/processed-orders")
public class DemoService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Inject
    DaprClient dapr;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response processOrder(JsonObject order) {
        logger.info(order.encode());
        return Response.noContent().build();
    }

}