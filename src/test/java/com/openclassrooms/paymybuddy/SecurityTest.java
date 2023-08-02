package com.openclassrooms.paymybuddy;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SecurityTest {
    @Test
    public void BCryptPasswordEncoder() {

        String strPassword = "testPassword";
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(16);
        String result = bCryptPasswordEncoder.encode(strPassword);
        assertTrue(bCryptPasswordEncoder.matches("testPassword", result));
    }
}
