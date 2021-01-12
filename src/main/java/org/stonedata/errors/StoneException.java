package org.stonedata.errors;

public class StoneException extends RuntimeException {

    public StoneException() {
        // TODO remove empty constructor
    }

    public StoneException(String message) {
        super(message);
    }

    public StoneException(String message, Exception cause) {
        super(message, cause);
    }

    public StoneException(Throwable cause) {
        super(cause);
        // TODO add message
    }
}
