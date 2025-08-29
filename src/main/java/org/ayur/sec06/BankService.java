package org.ayur.sec06;

import com.ayur.models.sec06.AccountBalance;
import com.ayur.models.sec06.AllAccountsResponse;
import com.ayur.models.sec06.BalanceCheckRequest;
import com.ayur.models.sec06.BankServiceGrpc;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import org.ayur.sec06.repository.AccountRepository;

import java.util.List;
import java.util.Map;

public class BankService extends BankServiceGrpc.BankServiceImplBase {

    @Override
    public void getAccountBalance(BalanceCheckRequest request, StreamObserver<AccountBalance> responseObserver) {
        int accountNumber = request.getAccountNumber();
        var balance = AccountRepository.getBalance(accountNumber);
        AccountBalance accountBalance = AccountBalance.newBuilder().setAccountNumber(accountNumber).setBalance(balance).build();
        responseObserver.onNext(accountBalance);
        responseObserver.onCompleted();
    }

    @Override
    public void getAllAccounts(Empty request, StreamObserver<AllAccountsResponse> responseObserver) {
        List<AccountBalance> accounts =  AccountRepository.getAllAccounts()
                .entrySet().stream()
                .map(e -> AccountBalance.newBuilder()
                        .setAccountNumber(e.getKey())
                        .setBalance(e.getValue())
                        .build()).toList();

        AllAccountsResponse response = AllAccountsResponse.newBuilder().addAllAccounts(accounts).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
