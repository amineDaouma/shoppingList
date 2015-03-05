package io.pvardanega.shoppinglist;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("healthcheck")
public class HealthCheck {

    @GET
    public String doesItWorks() {
        return "It works!";
    }
}
