package org.ayur.sec08;

import com.ayur.models.sec08.GuessNumberGrpc;
import com.ayur.models.sec08.GuessRequest;
import com.ayur.models.sec08.GuessResponse;
import io.grpc.stub.StreamObserver;

public class GuessNumberService extends GuessNumberGrpc.GuessNumberImplBase {

    @Override
    public StreamObserver<GuessRequest> makeGuess(StreamObserver<GuessResponse> responseObserver) {
        return new GuessNumberRequestHandler(responseObserver);
    }
}
