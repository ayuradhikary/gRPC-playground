package com.ayur.test.sec06;

import com.ayur.models.sec06.AccountBalance;
import com.ayur.models.sec06.AllAccountsResponse;
import com.ayur.models.sec06.BalanceCheckRequest;
import com.google.protobuf.Empty;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lec01UnaryBlockingClientTest extends AbstractTest {

    private static final Logger logger = LoggerFactory.getLogger(Lec01UnaryBlockingClientTest.class);

    @Test
    public void getBalanceTest() {
        BalanceCheckRequest request = BalanceCheckRequest.newBuilder().setAccountNumber(1).build();
        AccountBalance accountBalance = this.bankStub.getAccountBalance(request);
        logger.info("unary balance recieved: {}", accountBalance);
        Assertions.assertEquals(100, accountBalance.getBalance());
    }

    @Test
    public void getAllAccountsTest() {
        AllAccountsResponse response =  this.bankStub.getAllAccounts(Empty.getDefaultInstance());
        logger.info("unary all accounts fetched:{}", response);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(10, response.getAccountsCount());
    }

}
