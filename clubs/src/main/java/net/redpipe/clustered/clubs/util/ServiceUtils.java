package net.redpipe.clustered.clubs.util;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.rxjava.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.types.HttpEndpoint;
import net.redpipe.engine.core.AppGlobals;

import java.util.List;

/**
 * Created by fiorenzo on 24/03/17.
 */
public class ServiceUtils {

    private static final Logger logger = LoggerFactory.getLogger(ServiceUtils.class);

    public static Future<Void> publishHttpEndpoint(String name) {
        String host = AppGlobals.get().getConfig().getString("http_address", "0.0.0.0");
        int port = AppGlobals.get().getConfig().getInteger("http_port", 9000);
        ServiceDiscovery discovery = (ServiceDiscovery) AppGlobals.get().getGlobal("serviceDiscovery");
        logger.info("..service publishing: " + name + ",host:" + host + ", port: " + port + ", root: /");
        Record record = HttpEndpoint.createRecord(name, host, port, "/", new JsonObject().put("api.name", name));
        return publish(record, discovery);
    }


    /**
     * Publish a service with record.
     *
     * @param record service record
     * @return async end
     */
    private static Future<Void> publish(Record record, ServiceDiscovery discovery) {
        Future<Void> future = Future.future();
        if (discovery == null) {
            future.fail("Cannot repository_create serviceDiscovery service");
        } else {
            // publish the service
            discovery.publish(record, ar ->
            {
                if (ar.succeeded()) {
                    logger.info("Service <" + ar.result().getName() + "> published");
                    future.complete();
                } else {
                    future.fail(ar.cause());
                }
            });
        }

        return future;
    }


    public static Future<List<Record>> getAllEndpoints(ServiceDiscovery serviceDiscovery) {
        logger.info("ALL ENDPOINTS");
        Future<List<Record>> future = Future.future();
        serviceDiscovery.getRecords(record -> record.getType().equals(HttpEndpoint.TYPE),
                future.completer());
        return future;
    }

}
