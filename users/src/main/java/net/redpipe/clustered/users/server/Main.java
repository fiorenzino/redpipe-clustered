package net.redpipe.clustered.users.server;

import io.vertx.rxjava.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;
import net.redpipe.clustered.users.service.UsersService;
import net.redpipe.clustered.users.util.ServiceUtils;
import net.redpipe.engine.core.AppGlobals;
import net.redpipe.engine.core.Server;

public class Main {

    public static void main(String[] args) {
        new Server()
                .configFile("conf/users.json")
                .start(UsersService.class)
                .subscribe(v -> onStart(),
                        x -> x.printStackTrace());
    }

    private static void onStart() {
        AppGlobals globals = AppGlobals.get();
        ServiceDiscovery serviceDiscovery = ServiceDiscovery
                .create(AppGlobals.get().getVertx(), new ServiceDiscoveryOptions().setBackendConfiguration(globals.getConfig()));
        System.out.println("serviceDiscovery: " + serviceDiscovery);
        globals.setGlobal("serviceDiscovery", serviceDiscovery);
        ServiceUtils.publishHttpEndpoint("users");
        System.err.println("Deploy is completed");
    }

}