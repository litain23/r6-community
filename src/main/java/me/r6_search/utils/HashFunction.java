package me.r6_search.utils;

import org.apache.commons.codec.binary.Hex;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashFunction {
    public String generateHashString(String str) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hashedByte = md.digest(str.getBytes(Charset.forName("UTF8")));
        String hashString = Hex.encodeHexString(hashedByte);
        return hashString;
    }
}
