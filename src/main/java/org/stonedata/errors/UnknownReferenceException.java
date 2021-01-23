package org.stonedata.errors;

import org.stonedata.util.PP;

public class UnknownReferenceException extends StoneException {
    public UnknownReferenceException(String reference) {
        super(generateMessage(reference));
    }

    private static String generateMessage(String reference) {
        return String.format("Unknown reference %s.",
                PP.str(reference));
    }
}
