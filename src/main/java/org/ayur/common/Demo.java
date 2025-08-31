package org.ayur.common;

import org.ayur.sec06.BankService;
import org.ayur.sec06.TransferService;
import org.ayur.sec07.FlowControlService;
import org.ayur.sec08.GuessNumberService;

public class Demo {

    public static void main(String[] args) {
        GrpcServer.create(new BankService(), new TransferService(), new FlowControlService(), new GuessNumberService()).start().await();
    }
}
