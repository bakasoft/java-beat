package org.beat.errors;

public class UnsupportedValueException extends BeatException {
    public UnsupportedValueException(String expectedType, String actualValue) {
        super(String.format(
                "Value %s is not supported, expected type: %s", actualValue, expectedType
        ));
    }
    public UnsupportedValueException(String message) {
        super(message);
    }
}
