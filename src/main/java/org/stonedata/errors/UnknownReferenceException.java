package org.stonedata.errors;

import org.stonedata.util.PP;

public class UnknownReferenceException extends StoneException {
    public UnknownReferenceException(String typeName, String reference) {
        super(generateMessage(typeName, reference));
    }

    private static String generateMessage(String typeName, String reference) {
        return String.format("Unknown reference %s<%s>",
                typeName != null ? typeName : "",
                PP.str(reference));
    }
}
