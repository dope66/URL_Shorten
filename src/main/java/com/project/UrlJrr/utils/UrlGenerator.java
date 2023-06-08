package com.project.UrlJrr.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.UUID;
@Slf4j
public class UrlGenerator {
    private static final String BASE62_CHARACTERS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int BASE62_RADIX = BASE62_CHARACTERS.length();

    public static String generateShortUrl() {
        UUID uuid = UUID.randomUUID();
        long mostSignificantBits = uuid.getMostSignificantBits();
        long leastSignificantBits = uuid.getLeastSignificantBits();
        StringBuilder sb = new StringBuilder();
        appendBase62EncodedValue(sb, mostSignificantBits);
        appendBase62EncodedValue(sb, leastSignificantBits);
        return sb.toString();
    }

    private static void appendBase62EncodedValue(StringBuilder sb, long value) {
        if (value < 0) {
            value = Math.abs(value); // 음수 값을 양수로 변환
        }
        if (value == 0) {
            sb.insert(0, BASE62_CHARACTERS.charAt(0)); // 0번째 문자를 추가
            return; // 반복문 실행하지 않고 종료
        }
        while (value > 0) {
            sb.insert(0, BASE62_CHARACTERS.charAt((int) (value % BASE62_RADIX)));
            value /= BASE62_RADIX;
        }
    }


    public static void main(String[] args) {
        String shortUrl = generateShortUrl();
        System.out.println("Short URL: " + shortUrl);
    }
}
