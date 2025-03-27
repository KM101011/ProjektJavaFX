package com.example.projekt_1.exceptions;

public class BazaPodatakaException extends Exception{

    public BazaPodatakaException() {
        super();
    }

    public BazaPodatakaException(String message) {
        super(message);
    }

    public BazaPodatakaException(Throwable cause) {
        super(cause);
    }

    public BazaPodatakaException(String message, Throwable cause) {
        super(message, cause);
    }
}
