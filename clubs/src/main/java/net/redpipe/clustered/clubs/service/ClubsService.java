package net.redpipe.clustered.clubs.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("/")
public class ClubsService {

    public ClubsService() {
        System.out.println("init ClubsService");
    }

    @GET
    public Response get() {
        return Response.ok("Hello Clubs").build();
    }

    @GET
    @Path("/{id}")
    public Response single(@PathParam("id") String id) {
        return Response.ok("id:" + id).build();
    }


}
