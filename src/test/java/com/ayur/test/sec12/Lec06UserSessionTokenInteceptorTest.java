package com.ayur.test.sec12;

import com.ayur.models.sec12.AccountBalance;
import com.ayur.models.sec12.BalanceCheckRequest;
import com.ayur.models.sec12.Money;
import com.ayur.models.sec12.WithdrawRequest;
import com.ayur.test.common.ResponseObserver;
import io.grpc.CallCredentials;
import io.grpc.ClientInterceptor;
import io.grpc.Metadata;
import org.ayur.common.GrpcServer;
import org.ayur.sec12.BankService;
import org.ayur.sec12.Constants;
import org.ayur.sec12.interceptors.UserTokenIntercepter;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;

public class Lec06UserSessionTokenInteceptorTest extends AbstractInterceptorTest {

    private static final Logger logger = LoggerFactory.getLogger(Lec06UserSessionTokenInteceptorTest.class);

    @Override
    protected GrpcServer createServer() {
        return GrpcServer.create(6565, builder -> {
            builder.addService(new BankService())
                    .intercept(new UserTokenIntercepter());
        });
    }

    @Override
    protected List<ClientInterceptor> getClientInterceptors() {
        return Collections.emptyList();
    }

    @Test
    public void unaryUserCredentialsDemo() {
        for (int i = 1; i <= 5; i++) {
            BalanceCheckRequest balanceCheckRequest = BalanceCheckRequest.newBuilder().setAccountNumber(1).build();
            AccountBalance accountBalance = bankStub.withCallCredentials(new UserSessionToken("user-token-" + i)).getAccountBalance(balanceCheckRequest);
            logger.info("{}", accountBalance);
        }
    }

    @Test
    public void streamingUserCredentialsDemo() {
        for (int i = 1; i <= 5; i++) {
            ResponseObserver<Money> responseObserver = ResponseObserver.create();
            WithdrawRequest request = WithdrawRequest.newBuilder().setAccountNumber(i).setAmount(30).build();
            this.asyncBankStub.withCallCredentials(new UserSessionToken("user-token-" + i)).withdraw(request, responseObserver);
            responseObserver.await();
        }
    }

    private static class UserSessionToken extends CallCredentials {

        private static final String TOKEN_FORMAT = "%s %s";

        private final String jwt;

        private UserSessionToken(String jwt) {
            this.jwt = jwt;
        }

        @Override
        public void applyRequestMetadata(RequestInfo requestInfo, Executor executor, MetadataApplier metadataApplier) {
            executor.execute(() -> {
                Metadata metadata = new Metadata();
                metadata.put(Constants.USER_TOKEN_KEY, TOKEN_FORMAT.formatted(Constants.BEARER, jwt));
                metadataApplier.apply(metadata);
            });
        }
    }


}
