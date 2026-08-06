package com.moriha.manager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class ManagerApiApplicationTests {

    @Autowired
    private PasswordEncoder encoder;

    @Test
    void testPasswordEncoder(){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = encoder.encode("moriha");
        System.out.println(password);
    }
}
