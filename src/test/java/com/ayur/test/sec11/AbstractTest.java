package com.ayur.test.sec11;

import com.ayur.models.sec11.BankServiceGrpc;
import com.ayur.test.common.AbstractChannelTest;
import org.ayur.common.GrpcServer;
import org.ayur.sec11.DeadlineBankService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public class AbstractTest extends AbstractChannelTest {

    private final GrpcServer grpcServer = GrpcServer.create(new DeadlineBankService());
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
