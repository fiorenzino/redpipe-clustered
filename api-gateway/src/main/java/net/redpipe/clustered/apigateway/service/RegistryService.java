package net.redpipe.clustered.apigateway.service;

import io.vertx.core.Future;
import io.vertx.rx.java.ObservableFuture;
import io.vertx.rx.java.RxHelper;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.servicediscovery.ServiceDiscovery;
import net.redpipe.clustered.apigateway.util.ServiceUtils;
import rx.Single;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/registry")
public class RegistryService {


    @Inject
    Vertx vertx;

    @Inject
    ServiceDiscovery serviceDiscovery;

    public RegistryService() {
        System.out.println("init RegistryService");
    }


    @GET
    public Single<String> get() {
        ObservableFuture<String> resultHandler = RxHelper.observableFuture();
        ServiceUtils.getAllEndpoints(serviceDiscovery).setHandler(ar ->
        {
            if (ar.succeeded()) {
                StringBuffer sb = new StringBuffer();
                ar.result().stream()
                        .forEach(record -> {
                            sb.append(record.getName() + ", " + record.getLocation() + ", " + record.getMetadata());
                        });

                resultHandler.toHandler().handle(Future.succeededFuture(sb.toString()));
            } else {
                resultHandler.toHandler().handle(Future.failedFuture("no result"));
            }
        });
        return resultHandler.toSingle();
    }


}
