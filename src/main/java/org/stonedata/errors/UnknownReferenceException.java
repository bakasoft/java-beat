package org.stonedata.errors;

import org.stonedata.util.PP;

public class UnknownReferenceException extends StoneException {
    public UnknownReferenceException(String type, Object reference) {
        super(generateMessage(type, reference));
    }

    private static String generateMessage(String type, Object reference) {
        return String.format("Unknown reference: %s<%s>",
                type != null ? type : "",
                PP.str(reference));
    }
}
