package com.ayur.test.sec12;

import com.ayur.models.sec12.AccountBalance;
import com.ayur.models.sec12.BalanceCheckRequest;
import com.ayur.test.common.ResponseObserver;
import com.ayur.test.sec12.interceptors.DeadlineInterceptor;
import io.grpc.ClientInterceptor;
import io.grpc.Deadline;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Lec03DeadlineTest extends AbstractInterceptorTest {

    @Override
    protected List<ClientInterceptor> getClientInterceptors() {
        return List.of(new DeadlineInterceptor(Duration.ofSeconds(2)));
    }

    @Test
    public void blockingDeadlineTest() {
        BalanceCheckRequest balanceCheckRequest = BalanceCheckRequest.newBuilder().setAccountNumber(1).build();
        AccountBalance accountBalance = bankStub.getAccountBalance(balanceCheckRequest);
    }

    @Test
    public void getBalanceTest() {
        BalanceCheckRequest balanceCheckRequest = BalanceCheckRequest.newBuilder().setAccountNumber(1).build();
        ResponseObserver<AccountBalance> responseObserver = ResponseObserver.create();
        this.asyncBankStub.getAccountBalance(balanceCheckRequest, responseObserver);
        responseObserver.await();
        Assertions.assertTrue(responseObserver.getItems().isEmpty());
        Assertions.assertEquals(Status.Code.DEADLINE_EXCEEDED, Status.fromThrowable(responseObserver.getThrowable()).getCode());
    }
}
