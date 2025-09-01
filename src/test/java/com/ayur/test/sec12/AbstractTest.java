package com.ayur.test.sec12;

import com.ayur.models.sec12.BankServiceGrpc;
import com.ayur.test.common.AbstractChannelTest;
import org.ayur.common.GrpcServer;
import org.ayur.sec12.BankService;
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
