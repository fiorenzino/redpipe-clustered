package net.redpipe.clustered.users.service;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("/")
@ApplicationScoped
public class UsersService {

    public UsersService() {
        System.out.println("init UsersService");
    }

    @GET
    public Response get() {
        return Response.ok("Hello Users").build();
    }


    @GET
    @Path("/{id}")
    public Response single(@PathParam("id") String id) {
        return Response.ok("users id: " + id
        ).build();
    }


}
