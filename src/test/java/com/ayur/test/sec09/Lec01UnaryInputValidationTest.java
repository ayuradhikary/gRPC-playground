package com.ayur.test.sec09;

import com.ayur.models.sec09.AccountBalance;
import com.ayur.models.sec09.BalanceCheckRequest;
import com.ayur.test.common.ResponseObserver;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lec01UnaryInputValidationTest extends AbstractTest {

    private static Logger logger = LoggerFactory.getLogger(Lec01UnaryInputValidationTest.class);

    @Test
    public void blockingInputValidationTest() {
        StatusRuntimeException ex =  Assertions.assertThrows(StatusRuntimeException.class, () -> {
            BalanceCheckRequest request = BalanceCheckRequest.newBuilder().setAccountNumber(11).build();
            AccountBalance response = this.bankStub.getAccountBalance(request);
        });
        Assertions.assertEquals(Status.Code.INVALID_ARGUMENT, ex.getStatus().getCode());
    }

    @Test
    public void asyncInputValidationTest() {
        BalanceCheckRequest request = BalanceCheckRequest.newBuilder().setAccountNumber(11).build();
        ResponseObserver<AccountBalance> observer = ResponseObserver.create();
        this.asyncBankStub.getAccountBalance(request, observer);
        observer.await();
        Assertions.assertTrue(observer.getItems().isEmpty());
        Assertions.assertNotNull(observer.getThrowable());
        Assertions.assertEquals(Status.Code.INVALID_ARGUMENT, ((StatusRuntimeException) observer.getThrowable()).getStatus().getCode());
    }
}
