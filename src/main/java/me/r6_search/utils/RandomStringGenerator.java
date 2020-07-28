package me.r6_search.utils;

import java.security.SecureRandom;

public class RandomStringGenerator {
    private static final String RANDOM_CODE = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public String generateRandomString(int len) {
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(len);
        for( int i = 0; i < len; i++ )
            sb.append(RANDOM_CODE.charAt( rnd.nextInt(RANDOM_CODE.length())));
        return sb.toString();
    }
}
