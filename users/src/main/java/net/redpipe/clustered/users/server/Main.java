package net.redpipe.clustered.users.server;

import net.redpipe.clustered.users.service.UsersService;
import net.redpipe.engine.core.Server;

public class Main {
    public static void main(String[] args) {
        new Server()
                .configFile("conf/users.json")
                .start(UsersService.class)
                .subscribe(v -> System.err.println("Deploy is completed"),
                        x -> x.printStackTrace());
    }
}
