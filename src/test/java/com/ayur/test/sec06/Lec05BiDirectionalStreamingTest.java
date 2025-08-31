package com.ayur.test.sec06;

import com.ayur.models.sec06.TransferRequest;
import com.ayur.models.sec06.TransferResponse;
import com.ayur.models.sec06.TransferStatus;
import com.ayur.test.common.ResponseObserver;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class Lec05BiDirectionalStreamingTest extends AbstractTest {

    private static final Logger log = LoggerFactory.getLogger(Lec05BiDirectionalStreamingTest.class);

    @Test
    public void transferTest() {

        ResponseObserver<TransferResponse> responseObserver = ResponseObserver.create();
        StreamObserver<TransferRequest> requestObserver = this.transferAsyncStub.transfer(responseObserver);

        List<TransferRequest> requests = List.of(
                TransferRequest.newBuilder().setAmount(10).setFromAccount(6).setToAccount(6).build(),
                TransferRequest.newBuilder().setAmount(110).setFromAccount(6).setToAccount(7).build(),
                TransferRequest.newBuilder().setAmount(10).setFromAccount(6).setToAccount(7).build(),
                TransferRequest.newBuilder().setAmount(10).setFromAccount(7).setToAccount(6).build()
        );

        requests.forEach(requestObserver::onNext);
        requestObserver.onCompleted();
        responseObserver.await();

        Assertions.assertEquals(4, responseObserver.getItems().size());
        this.validate(responseObserver.getItems().get(0), TransferStatus.REJECTED, 100, 100);
        this.validate(responseObserver.getItems().get(1), TransferStatus.REJECTED, 100, 100);
        this.validate(responseObserver.getItems().get(2), TransferStatus.COMPLETED, 90, 110);
        this.validate(responseObserver.getItems().get(3), TransferStatus.COMPLETED, 100, 100);
    }

    private void validate(TransferResponse response, TransferStatus expectedStaus, int expectedFromAccountBalance, int expectedToAccountBalance) {
        Assertions.assertEquals(expectedStaus, response.getStatus());
        Assertions.assertEquals(expectedFromAccountBalance, response.getFromAccount().getBalance());
        Assertions.assertEquals(expectedToAccountBalance, response.getToAccount().getBalance());
    }


}
