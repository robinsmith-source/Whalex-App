package org.example.exceptions;

public class WriteDataException extends Exception {

    public WriteDataException() {
        super("Error while loading data.");
    }

    public WriteDataException(String message) {
        super(message);
    }
}
