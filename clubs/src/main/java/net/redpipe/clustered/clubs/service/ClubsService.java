package net.redpipe.clustered.clubs.service;

import javax.annotation.PostConstruct;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/")
public class ClubsService {

    @PostConstruct
    private void init() {
        System.out.println("init ClubsService");
    }

    @GET
    public Response get() {
        return Response.ok("Hello World").build();
    }


}
