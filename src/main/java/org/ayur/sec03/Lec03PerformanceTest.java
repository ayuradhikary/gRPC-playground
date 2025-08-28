package org.ayur.sec03;

import com.ayur.models.sec03.Person;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.InvalidProtocolBufferException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lec03PerformanceTest {

    private static final Logger log = LoggerFactory.getLogger(Lec03PerformanceTest.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {
        var protoPerson = com.ayur.models.sec03.Person.newBuilder().setLastName("sam")
                .setAge(12)
                .setEmail("sam@gmail.com")
                .setEmployed(true)
                .setSalary(1000.2345)
                .setBankAccountNumber(12345678L)
                .setBalance(-10000)
                .build();

        JsonPerson jsonPerson = new JsonPerson("sam", 12, "sam@gmail.com", true, 1000.2345, 12345678L, -10000);

//        for (int i = 0; i < 5; i++) {
//            runTest("json", () -> json(jsonPerson));
//            runTest("proto", () -> proto(protoPerson));
//        }
        json(jsonPerson);
        proto(protoPerson);

    }

    private static void proto(Person person) {
        try {
            byte[] byteArray = person.toByteArray();
            log.info("proto byte length: {}", byteArray.length);
            Person.parseFrom(byteArray);
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }
    }

    private static void json(JsonPerson person) {
        try {
            byte[] byteArray = mapper.writeValueAsBytes(person);
            log.info("json byte length: {}", byteArray.length);
            mapper.readValue(byteArray, JsonPerson.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private static void runTest(String testName, Runnable runnable) {
        Long start = System.currentTimeMillis();
        for (int i = 0; i < 5_000_000; i++) {
            runnable.run();
        }
        Long end = System.currentTimeMillis();
        log.info("time taken for {} - {} ms", testName, (end - start));
    }


}
