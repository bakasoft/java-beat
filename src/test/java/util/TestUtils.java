package util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

public class TestUtils {

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
