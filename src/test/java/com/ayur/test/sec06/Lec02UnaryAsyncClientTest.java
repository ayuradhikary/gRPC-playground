package com.ayur.test.sec06;

import com.ayur.models.sec06.AccountBalance;
import com.ayur.models.sec06.BalanceCheckRequest;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

public class Lec02UnaryAsyncClientTest extends AbstractTest {

    private static final Logger log = LoggerFactory.getLogger(Lec02UnaryAsyncClientTest.class);

    @Test
    public void getBalanceTest() throws InterruptedException {
        BalanceCheckRequest request = BalanceCheckRequest.newBuilder().setAccountNumber(1).build();
        CountDownLatch latch = new CountDownLatch(1);
        this.asyncStub.getAccountBalance(request, new StreamObserver<AccountBalance>() {
            @Override
            public void onNext(AccountBalance accountBalance) {
                log.info("async balance received. ");
                Assertions.assertEquals(100, accountBalance.getBalance());
                latch.countDown();
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {

            }
        });
        latch.await();
    }

}
