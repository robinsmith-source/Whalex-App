package org.example.exceptions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoadDataException extends Exception {

    private static final Logger log = LogManager.getLogger(LoadDataException.class);

    public LoadDataException() {
        super("Error while loading data.");
    }

    public LoadDataException(String message) {
        super(message);
    }
}
