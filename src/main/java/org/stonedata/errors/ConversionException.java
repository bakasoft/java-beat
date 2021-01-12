package org.stonedata.errors;

public class ConversionException extends StoneException {
    public ConversionException(String message) {
        super(message);
    }

    public ConversionException(Class<?> source, Class<?> target) {
        super(generateMessage(source, target));
    }

    private static String generateMessage(Class<?> source, Class<?> target) {
        return String.format("Cannot convert %s to %s.", source, target);
    }
}
