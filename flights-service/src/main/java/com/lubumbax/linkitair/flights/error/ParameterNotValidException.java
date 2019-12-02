package com.lubumbax.linkitair.flights.error;

public class ParameterNotValidException extends RuntimeException {
    public ParameterNotValidException(String value) {
        super("The parameter with value [" + value + "] is not valid");
    }

    public ParameterNotValidException(String name, String value) {
        super("The parameter [" + name + "] with value [" + value + "] is not valid");
    }

    public ParameterNotValidException(String name, String value, String message) {
        super("The parameter [" + name + "] with value [" + value + "] is not valid: message");
    }
}
