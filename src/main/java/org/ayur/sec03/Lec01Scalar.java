package org.ayur.sec03;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ayur.models.sec03.Person;

public class Lec01Scalar {

    private static final Logger log = LoggerFactory.getLogger(Lec01Scalar.class);

    public static void main(String[] args) {

        var person = Person.newBuilder().setLastName("sam")
                .setAge(12)
                .setEmail("sam@gmail.com")
                .setEmployed(true)
                .setSalary(1000.2345)
                .setBankAccountNumber(12345678L)
                .setBalance(-10000)
                .build();

        log.info("person {}", person);
    }

}
