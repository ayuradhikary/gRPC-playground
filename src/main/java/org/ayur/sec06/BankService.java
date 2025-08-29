package org.ayur.sec06;

import com.ayur.models.sec06.AccountBalance;
import com.ayur.models.sec06.BalanceCheckRequest;
import com.ayur.models.sec06.BankServiceGrpc;
import io.grpc.stub.StreamObserver;
import org.ayur.sec06.repository.AccountRepository;

public class BankService extends BankServiceGrpc.BankServiceImplBase {

    @Override
    public void getAccountBalance(BalanceCheckRequest request, StreamObserver<AccountBalance> responseObserver) {
        int accountNumber = request.getAccountNumber();
        var balance = AccountRepository.getBalance(accountNumber);
        AccountBalance accountBalance = AccountBalance.newBuilder().setAccountNumber(accountNumber).setBalance(balance).build();
        responseObserver.onNext(accountBalance);
        responseObserver.onCompleted();
    }
}
