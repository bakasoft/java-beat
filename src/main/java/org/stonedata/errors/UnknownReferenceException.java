package org.stonedata.errors;

import org.stonedata.io.TextLocation;
import org.stonedata.util.PP;

public class UnknownReferenceException extends InvalidSyntaxException {
    public UnknownReferenceException(TextLocation location, String type, Object reference) {
        super(location, generateMessage(type, reference));
    }

    private static String generateMessage(String type, Object reference) {
        return String.format("Unknown reference %s<%s>",
                type != null ? type : "",
                PP.str(reference));
    }
}
