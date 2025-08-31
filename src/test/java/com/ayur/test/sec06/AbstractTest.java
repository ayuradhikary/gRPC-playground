package com.ayur.test.sec06;

import com.ayur.models.sec06.BankServiceGrpc;
import com.ayur.models.sec06.TransferServiceGrpc;
import com.ayur.test.common.AbstractChannelTest;
import org.ayur.common.GrpcServer;
import org.ayur.sec06.BankService;
import org.ayur.sec06.TransferService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public class AbstractTest extends AbstractChannelTest {

    private final GrpcServer grpcServer = GrpcServer.create(new BankService(), new TransferService());
    protected BankServiceGrpc.BankServiceBlockingStub bankStub;
    protected BankServiceGrpc.BankServiceStub asyncBankStub;
    protected TransferServiceGrpc.TransferServiceStub transferAsyncStub;

    @BeforeAll
    public void setup() {
        this.grpcServer.start();
        this.asyncBankStub = BankServiceGrpc.newStub(channel);
        this.bankStub = BankServiceGrpc.newBlockingStub(channel);
        this.transferAsyncStub = TransferServiceGrpc.newStub(channel);
    }

    @AfterAll
    public void stop() {
        this.grpcServer.stop();
    }
}
