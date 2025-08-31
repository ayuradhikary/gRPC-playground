package org.ayur.sec08;

import com.ayur.models.sec08.GuessRequest;
import com.ayur.models.sec08.GuessResponse;
import com.ayur.models.sec08.Result;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class GuessNumberRequestHandler implements StreamObserver<GuessRequest> {

    private final Logger logger = LoggerFactory.getLogger(GuessNumberRequestHandler.class);

    private final StreamObserver<GuessResponse> responseObserver;
    private final int secret;
    private int attempts = 0;

    public GuessNumberRequestHandler(StreamObserver<GuessResponse> responseObserver) {
        this.responseObserver = responseObserver;
        this.secret = new Random().nextInt(100) + 1;
        logger.info("Secret number : {} ", secret);
    }

    @Override
    public void onNext(GuessRequest guessRequest) {
        attempts++;
        int guess = guessRequest.getGuess();
        Result result;
        if (guess == secret) {
            result = Result.CORRECT;
        } else if (guess < secret) {
            result = Result.TOO_LOW;
        } else {
            result = Result.TOO_HIGH;
        }
        GuessResponse response = GuessResponse.newBuilder()
                .setAttempt(attempts)
                .setResult(result)
                .build();
        responseObserver.onNext(response);

        if (result == Result.CORRECT) {
            // End the stream once guessed correctly
            responseObserver.onCompleted();
        }
    }

    @Override
    public void onError(Throwable throwable) {
        logger.info("Error: {}", throwable.getMessage());
    }

    @Override
    public void onCompleted() {
        logger.info("Client guessed the number.");
        responseObserver.onCompleted();
    }
}
