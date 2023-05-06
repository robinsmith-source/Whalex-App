package org.example.exceptions;

public class ReadDataException extends Exception {

    public ReadDataException() {
        super("Error while reading data.");
    }

    public ReadDataException(String message) {
        super(message);
    }
}
