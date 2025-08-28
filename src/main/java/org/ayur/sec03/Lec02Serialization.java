package org.ayur.sec03;

import com.ayur.models.sec03.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class Lec02Serialization {

    private static final Logger log = LoggerFactory.getLogger(Lec02Serialization.class);
    private static final Path PATH = Path.of("person.out");

    public static void main(String[] args) throws IOException {
        var person = com.ayur.models.sec03.Person.newBuilder().setLastName("sam")
                .setAge(12)
                .setEmail("sam@gmail.com")
                .setEmployed(true)
                .setSalary(1000.2345)
                .setBankAccountNumber(12345678L)
                .setBalance(-10000)
                .build();

        serialize(person);
        log.info("{}", deserialize());
        log.info("equals:{} ",person.equals(deserialize()));
        log.info("bytes length: {}", person.toByteArray().length);
    }

    public static void serialize(Person person) throws IOException {
        try(OutputStream stream = Files.newOutputStream(PATH)) {
            person.writeTo(stream);
        }
    }

    public static Person deserialize() throws IOException{
        try(InputStream stream = Files.newInputStream(PATH)) {
            return Person.parseFrom(stream);
        }
    }

}
