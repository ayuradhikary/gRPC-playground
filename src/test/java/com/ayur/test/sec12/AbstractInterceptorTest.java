package com.ayur.test.sec12;

import com.ayur.models.sec12.BankServiceGrpc;
import io.grpc.ClientInterceptor;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.ayur.common.GrpcServer;
import org.ayur.sec12.BankService;
import org.ayur.sec12.interceptors.GzipResponseInterceptor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractInterceptorTest {

    private GrpcServer grpcServer;
    protected ManagedChannel channel;
    protected BankServiceGrpc.BankServiceBlockingStub bankStub;
    protected BankServiceGrpc.BankServiceStub asyncBankStub;

    protected abstract List<ClientInterceptor> getClientInterceptors();

    protected GrpcServer createServer() {
        return GrpcServer.create(6565, builder -> {
            builder.addService(new BankService())
                    .intercept(new GzipResponseInterceptor());
        });
    }

    @BeforeAll
    public void setup() {
        this.grpcServer = createServer();
        this.grpcServer.start();
        this.channel = ManagedChannelBuilder.forAddress("localhost", 6565).usePlaintext().intercept(getClientInterceptors()).build();
        this.asyncBankStub = BankServiceGrpc.newStub(channel);
        this.bankStub = BankServiceGrpc.newBlockingStub(channel);
    }

    @AfterAll
    public void stop() {
        this.grpcServer.stop();
        channel.shutdownNow();
    }
}
