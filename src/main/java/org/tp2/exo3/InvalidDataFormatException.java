package org.tp2.exo3;



public class InvalidDataFormatException extends RuntimeException {
    public InvalidDataFormatException(String message) {
        super(message);
    }

    public InvalidDataFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}