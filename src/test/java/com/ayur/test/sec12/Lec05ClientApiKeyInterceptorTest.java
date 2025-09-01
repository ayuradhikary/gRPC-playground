package com.ayur.test.sec12;

import com.ayur.models.sec12.AccountBalance;
import com.ayur.models.sec12.BalanceCheckRequest;
import io.grpc.ClientInterceptor;
import io.grpc.Metadata;
import io.grpc.stub.MetadataUtils;
import org.ayur.common.GrpcServer;
import org.ayur.sec12.BankService;
import org.ayur.sec12.interceptors.ApiKeyValidationInterceptor;
import org.ayur.sec12.interceptors.GzipResponseInterceptor;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Lec05ClientApiKeyInterceptorTest extends AbstractInterceptorTest {

    private static final Logger logger = LoggerFactory.getLogger(Lec05ClientApiKeyInterceptorTest.class);
    private static final Metadata.Key<String> API_KEY = Metadata.Key.of("api-key", Metadata.ASCII_STRING_MARSHALLER);

    @Override
    protected GrpcServer createServer() {
        return GrpcServer.create(6565, builder -> {
            builder.addService(new BankService())
                    .intercept(new ApiKeyValidationInterceptor());
        });
    }

    @Override
    protected List<ClientInterceptor> getClientInterceptors() {
        return List.of(MetadataUtils.newAttachHeadersInterceptor(getApiKey()));
    }

    @Test
    public void clientApiKeyDemo() {
        BalanceCheckRequest balanceCheckRequest = BalanceCheckRequest.newBuilder().setAccountNumber(1).build();
        AccountBalance accountBalance = bankStub.getAccountBalance(balanceCheckRequest);
        logger.info("{}", accountBalance);
    }

    private Metadata getApiKey() {
        var metadata = new Metadata();
        metadata.put(API_KEY, "bank-client-secret");
        return metadata;
    }
}
