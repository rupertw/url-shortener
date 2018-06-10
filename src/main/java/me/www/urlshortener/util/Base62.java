package me.www.urlshortener.util;

import java.util.HashMap;

/**
 * Base62
 * number encoding and decoding system based on ASCII 62 characters(A-Za-z0-9)
 *
 * @author user
 */
public class Base62 {

    private static final char[] BASE62_CODE = "aAbBcCdDeEfFgGhHiIjJkKlLmMnNoOpPqQrRsStTuUvVwWxXyYzZ0123456789".toCharArray();
    private static final HashMap<Character, Integer> BASE62_INDEX = new HashMap<>();

    static {
        for (int i = 0; i < BASE62_CODE.length; i++) {
            BASE62_INDEX.put(BASE62_CODE[i], i);
        }
    }

    public static String encode(long num) {
        StringBuilder encodedText = new StringBuilder();

        do {
            int remainder = (int) (num % 62);
            encodedText.append(BASE62_CODE[remainder]);
            num /= 62;
        } while (num > 0);

        return encodedText.toString();
    }

    public static long decode(String code) {
        long decodedNumber = 0;
        long pow = 1;

        for (int i = 0; i < code.length(); i++) {
            int digit = BASE62_INDEX.get(code.charAt(i));
            decodedNumber += digit * pow;
            pow *= 62;
        }

        return decodedNumber;
    }
}