package com.ayur.test.sec06;

import com.ayur.models.sec06.Money;
import com.ayur.models.sec06.WithdrawRequest;
import com.ayur.test.common.ResponseObserver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

public class Lec03ServerStreamingClientTest extends AbstractTest {

    private static final Logger log = LoggerFactory.getLogger(Lec03ServerStreamingClientTest.class);

    @Test
    public void blockingClientWithdrawTest() {
        WithdrawRequest request = WithdrawRequest.newBuilder().setAccountNumber(2).setAmount(20).build();
        Iterator<Money> iterator = this.bankStub.withdraw(request);
        int count = 0;
        while(iterator.hasNext()) {
            log.info("received money: {}", iterator.next());
            count++;
        }
        Assertions.assertEquals(2, count);
    }

    @Test
    public void asyncClientWithdrawTest() {
        WithdrawRequest request = WithdrawRequest.newBuilder().setAccountNumber(2).setAmount(20).build();
        ResponseObserver<Money> responseObserver = ResponseObserver.create();
        this.asyncBankStub.withdraw(request, responseObserver);
        responseObserver.await();
        Assertions.assertEquals(2, responseObserver.getItems().size());
        Assertions.assertEquals(10, responseObserver.getItems().get(0).getAmount());
        Assertions.assertNull(responseObserver.getThrowable());
    }

}
