package org.beat.util;

public class PP {

    private PP() {}

    public static String str(char c) {
        return str(String.valueOf(c));
    }

    public static String str(Object value) {
        if (value == null) {
            return "null";
        }

        return str(value.toString());
    }

    public static String str(String value) {
        if (value.indexOf('\"') != -1 && value.indexOf('\'') == -1) {
            return str(value, '\'');
        }

        return str(value, '\"');
    }

    private static String str(String value, char delimiter) {
        var output = new StringBuilder();

        output.append(delimiter);

        for (var c : value.toCharArray()) {
            if (c == delimiter || c == '\"' || c == '\'' || c == '\\') {
                output.append('\\');
                output.append(c);
            }
            else if (c == '\n') {
                output.append("\\n");
            }
            else if (c == '\r') {
                output.append("\\r");
            }
            else if (c == '\t') {
                output.append("\\t");
            }
            else if (c >= 0x20 && c <= 0x7E) {
                output.append(c);
            }
            else {
                output.append("\\u");
                var hex = Integer.toHexString(c);
                if (hex.length() < 4) {
                    output.append("0".repeat(4 - hex.length()));
                }
                output.append(hex);
            }
        }

        output.append(delimiter);

        return output.toString();
    }

    public static Object typeOf(Object value) {
        if (value == null) {
            return "null";
        }
        return type(value.getClass());
    }

    public static String type(Class<?> type) {
        if (type == null) {
            return "null";
        }
        return type.getName();
    }

    public static String hex(int value) {
        var valueL = (0xFFFFFFFFL & value);

        return "0x" + Long.toHexString(valueL).toUpperCase();
    }
}
