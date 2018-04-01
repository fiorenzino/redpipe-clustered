package net.redpipe.clustered.clubs.server;

import net.redpipe.clustered.clubs.service.ClubsService;
import net.redpipe.engine.core.Server;

public class Main {
    public static void main(String[] args) {
        new Server()
                .configFile("conf/clubs.json")
                .start(ClubsService.class)
                .subscribe(v -> System.err.println("Deploy is completed"),
                        x -> x.printStackTrace());
    }
}
