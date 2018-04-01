package net.redpipe.clustered.users.service;

import javax.annotation.PostConstruct;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/")
public class UsersService {

    @PostConstruct
    private void init() {
        System.out.println("init UsersService");
    }

    @GET
    public Response get() {
        return Response.ok("Hello World").build();
    }

   
}
