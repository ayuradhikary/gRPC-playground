package com.ayur.test.sec12;

import com.ayur.models.sec12.AccountBalance;
import com.ayur.models.sec12.BalanceCheckRequest;
import io.grpc.CallCredentials;
import io.grpc.ClientInterceptor;
import io.grpc.Metadata;
import org.ayur.common.GrpcServer;
import org.ayur.sec12.Constants;
import org.ayur.sec12.UserRoleBankService;
import org.ayur.sec12.interceptors.UserRoleInterceptor;
import org.ayur.sec12.interceptors.UserTokenIntercepter;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;

public class Lec07UserRoleContextTest extends AbstractInterceptorTest {

    private static final Logger logger = LoggerFactory.getLogger(Lec07UserRoleContextTest.class);

    @Override
    protected GrpcServer createServer() {
        return GrpcServer.create(6565, builder -> {
            builder.addService(new UserRoleBankService())
                    .intercept(new UserRoleInterceptor());
        });
    }

    @Override
    protected List<ClientInterceptor> getClientInterceptors() {
        return Collections.emptyList();
    }

    @Test
    public void unaryUserCredentialsDemo() {
        for (int i = 1; i <= 4; i++) {
            BalanceCheckRequest balanceCheckRequest = BalanceCheckRequest.newBuilder().setAccountNumber(i).build();
            AccountBalance accountBalance = bankStub.withCallCredentials(new UserSessionToken("user-token-" + i)).getAccountBalance(balanceCheckRequest);
            logger.info("{}", accountBalance);
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
