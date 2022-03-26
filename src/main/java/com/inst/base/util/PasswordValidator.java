package com.inst.base.util;

import org.springframework.http.HttpStatus;

import java.util.Base64;

public class PasswordValidator {
    public static String decodePassword(String source) {
        try {
            return new String(Base64.getDecoder().decode(source.getBytes()));
        } catch(Exception e) {
            throw new ServiceException("Can`t parse password for Base64. Check given password.", HttpStatus.BAD_REQUEST);
        }
    }

    public static String decodeAndValidatePassword(String source) {
        String password = decodePassword(source);

        if(password.length() < 6 || password.length() > 64)
            throw new ServiceException("Minimum password length 6, maximum 64", HttpStatus.BAD_REQUEST);

        return password;
    }
}
