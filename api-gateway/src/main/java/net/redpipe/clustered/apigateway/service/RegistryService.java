package net.redpipe.clustered.apigateway.service;

import io.vertx.rxjava.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.types.HttpEndpoint;
import rx.Single;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.List;

@Path("/registry")
@ApplicationScoped
public class RegistryService {

    @Inject
    ServiceDiscovery serviceDiscovery;

    @PostConstruct
    private void init() {
        System.out.println("init RegistryService");
    }

    @GET
    public Single<List<Record>> get() {
        return allRecords();
    }


    private Single<List<Record>> allRecords() {
        return serviceDiscovery.rxGetRecords(record -> record.getType().equals(HttpEndpoint.TYPE));
    }
}
