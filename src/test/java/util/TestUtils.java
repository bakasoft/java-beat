package util;

import org.stonedata.Stone;

import java.io.IOException;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestUtils {

    public static void assertSameOutput(Stone stone, Object result, String text) throws IOException {
        var buffer = new StringBuilder();

        stone.writeText(result, buffer, true);

        var expectedLines = text.split("\n");
        var actualLines = buffer.toString().split("\n");

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

    public static byte[] parseBinaryBytes(String input) {
        var items = input.split(" +");
        var result = new byte[items.length];

        for (int i = 0; i < items.length; i++) {
            var value = Integer.parseInt(items[i], 2);

            if (value >= 0 && value <= 255) {
                result[i] = (byte)value;
            }
            else {
                throw new AssertionError(String.format("Binary number is not a valid byte: %s", items[i]));
            }
        }

        return result;
    }

    public static String formatBinaryBytes(byte[] data) {
        var result = new StringBuilder();

        for (int i = 0; i < data.length; i++) {
            var b = data[i];

            if (i > 0) {
                result.append(' ');
            }

            var bin = Integer.toBinaryString(0xFF & b);

            result.append("0".repeat(8 - bin.length()));
            result.append(bin);
        }

        return result.toString();
    }

    public static String sha1(byte[] data) {
        try {
            var md = MessageDigest.getInstance("SHA-1");

            return toHex(md.digest(data));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String toHex(byte[] hash) {
        var formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }

    public static String loadString(String name) {
        var url = TestUtils.class.getResource(name);

        if (url == null) {
            throw new AssertionError("Missing resource: " + name);
        }

        try (var reader = new InputStreamReader(url.openStream())) {
            var buffer = new StringBuilder();
            int c;

            while ((c = reader.read()) != -1) {
                buffer.append((char)c);
            }

            return buffer.toString();
        }
        catch (IOException e) {
            throw new AssertionError();
        }
    }

}
