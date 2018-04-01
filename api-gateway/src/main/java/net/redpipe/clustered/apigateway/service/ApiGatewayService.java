package net.redpipe.clustered.apigateway.service;

import io.vertx.core.Future;
import io.vertx.rx.java.ObservableFuture;
import io.vertx.rx.java.RxHelper;
import io.vertx.rxjava.core.buffer.Buffer;
import io.vertx.rxjava.ext.web.client.HttpRequest;
import io.vertx.rxjava.ext.web.client.HttpResponse;
import io.vertx.rxjava.ext.web.client.WebClient;
import io.vertx.rxjava.servicediscovery.ServiceDiscovery;
import io.vertx.rxjava.servicediscovery.types.HttpEndpoint;
import net.redpipe.engine.core.AppGlobals;
import rx.Single;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/api/")
@ApplicationScoped
public class ApiGatewayService {


    @Inject
    ServiceDiscovery serviceDiscovery;


    @GET
    public Response get() {
        return Response.ok("Hello ApiGatewayService").build();
    }

    public ApiGatewayService() {
        System.out.println("init ApiGatewayService");
    }

    @DELETE
    @Path("/{path:.*}")
    public Single<String> delete(@PathParam("path") String path,
                                 @Context UriInfo ui) {
        return handle(path, ui);
    }

    @PUT
    @Path("/{path:.*}")
    public Single<String> path(@PathParam("path") String path,
                               @Context UriInfo ui) {
        return handle(path, ui);
    }

    @POST
    @Path("/{path:.*}")
    public Single<String> post(@PathParam("path") String path,
                               @Context UriInfo ui) {
        return handle(path, ui);
    }

    @GET
    @Path("/{path:.*}")
    public Single<String> get(@PathParam("path") String path,
                              @Context UriInfo ui) {
        return handle(path, ui);
    }

    private Single<String> handle(String path, UriInfo ui) {
        String[] paths = path.split("/");
        String apiname;
        if (paths != null && paths.length > 0) {
            apiname = paths[0];
            path = path.replace(apiname, "");
        } else {
            apiname = path;
        }
        String finalPath = path.startsWith("/") ? path : "/" + path;
        System.out.println("apiname:" + apiname);
        System.out.println("path:" + path);
        System.out.println("queryparams");
        ObservableFuture<String> resultHandler = RxHelper.observableFuture();

        MultivaluedMap<String, String> queryParams = ui.getQueryParameters();
        queryParams.forEach(
                (k, v) -> {
                    System.out.println("k: " + k + ", v: " + v);
                }
        );
        ServiceDiscovery serviceDiscovery = (ServiceDiscovery) AppGlobals.get().getGlobal("serviceDiscovery");
        Single<WebClient> quotes = HttpEndpoint.rxGetWebClient(serviceDiscovery, rec -> rec.getName().equals(apiname));
        quotes.subscribe((client) -> {
            if (client == null) {
                resultHandler.toHandler().handle(Future.failedFuture("no result"));
            } else {
                doDispatch(client, finalPath, ui, resultHandler);
            }
        });
        return resultHandler.toSingle();
    }


    private void doDispatch(WebClient client, String path, UriInfo ui, ObservableFuture<String> resultHandler) {
        HttpRequest<Buffer> getReq = client.get(path);
        MultivaluedMap<String, String> queryParams = ui.getQueryParameters();
        queryParams.forEach(
                (k, v) -> {
                    getReq.addQueryParam(k, v.toString());
                    System.out.println("k: " + k + ", v: " + v);
                }
        );
        Single<HttpResponse<Buffer>> responseHandler =
                getReq.rxSend();
        responseHandler.map(response -> {
            resultHandler.toHandler().handle(Future.succeededFuture(response.body().toString()));
            return "";
        }).subscribe();
    }


}
