package kr.pincoin.durian.auth.exception;

import org.springframework.security.core.AuthenticationException;

public class InvalidSecretKeyException extends AuthenticationException  {
    public InvalidSecretKeyException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public InvalidSecretKeyException(String msg) {
        super(msg);
    }
}
