package org.beat.errors;

import org.beat.io.TextLocation;

public class InvalidSyntaxException extends BeatException {

    public InvalidSyntaxException(String message, TextLocation location) {
        super(generateMessage(location, message));
    }

    private static String generateMessage(TextLocation location, String message) {
        var line = location.getLine();
        var column = location.getColumn();
        var resource = location.getResource();
        return String.format("Syntax Error: %s (Ln. %s, Col. %s, %s)",
                message, line, column, resource);
    }

}
