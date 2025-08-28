package org.ayur.sec03;

import com.ayur.models.sec03.Credentials;
import com.ayur.models.sec03.Email;
import com.ayur.models.sec03.Phone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lec08OneOf {

    private static final Logger log = LoggerFactory.getLogger(Lec08OneOf.class);

    public static void main(String[] args) {

        var email = Email.newBuilder().setAddress("sam@gmail.com").setPassword("admin").build();
        var phone = Phone.newBuilder().setNumber(9812523930L).setCode(123).build();

        login(Credentials.newBuilder().setEmail(email).build());
        login(Credentials.newBuilder().setPhone(phone).build());

        //if we set both then the last one which has been set will be used,  in the case below the phone will be used.
        login(Credentials.newBuilder().setEmail(email).setPhone(phone).build());

    }

    private static void login(Credentials credentials) {
        switch (credentials.getLoginTypeCase()) {
            case EMAIL -> log.info("email -> {}", credentials.getEmail());
            case PHONE -> log.info("phone -> {}", credentials.getPhone());
        }

    }

}
