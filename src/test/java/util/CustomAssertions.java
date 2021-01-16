package util;

public class CustomAssertions {

    public static void assertInstanceOf(Class<?> expectedType, Object value) {
        if (value == null) {
            throw new AssertionError("Expected an instance of " + expectedType + " instead of null.");
        }
        else if (!expectedType.isInstance(value)) {
            var actualType = value.getClass();

            throw new AssertionError("Expected an instance of " + expectedType + " instead of " + actualType + ".");
        }
    }

    public static <T extends Exception> T assertException(Class<T> exceptionType, TestRunnable runnable) {
        try {
            runnable.run();
        }
        catch (Exception e) {
            if (exceptionType.isInstance(e)) {
                return exceptionType.cast(e);
            }

            throw new AssertionError("Unexpected exception type: " + e.getClass().getName(), e);
        }

        throw new AssertionError("Expected exception: " + exceptionType.getName());
    }

    public interface TestRunnable {
        void run() throws Exception;
    }

}
