package org.ayur.sec03;

import com.ayur.models.sec03.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LecO7DefaultValues {
    private static final Logger log = LoggerFactory.getLogger(Lec06Map.class);

    public static void main(String[] args) {

        var school = School.newBuilder().build();

        log.info("{}",school.getId());
        log.info("{}",school.getName());
        log.info("{}", school.getAddress());

        log.info("is default?: {}",school.getAddress().equals(Address.getDefaultInstance()) );

        // has
        log.info("has address? {}", school.hasAddress());

        //collection
        var lib = Library.newBuilder().build();
        log.info("{}", lib.getBooksList());

        //map
        var dealer = Dealer.newBuilder().build();
        log.info("{}", dealer.getInventoryMap());

        //enum
        var car = Car.newBuilder().build();
        log.info("{}", car.getBodyStyle());


    }
}
