package kr.pincoin.durian.common.util;

import org.apache.tomcat.util.codec.binary.Base64;

import java.security.SecureRandom;

public class RandomString {
    public enum Type {
        ALPHABET,
        UPPERCASE,
        LOWERCASE,
        NUMERIC,
        MNEMONIC,
        ALPHANUMERIC,
    }

    public static final String RANDOM_DIGITS = "0123456789";
    public static final String RANDOM_LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    public static final String RANDOM_UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    // 숫자 + 대문자 (소문자 미포함)
    // 숫자: 0, 1 제외
    // 대문자: I, O 제외
    public static final String RANDOM_MNEMONIC = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ";

    private final char[] symbols;

    private final char[] buf;

    private static final SecureRandom rnd = new SecureRandom();

    public RandomString(Type type, int length) {
        switch (type) {
            case MNEMONIC -> this.symbols = RANDOM_MNEMONIC.toCharArray();
            case ALPHABET -> this.symbols = (RANDOM_UPPERCASE + RANDOM_LOWERCASE).toCharArray();
            case UPPERCASE -> this.symbols = RANDOM_UPPERCASE.toCharArray();
            case LOWERCASE -> this.symbols = RANDOM_LOWERCASE.toCharArray();
            case NUMERIC -> this.symbols = RANDOM_DIGITS.toCharArray();
            default -> this.symbols = (RANDOM_DIGITS + RANDOM_LOWERCASE + RANDOM_UPPERCASE).toCharArray();
        }
        this.buf = new char[length];
    }

    public RandomString() {
        this(Type.ALPHANUMERIC, 8);
    }

    public RandomString(Type type) {
        this(type, 8);
    }

    public RandomString(int length) {
        this(Type.ALPHANUMERIC, length);
    }

    public String randomString() {
        for (int idx = 0; idx < buf.length; ++idx)
            buf[idx] = symbols[rnd.nextInt(symbols.length)];
        return new String(buf);
    }

    String base64encodedRandomString() {
        return Base64.encodeBase64String(randomString().getBytes());
    }
}
