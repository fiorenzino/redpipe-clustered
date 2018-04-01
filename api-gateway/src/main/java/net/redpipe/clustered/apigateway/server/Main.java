package net.redpipe.clustered.apigateway.server;

import io.vertx.rxjava.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;
import net.redpipe.clustered.apigateway.service.ApiGatewayService;
import net.redpipe.clustered.apigateway.service.RegistryService;
import net.redpipe.clustered.apigateway.util.ServiceUtils;
import net.redpipe.engine.core.AppGlobals;
import net.redpipe.engine.core.Server;

public class Main {
    public static void main(String[] args) {
        new Server()
                .configFile("conf/api-gateway.json")
                .start(RegistryService.class, ApiGatewayService.class)
                .subscribe(v -> onStart(),
                        x -> x.printStackTrace());
    }

    private static void onStart() {
        AppGlobals globals = AppGlobals.get();
        ServiceDiscovery serviceDiscovery = ServiceDiscovery
                .create(AppGlobals.get().getVertx(), new ServiceDiscoveryOptions().setBackendConfiguration(globals.getConfig()));
        System.out.println("serviceDiscovery: " + serviceDiscovery);
        globals.setGlobal("serviceDiscovery", serviceDiscovery);
        ServiceUtils.publishHttpEndpoint("api-gateway");
        ServiceUtils.publishHttpEndpoint("registry");
        System.err.println("Deploy is completed");
    }
}
