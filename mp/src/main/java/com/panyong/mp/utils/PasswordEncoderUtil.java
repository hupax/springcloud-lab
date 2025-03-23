package com.panyong.mp.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderUtil {
    
    public static void main(String[] args) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode("myPassword");
        
        System.out.println(hashedPassword);
        
        boolean isPasswordMatch = passwordEncoder.matches("myPassword", hashedPassword);
        System.out.println("Password matches: " + isPasswordMatch);
    }
}
