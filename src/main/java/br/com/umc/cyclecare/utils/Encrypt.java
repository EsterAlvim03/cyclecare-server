package br.com.umc.cyclecare.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Encrypt {
    public static String encryptPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }

    public static Boolean matchPasswords(String dbPassword, String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(password, dbPassword);
    }
}
