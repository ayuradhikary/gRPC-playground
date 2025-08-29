//package org.ayur.sec06;
//
//import com.ayur.models.sec06.AccountBalance;
//import com.ayur.models.sec06.BalanceCheckRequest;
//import com.ayur.models.sec06.BankServiceGrpc;
//import io.grpc.ManagedChannel;
//import io.grpc.ManagedChannelBuilder;
//import io.grpc.stub.StreamObserver;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

//public class GrpcClient {

//    private static final Logger log = LoggerFactory.getLogger(GrpcClient.class);

//    public static void main(String[] args) throws InterruptedException {

//        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 6565)
//                .usePlaintext()
//                .build();
//        BankServiceGrpc.BankServiceBlockingStub stub = BankServiceGrpc.newBlockingStub(channel);
//        AccountBalance accountBalance = stub.getAccountBalance(BalanceCheckRequest.newBuilder().setAccountNumber(2).build());
//        log.info("{}", accountBalance);

        //async stub
//        BankServiceGrpc.BankServiceStub stub = BankServiceGrpc.newStub(channel);
//        stub.getAccountBalance(BalanceCheckRequest.newBuilder().setAccountNumber(2).build(), new StreamObserver<AccountBalance>() {
//            @Override
//            public void onNext(AccountBalance accountBalance) {
//                log.info("{}", accountBalance);
//            }
//
//            @Override
//            public void onError(Throwable throwable) {
//
//            }
//
//            @Override
//            public void onCompleted() {
//                log.info("completed");
//            }
//        });

//        log.info("done");
//        Thread.sleep(2000);

//    }


//}
