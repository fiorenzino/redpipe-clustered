package net.redpipe.clustered.clubs.server;

import io.vertx.rxjava.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;
import net.redpipe.clustered.clubs.service.ClubsService;
import net.redpipe.clustered.clubs.util.ServiceUtils;
import net.redpipe.engine.core.AppGlobals;
import net.redpipe.engine.core.Server;

public class Main {


    public static void main(String[] args) {
        new Server()
                .configFile("conf/clubs.json")
                .start(ClubsService.class)
                .subscribe(v -> onStart(),
                        x -> x.printStackTrace());
    }

    private static void onStart() {
        AppGlobals globals = AppGlobals.get();
        ServiceDiscovery serviceDiscovery = ServiceDiscovery
                .create(AppGlobals.get().getVertx(), new ServiceDiscoveryOptions().setBackendConfiguration(globals.getConfig()));
        System.out.println("serviceDiscovery: " + serviceDiscovery);
        globals.setGlobal("serviceDiscovery", serviceDiscovery);
        ServiceUtils.publishHttpEndpoint("clubs");
        System.err.println("Deploy is completed");
    }

}
