package me.elordenador.tienda.auth.exception;

import lombok.Getter;
@Getter
public class ApiException extends RuntimeException {
  private final ErrorCode errorCode;

  public ApiException(ErrorCode errorCode) {
    super(errorCode.name());
    this.errorCode = errorCode;
  }

}
