package com.payments.paymentsconciliationservice.infrastructure.exceptions;

public class CustomProcessException extends RuntimeException {
    private final String errorCode;
    private final ErrorMessageService errorMessageService;

    public CustomProcessException(String errorCode, ErrorMessageService errorMessageService) {
        super(errorCode); // Guardamos solo el código, el mensaje lo obtenemos después
        this.errorCode = errorCode;
        this.errorMessageService = errorMessageService;

    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return errorMessageService.getMessage(errorCode); // R
    }


}