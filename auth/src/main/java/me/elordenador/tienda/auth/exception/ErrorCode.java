package me.elordenador.tienda.auth.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST),
    REQUIRED_FULLNAME(HttpStatus.BAD_REQUEST),
    REQUIRED_USERNAME(HttpStatus.BAD_REQUEST),
    INVALID_USERNAME_LENGTH(HttpStatus.BAD_REQUEST),
    REQUIRED_EMAIL(HttpStatus.BAD_REQUEST),
    INVALID_EMAIL(HttpStatus.BAD_REQUEST),
    REQUIRED_PASSWORD(HttpStatus.BAD_REQUEST),
    PASSWORD_TOO_SHORT(HttpStatus.BAD_REQUEST),
    WEAK_PASSWORD(HttpStatus.BAD_REQUEST),

    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT),
    USERNAME_ALREADY_EXISTS(HttpStatus.CONFLICT),
    USER_DISABLED(HttpStatus.FORBIDDEN),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND);

    private final HttpStatus status;

    public static final class Codes {
        public static final String REQUIRED_FULLNAME = "REQUIRED_FULLNAME";
        public static final String REQUIRED_USERNAME = "REQUIRED_USERNAME";
        public static final String INVALID_USERNAME_LENGTH = "INVALID_USERNAME_LENGTH";
        public static final String REQUIRED_EMAIL = "REQUIRED_EMAIL";
        public static final String INVALID_EMAIL = "INVALID_EMAIL";
        public static final String REQUIRED_PASSWORD = "REQUIRED_PASSWORD";
        public static final String PASSWORD_TOO_SHORT = "PASSWORD_TOO_SHORT";
        public static final String WEAK_PASSWORD = "WEAK_PASSWORD";

        private Codes() {}
    }
}
