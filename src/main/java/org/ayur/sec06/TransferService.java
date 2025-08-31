package org.ayur.sec06;

import com.ayur.models.sec06.TransferRequest;
import com.ayur.models.sec06.TransferResponse;
import com.ayur.models.sec06.TransferServiceGrpc;
import io.grpc.stub.StreamObserver;
import org.ayur.sec06.requestHandlers.TransferRequestHandler;

public class TransferService extends TransferServiceGrpc.TransferServiceImplBase {

    @Override
    public StreamObserver<TransferRequest> transfer(StreamObserver<TransferResponse> responseObserver) {
        return new TransferRequestHandler(responseObserver);
    }
}
