package com.ayur.test.sec09;

import com.ayur.models.sec09.AccountBalance;
import com.ayur.models.sec09.BalanceCheckRequest;
import com.ayur.models.sec09.Money;
import com.ayur.models.sec09.WithdrawRequest;
import com.ayur.test.common.ResponseObserver;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.stream.Stream;

public class Lec02ServerStreamingInputValidationTest extends AbstractTest {

    private final Logger logger = LoggerFactory.getLogger(Lec02ServerStreamingInputValidationTest.class);

    @ParameterizedTest
    @MethodSource("testData")
    public void blockingInputValidationTest(WithdrawRequest request, Status.Code code) {
        StatusRuntimeException ex =  Assertions.assertThrows(StatusRuntimeException.class, () -> {
            boolean response = this.bankStub.withdraw(request).hasNext();
        });
        Assertions.assertEquals(code, ex.getStatus().getCode());
    }

    @ParameterizedTest
    @MethodSource("testData")
    public void asyncInputValidationTest(WithdrawRequest request, Status.Code code) {
        ResponseObserver<Money> observer = ResponseObserver.create();
        this.asyncBankStub.withdraw(request, observer);
        observer.await();
        Assertions.assertTrue(observer.getItems().isEmpty());
        Assertions.assertNotNull(observer.getThrowable());
        Assertions.assertEquals(code, ((StatusRuntimeException) observer.getThrowable()).getStatus().getCode());
    }

    private Stream<Arguments> testData() {
        return Stream.of(
                Arguments.of(WithdrawRequest.newBuilder().setAccountNumber(11).setAmount(10).build(), Status.Code.INVALID_ARGUMENT),
                Arguments.of(WithdrawRequest.newBuilder().setAccountNumber(1).setAmount(17).build(), Status.Code.INVALID_ARGUMENT),
                Arguments.of(WithdrawRequest.newBuilder().setAccountNumber(1).setAmount(120).build(), Status.Code.FAILED_PRECONDITION)
        );
    }

}
