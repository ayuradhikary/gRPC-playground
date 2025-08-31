package com.ayur.test.sec06;

import com.ayur.models.sec06.AccountBalance;
import com.ayur.models.sec06.AllAccountsResponse;
import com.ayur.models.sec06.BalanceCheckRequest;
import com.ayur.test.common.ResponseObserver;
import com.google.protobuf.Empty;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lec02UnaryAsyncClientTest extends AbstractTest {

    private static final Logger log = LoggerFactory.getLogger(Lec02UnaryAsyncClientTest.class);

    @Test
    public void getBalanceTest() {
        BalanceCheckRequest request = BalanceCheckRequest.newBuilder().setAccountNumber(1).build();
        ResponseObserver<AccountBalance> responseObserver = ResponseObserver.create();
        this.asyncBankStub.getAccountBalance(request, responseObserver);
        responseObserver.await();
        Assertions.assertEquals(1, responseObserver.getItems().size());
        Assertions.assertEquals(100, responseObserver.getItems().get(0).getBalance());
        Assertions.assertNull(responseObserver.getThrowable());
    }

    @Test
    public void getAllAccountsTest() {
        ResponseObserver<AllAccountsResponse> responseObserver = ResponseObserver.create();
        this.asyncBankStub.getAllAccounts(Empty.getDefaultInstance(), responseObserver);
        responseObserver.await();
        Assertions.assertEquals(1, responseObserver.getItems().size());
        Assertions.assertEquals(10, responseObserver.getItems().get(0).getAccountsCount());
        Assertions.assertNull(responseObserver.getThrowable());
    }

}
