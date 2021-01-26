package org.beat.errors;

public class ExaminerNotFoundException extends BeatException {
    public ExaminerNotFoundException(Class<?> type) {
        super(generateMessage(type));
    }

    private static String generateMessage(Class<?> type) {
        return String.format("Missing examiner for %s.", type);
    }
}
