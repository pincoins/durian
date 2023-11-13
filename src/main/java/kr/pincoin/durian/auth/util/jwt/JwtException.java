package kr.pincoin.durian.auth.util.jwt;

public class JwtException extends RuntimeException {
    private final String errorCode;

    public JwtException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getCode() {
        return this.errorCode;
    }
}
