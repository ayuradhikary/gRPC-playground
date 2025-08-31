package com.ayur.test.sec06;

import com.ayur.models.sec06.AccountBalance;
import com.ayur.models.sec06.DepositRequest;
import com.ayur.models.sec06.Money;
import com.ayur.test.common.ResponseObserver;
import com.google.common.util.concurrent.Uninterruptibles;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class Lec04ClientStreamingTest extends AbstractTest {

    private static final Logger log = LoggerFactory.getLogger(Lec04ClientStreamingTest.class);

    @Test
    public void depositTest() {
        ResponseObserver<AccountBalance> responseObserver = ResponseObserver.create();
        StreamObserver<DepositRequest> requestObserver = this.asyncStub.deposit(responseObserver);

        //initial message - account number
        requestObserver.onNext(DepositRequest.newBuilder().setAccountNumber(5).build());

//        Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);
//        requestObserver.onError(new RuntimeException()); //cancels the request from the client

        //sending stream money
        IntStream.range(0, 10)
                .mapToObj(i -> Money.newBuilder().setAmount(10).build())
                .map(m -> DepositRequest.newBuilder().setMoney(m).build())
                .forEach(requestObserver::onNext);

//        notifying the server that we are done
        requestObserver.onCompleted();


        //at this point our response observer receive a response.
        responseObserver.await();
        Assertions.assertEquals(1, responseObserver.getItems().size());
        Assertions.assertEquals(200, responseObserver.getItems().get(0).getBalance());
        Assertions.assertNull(responseObserver.getThrowable());
    }

}
