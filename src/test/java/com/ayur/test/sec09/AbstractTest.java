package com.ayur.test.sec09;

import com.ayur.models.sec09.BankServiceGrpc;
import com.ayur.test.common.AbstractChannelTest;
import org.ayur.common.GrpcServer;
import org.ayur.sec09.BankService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public class AbstractTest extends AbstractChannelTest {

    private final GrpcServer grpcServer = GrpcServer.create(new BankService());
    protected BankServiceGrpc.BankServiceBlockingStub bankStub;
    protected BankServiceGrpc.BankServiceStub asyncBankStub;

    @BeforeAll
    public void setup() {
        this.grpcServer.start();
        this.asyncBankStub = BankServiceGrpc.newStub(channel);
        this.bankStub = BankServiceGrpc.newBlockingStub(channel);
    }

    @AfterAll
    public void stop() {
        this.grpcServer.stop();
    }
}
