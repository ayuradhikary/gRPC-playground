package com.ayur.test.sec11;

import com.ayur.models.sec11.AccountBalance;
import com.ayur.models.sec11.BalanceCheckRequest;
import com.ayur.test.common.ResponseObserver;
import io.grpc.Deadline;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class Lec01UnaryDeadlineTest extends AbstractTest {

    private final Logger logger = LoggerFactory.getLogger(Lec01UnaryDeadlineTest.class);

    @Test
    public void blockingDeadlineTest() {

        StatusRuntimeException ex = Assertions.assertThrows(StatusRuntimeException.class, () -> {
            BalanceCheckRequest balanceCheckRequest = BalanceCheckRequest.newBuilder().setAccountNumber(1).build();
            AccountBalance accountBalance = bankStub.withDeadline(Deadline.after(2, TimeUnit.SECONDS)).getAccountBalance(balanceCheckRequest);

        });

        Assertions.assertEquals(Status.Code.DEADLINE_EXCEEDED, ex.getStatus().getCode());
    }

    @Test
    public void getBalanceTest() {
        BalanceCheckRequest balanceCheckRequest = BalanceCheckRequest.newBuilder().setAccountNumber(1).build();
        ResponseObserver<AccountBalance> responseObserver = ResponseObserver.create();
        this.asyncBankStub.withDeadline(Deadline.after(2, TimeUnit.SECONDS)).getAccountBalance(balanceCheckRequest, responseObserver);
        responseObserver.await();
        Assertions.assertTrue(responseObserver.getItems().isEmpty());
        Assertions.assertEquals(Status.Code.DEADLINE_EXCEEDED, Status.fromThrowable(responseObserver.getThrowable()).getCode());
    }
}
