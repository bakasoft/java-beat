package org.beat.errors;

public class WriterNotFoundException extends BeatException {
    public WriterNotFoundException(Class<?> targetType) {
        super(String.format("Not writer found for: %s", targetType));
    }
}
