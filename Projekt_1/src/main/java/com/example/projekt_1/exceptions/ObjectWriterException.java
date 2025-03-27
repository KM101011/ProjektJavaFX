package com.example.projekt_1.exceptions;

public class ObjectWriterException extends RuntimeException {

    public ObjectWriterException() {
        super();
    }

    public ObjectWriterException(String message) {
        super(message);
    }

    public ObjectWriterException(Throwable cause) {
        super(cause);
    }

    public ObjectWriterException(String message, Throwable cause) {
        super(message, cause);
    }
}
