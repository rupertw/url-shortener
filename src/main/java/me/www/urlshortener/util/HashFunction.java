package me.www.urlshortener.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * HashFunction
 *
 * @author user
 */
public class HashFunction {

    public static String digestWithSHA256(String[] strArr) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            for (String item : strArr) {
                md.update(item.getBytes());
            }

            return Base64.getEncoder().encodeToString(md.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}