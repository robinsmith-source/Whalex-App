package org.example.exceptions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ReadDataException extends Exception {

    private static final Logger log = LogManager.getLogger(ReadDataException.class);

    public ReadDataException() {
        super("Error while reading data.");
    }

    public ReadDataException(String message) {
        super(message);
    }
}
