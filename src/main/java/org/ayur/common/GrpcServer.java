package org.ayur.common;

import io.grpc.*;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
import org.ayur.sec12.interceptors.GzipResponseInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class GrpcServer {

    private static final Logger logger = LoggerFactory.getLogger(GrpcServer.class);
    private final Server server;

    private GrpcServer(Server server) {
        this.server = server;
    }

    public static GrpcServer create(BindableService... services) {
        return create(6565, services);
    }

    public static GrpcServer create(int port, BindableService... services) {
        return create(port, builder -> {
            Arrays.asList(services).forEach(builder::addService);
        });
    }

    public static GrpcServer   create(int port, Consumer<NettyServerBuilder> consumer) {
        var builder = ServerBuilder.forPort(port);
        consumer.accept((NettyServerBuilder)builder);
        return new GrpcServer(builder.build());
    }

    public GrpcServer start() {
        List<String> services = server.getServices()
                .stream()
                .map(ServerServiceDefinition::getServiceDescriptor)
                .map(ServiceDescriptor::getName)
                .toList();
        try {
            server.start();
            logger.info("server started. listening on port {}. services: {}", server.getPort(), services);
            return this;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void await() {
        try {
            server.awaitTermination();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() {
        server.shutdownNow();
        logger.info("server stoped.");
    }

}
