package com.project.UrlJrr.myutils;


import java.math.BigInteger;
import java.util.UUID;

public class UrlGenerator {
    private static final String BASE62_CHARACTERS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int BASE62_RADIX = BASE62_CHARACTERS.length();
    private static final int SHORT_URL_LENGTH = 8;

    public static String generateShortUrl() {
        UUID uuid = UUID.randomUUID();
        BigInteger uuidValue = new BigInteger(uuid.toString().replace("-", ""), 16);
        StringBuilder sb = new StringBuilder();
        while (sb.length() < SHORT_URL_LENGTH) {
            BigInteger[] quotientAndRemainder = uuidValue.divideAndRemainder(BigInteger.valueOf(BASE62_RADIX));
            sb.insert(0, BASE62_CHARACTERS.charAt(quotientAndRemainder[1].intValue()));
            uuidValue = quotientAndRemainder[0];
        }
        return sb.toString();
    }

//    public static void main(String[] args) {
//        String shortUrl = generateShortUrl();
//        System.out.println("Short URL: " + shortUrl);
//    }
}
