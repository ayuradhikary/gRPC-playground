package org.ayur.sec12.interceptors;

import io.grpc.*;
import org.ayur.sec12.Constants;
import org.ayur.sec12.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Set;

public class UserRoleInterceptor implements ServerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(UserRoleInterceptor.class);
    private static final Set<String> PRIME_SET = Set.of("user-token-1", "user-token-2");
    private static final Set<String> STANDARD_SET = Set.of("user-token-3", "user-token-4");

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall, Metadata metadata, ServerCallHandler<ReqT, RespT> serverCallHandler) {
        var token = extractToken(metadata.get(Constants.USER_TOKEN_KEY));
        log.info("token: {}", token);
        Context ctxt = toContext(token);
        if (Objects.nonNull(ctxt)) {
//            return serverCallHandler.startCall(serverCall, metadata); we cannot simply do this here because this will use the default context not the context that we have created in the toContext method below.
            return Contexts.interceptCall(ctxt, serverCall, metadata, serverCallHandler); // this way we passed the context that we have created with the user role.
        }
        return close(serverCall, metadata, Status.UNAUTHENTICATED.withDescription("token is either null or invalid"));
    }

    private String extractToken(String value) {
        return Objects.nonNull(value) && value.startsWith(Constants.BEARER) ? value.substring(Constants.BEARER.length()).trim() : null;
    }

    private Context toContext(String token) {
        if (Objects.nonNull(token) && (PRIME_SET.contains(token) || STANDARD_SET.contains(token))) {
            var role = PRIME_SET.contains(token) ? UserRole.PRIME : UserRole.STANDARD;
            return Context.current().withValue(Constants.USER_ROLE_KEY, role); // CONTEXT is imputable thats why we are creating another object of context and then returning it
        }
        return null;
    }

    private <ReqT, RespT> ServerCall.Listener<ReqT> close(ServerCall<ReqT, RespT> serverCall, Metadata metadata, Status status) {
        serverCall.close(status, metadata);
        return new ServerCall.Listener<ReqT>() {
        };
    }
}
