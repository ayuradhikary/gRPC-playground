package com.ayur.test.sec13;

import com.ayur.models.sec13.BalanceCheckRequest;
import com.ayur.models.sec13.BankServiceGrpc;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GrpcSSLTest extends AbstractTest{

    private static final Logger logger = LoggerFactory.getLogger(GrpcSSLTest.class);

    @Test
    public void clientWithSSLTest() {
//        var channel = ManagedChannelBuilder.forAddress("localhost", 6565).build(); ideally in real env we do this
        var channel = NettyChannelBuilder.forAddress("localhost", 6565).sslContext(clientSslContext()).build();
        var stub = BankServiceGrpc.newBlockingStub(channel);
        var request = BalanceCheckRequest.newBuilder().setAccountNumber(1).build();
        var response = stub.getAccountBalance(request);
        logger.info("{}", response);
        channel.shutdownNow();
    }

}
