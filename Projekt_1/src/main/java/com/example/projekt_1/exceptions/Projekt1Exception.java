package com.example.projekt_1.exceptions;

public class Projekt1Exception extends RuntimeException{

    public Projekt1Exception() {
        super();
    }

    public Projekt1Exception(String message) {
        super(message);
    }

    public Projekt1Exception(Throwable cause) {
        super(cause);
    }

    public Projekt1Exception(String message, Throwable cause) {
        super(message, cause);
    }
}
