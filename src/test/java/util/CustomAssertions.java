package util;

import org.stonedata.util.PP;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.fail;

public class CustomAssertions {

    public static void assertContains(String part, String text) {
        if (!text.contains(part)) {
            fail("Expected " + PP.str(text) + " to contain " + PP.str(part) + ".");
        }
    }

    public static void assertNotInstanceOf(Class<?> expectedType, Object value) {
        if (expectedType.isInstance(value)) {
            throw new AssertionError("Not expected an instance of " + expectedType + ".");
        }
    }

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

    public static void assertSameText(String expected, String actual) {
        var expectedLines = expected.split("\n");
        var actualLines = actual.split("\n");

        int eI = 0;
        int aI = 0;

        while (eI < expectedLines.length || aI < actualLines.length) {
            var expectedLine = eI < expectedLines.length ? expectedLines[eI].trim() : "";
            var actualLine = aI < actualLines.length ? actualLines[aI].trim() : "";

            if (Objects.equals(expectedLine, actualLine)) {
                eI++;
                aI++;
            }
            else if (expectedLine.isEmpty()) {
                eI++;
            }
            else if (actualLine.isEmpty()) {
                aI++;
            }
            else {
                var message = String.format(
                        "" +
                                "Expected (Ln. %s): %s\n" +
                                "But was  (Ln. %s): %s\n",
                        eI, expectedLine, aI, actualLine);
                throw new AssertionError(message);
            }
        }
    }

    public interface TestRunnable {
        void run() throws Exception;
    }

}
