package net.redpipe.clustered.apigateway.util;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.types.HttpEndpoint;

import java.util.List;
import java.util.Set;

/**
 * Created by fiorenzo on 24/03/17.
 */
public class ServiceUtils {

    private static final Logger logger = LoggerFactory.getLogger(ServiceUtils.class);

    public static Future<Void> publishHttpEndpoint(String name, String host, int port, String root,
                                                   ServiceDiscovery discovery, Set<Record> registeredRecords) {
        logger.info("..service publishing: " + name + ",host:" + host + ", port: " + port + ", root:" + root);
        Record record = HttpEndpoint.createRecord(name, host, port, root, new JsonObject().put("api.name", name)
        );
        return publish(record, discovery, registeredRecords);
    }

    public static Future<Void> publishApiGateway(String host, int port, ServiceDiscovery discovery,
                                                 Set<Record> registeredRecords) {
        Record record = HttpEndpoint.createRecord("api-gateway", true, host, port, "/", null)
                .setType("api-gateway");
        return publish(record, discovery, registeredRecords);
    }
   

    /**
     * Publish a service with record.
     *
     * @param record service record
     * @return async end
     */
    private static Future<Void> publish(Record record, ServiceDiscovery discovery, Set<Record> registeredRecords) {
        Future<Void> future = Future.future();
        if (discovery == null) {
            future.fail("Cannot repository_create serviceDiscovery service");
        } else {
            // publish the service
            discovery.publish(record, ar ->
            {
                if (ar.succeeded()) {
                    registeredRecords.add(record);
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
