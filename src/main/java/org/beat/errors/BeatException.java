package org.beat.errors;

public class BeatException extends RuntimeException {

    public BeatException() {
        // TODO remove empty constructor
    }

    public BeatException(String message) {
        super(message);
    }

    public BeatException(String message, Exception cause) {
        super(message, cause);
    }

    public BeatException(Throwable cause) {
        super(cause);
        // TODO add message
    }
}
