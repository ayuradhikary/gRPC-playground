package com.ayur.test.sec11;

import com.ayur.models.sec11.BankServiceGrpc;
import com.ayur.models.sec11.Money;
import com.ayur.models.sec11.WithdrawRequest;
import com.ayur.test.common.AbstractChannelTest;
import com.google.common.util.concurrent.Uninterruptibles;
import io.grpc.Deadline;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.ayur.common.GrpcServer;
import org.ayur.sec11.DeadlineBankService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class Lec03WitForReadyTest extends AbstractChannelTest {

    private static final Logger log = LoggerFactory.getLogger(Lec03WitForReadyTest.class);

    private final GrpcServer grpcServer = GrpcServer.create(new DeadlineBankService());
    private BankServiceGrpc.BankServiceBlockingStub bankStub;

    @BeforeAll
    public void setup() {
        Runnable runnable = () -> {
            Uninterruptibles.sleepUninterruptibly(3, TimeUnit.SECONDS);
            this.grpcServer.start();
        };
        Thread thread = new Thread(runnable);
        thread.start();
        this.bankStub = BankServiceGrpc.newBlockingStub(channel);
    }

    @Test
    public void blockingDeadlineTest() {
        log.info("sending the request");
        WithdrawRequest withdrawRequest = WithdrawRequest.newBuilder().setAccountNumber(1).setAmount(50).build();
        Iterator<Money> iterator = this.bankStub.withWaitForReady().withDeadline(Deadline.after(15, TimeUnit.SECONDS)).withdraw(withdrawRequest);
        while (iterator.hasNext()) {
            log.info("{}", iterator.next());
        }
    }

    @AfterAll
    public void stop() {
        this.grpcServer.stop();
    }

}
