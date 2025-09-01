package com.ayur.test.sec11;

import com.ayur.models.sec11.AccountBalance;
import com.ayur.models.sec11.BalanceCheckRequest;
import com.ayur.models.sec11.BankServiceGrpc;
import com.ayur.test.common.AbstractChannelTest;
import org.ayur.common.GrpcServer;
import org.ayur.sec11.DeadlineBankService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lec04LazyChannelDemoTest extends AbstractChannelTest {

    private static final Logger log = LoggerFactory.getLogger(Lec04LazyChannelDemoTest.class);
    private final GrpcServer grpcServer = GrpcServer.create(new DeadlineBankService());
    private BankServiceGrpc.BankServiceBlockingStub blockingStub;

    @BeforeAll
    public void setup() {
        this.grpcServer.start();
        this.blockingStub = BankServiceGrpc.newBlockingStub(channel);
    }

    @Test
    public void lazyChannelDemo() {
        BalanceCheckRequest balanceCheckRequest = BalanceCheckRequest.newBuilder().setAccountNumber(1).build();
        AccountBalance response = this.blockingStub.getAccountBalance(balanceCheckRequest);
        log.info("{}", response);
    }

    @AfterAll
    public void stop() {
        this.grpcServer.stop();
    }
}
