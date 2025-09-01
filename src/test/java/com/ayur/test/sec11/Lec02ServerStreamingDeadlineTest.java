package com.ayur.test.sec11;

import com.ayur.models.sec11.Money;
import com.ayur.models.sec11.WithdrawRequest;
import io.grpc.Deadline;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class Lec02ServerStreamingDeadlineTest extends AbstractTest {

    private final Logger logger = LoggerFactory.getLogger(Lec02ServerStreamingDeadlineTest.class);

    @Test
    public void blockingDeadlineTest() {
        StatusRuntimeException ex = Assertions.assertThrows(StatusRuntimeException.class, () -> {
            WithdrawRequest withdrawRequest = WithdrawRequest.newBuilder().setAccountNumber(1).setAmount(50).build();
            Iterator<Money> iterator = this.bankStub.withDeadline(Deadline.after(2, TimeUnit.SECONDS)).withdraw(withdrawRequest);
            while (iterator.hasNext()) {
                logger.info("{}", iterator.next());
            }
        });
        Assertions.assertEquals(Status.Code.DEADLINE_EXCEEDED, ex.getStatus().getCode());
    }

}
