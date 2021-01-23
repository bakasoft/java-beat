package org.stonedata.errors;

public class UnsupportedValueException extends StoneException {
    public UnsupportedValueException(String expectedType, String actualValue) {
        super(String.format(
                "Value %s is not supported, expected type: %s", actualValue, expectedType
        ));
    }
    public UnsupportedValueException(String message) {
        super(message);
    }
}
