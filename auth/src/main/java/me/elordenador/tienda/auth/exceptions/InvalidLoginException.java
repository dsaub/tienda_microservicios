package me.elordenador.tienda.auth.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidLoginException extends ApiException {
    public InvalidLoginException() {
        super(HttpStatus.UNAUTHORIZED, "INVALID_LOGIN");
    }
}
