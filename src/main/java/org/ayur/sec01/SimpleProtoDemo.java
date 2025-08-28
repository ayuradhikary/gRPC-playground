package org.ayur.sec01;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ayur.models.sec01.Person;

public class SimpleProtoDemo {

    private static final Logger log = LoggerFactory.getLogger(SimpleProtoDemo.class);

    public static void main(String[] args) {

        Person person = Person.newBuilder().setAge(24).setName("shyam").build();

        log.info("{}", person);

    }

}
