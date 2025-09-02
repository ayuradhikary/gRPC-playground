package org.ayur.sec13;

import com.ayur.models.sec13.*;
import com.google.common.util.concurrent.Uninterruptibles;
import io.grpc.Context;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.ayur.sec13.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class BankService extends BankServiceGrpc.BankServiceImplBase {

    private static final Logger log = LoggerFactory.getLogger(BankService.class);

    @Override
    public void getAccountBalance(BalanceCheckRequest request, StreamObserver<AccountBalance> responseObserver) {
        int accountNumber = request.getAccountNumber();
        var balance = AccountRepository.getBalance(accountNumber);
        AccountBalance accountBalance = AccountBalance.newBuilder().setAccountNumber(accountNumber).setBalance(balance).build();
        Uninterruptibles.sleepUninterruptibly(3, TimeUnit.SECONDS);
        responseObserver.onNext(accountBalance);
        responseObserver.onCompleted();
    }

    @Override
    public void withdraw(WithdrawRequest request, StreamObserver<Money> responseObserver) {
        /* we should be doing input validation but since we are only considering happy path scenario as of now that's why validations are skipped. */
        /* assumption: account # 1 - 10 & withdraw amount is multiple of $10. */
        int accountNumber = request.getAccountNumber();
        int requestedAmount = request.getAmount();
        int accountBalance = AccountRepository.getBalance(accountNumber);

        if(requestedAmount > accountBalance) {
            responseObserver.onError(Status.FAILED_PRECONDITION.asRuntimeException());
            return;
        }

        for(int i = 0; i < (requestedAmount/10) && !Context.current().isCancelled(); i++) {
            Money money = Money.newBuilder().setAmount(10).build();
            responseObserver.onNext(money);
            log.info("money sent {}", money);
            AccountRepository.deductAmount(accountNumber, 10);
            Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);
        }
        log.info("streaming completed");
        responseObserver.onCompleted();
    }
}
