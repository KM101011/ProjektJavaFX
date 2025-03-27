package com.example.projekt_1.exceptions;

public class PasswordHashingException extends Exception {

    public PasswordHashingException() {
        super();
    }

    public PasswordHashingException(String message) {
        super(message);
    }

    public PasswordHashingException(Throwable cause) {
        super(cause);
    }

    public PasswordHashingException(String message, Throwable cause) {
        super(message, cause);
    }
}
