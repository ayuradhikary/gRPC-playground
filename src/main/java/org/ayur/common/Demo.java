package org.ayur.common;

import org.ayur.sec06.BankService;
import org.ayur.sec06.TransferService;

public class Demo {

    public static void main(String[] args) {
        GrpcServer.create(new BankService(), new TransferService()).start().await();
    }
}
