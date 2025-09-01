package com.ayur.test.sec12;

import com.ayur.models.sec12.BalanceCheckRequest;
import com.ayur.test.sec12.interceptors.GzipRequestInterceptor;
import io.grpc.ClientInterceptor;
import org.junit.jupiter.api.Test;

import java.util.List;

public class Lec04GzipInterceptorTest extends AbstractInterceptorTest {

    @Override
    protected List<ClientInterceptor> getClientInterceptors() {
        return List.of(new GzipRequestInterceptor());
    }

    @Test
    public void gzipDemo() {
        var request = BalanceCheckRequest.newBuilder().setAccountNumber(1).build();
        var response = this.bankStub.getAccountBalance(request);
    }
}
