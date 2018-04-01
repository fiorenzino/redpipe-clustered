package net.redpipe.clustered.apigateway.server;

import io.vertx.rxjava.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;
import net.redpipe.clustered.apigateway.service.RegistryService;
import net.redpipe.engine.core.AppGlobals;
import net.redpipe.engine.core.Server;

public class Main {
    public static void main(String[] args) {
        new Server()
                .configFile("conf/api-gateway.json")
                .start(RegistryService.class)
                .subscribe(v -> onStart(),
                        x -> x.printStackTrace());
    }

    private static void onStart() {
        AppGlobals globals = AppGlobals.get();
        ServiceDiscovery serviceDiscovery = ServiceDiscovery
                .create(AppGlobals.get().getVertx(), new ServiceDiscoveryOptions().setBackendConfiguration(globals.getConfig()));
        globals.setGlobal("serviceDiscovery", serviceDiscovery);
        System.err.println("Deploy is completed");
    }
}
