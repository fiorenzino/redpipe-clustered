package net.redpipe.clustered.apigateway.producer;

import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.core.eventbus.EventBus;
import io.vertx.rxjava.servicediscovery.ServiceDiscovery;
import net.redpipe.engine.core.AppGlobals;

import javax.enterprise.inject.Produces;

public class AppGlobalsProducer {

    AppGlobalsProducer() {

    }

    @Produces
    public EventBus bus() {
        return AppGlobals.get().getVertx().eventBus();
    }

    @Produces
    public Vertx vertx() {
        return AppGlobals.get().getVertx();
    }

    @Produces
    public ServiceDiscovery serviceDiscovery() {
        return (ServiceDiscovery) AppGlobals.get().getGlobal("serviceDiscovery");
    }

    @Produces
    public JsonObject config() {
        return AppGlobals.get().getConfig();
    }

}
