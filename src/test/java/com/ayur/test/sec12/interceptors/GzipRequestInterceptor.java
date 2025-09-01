package com.ayur.test.sec12.interceptors;

import io.grpc.*;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class GzipRequestInterceptor implements ClientInterceptor {
    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> methodDescriptor, CallOptions callOptions, Channel channel) {
        callOptions = Objects.nonNull(callOptions.getCompressor()) ? callOptions : callOptions.withCompression("gzip");
        return channel.newCall(methodDescriptor, callOptions);
    }
}
