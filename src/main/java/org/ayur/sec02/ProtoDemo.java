package org.ayur.sec02;

import org.ayur.sec01.SimpleProtoDemo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ayur.models.sec02.Person;

public class ProtoDemo {

    private static final Logger log = LoggerFactory.getLogger(SimpleProtoDemo.class);

    public static void main(String[] args) {

        // create person
        var person1 = createPerson();

        // create another instance with same values
        var person2 = createPerson();

        //compare
        log.info("equals {}", person1.equals(person2));
        log.info("== {}", person1 == person2);

        //mutable? No

        //create another instance with different values.
        var person3 = person1.toBuilder().setName("Mike").build();

        log.info("equals {}", person1.equals(person3));
        log.info("== {}", person1 == person3);

        //null?
//        var person4 = person1.toBuilder().setName(null).build();
//        log.info("person4 {}", person4); //throws null pointer exception

        var person4 = person1.toBuilder().clearName().build();
        log.info("person4 {}", person4);
    }

    private static Person createPerson() {
        return Person.newBuilder()
                .setName("Ayur")
                .setAge(24)
                .build();
    }

}
