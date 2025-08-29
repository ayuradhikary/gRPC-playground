package org.ayur.common;

import org.ayur.sec06.BankService;

public class Demo {

    public static void main(String[] args) {
        GrpcServer.create(new BankService()).start().await();
    }
}
