package org.beat.errors;

import org.beat.util.PP;

public class UnknownReferenceException extends BeatException {
    public UnknownReferenceException(String reference) {
        super(generateMessage(reference));
    }

    private static String generateMessage(String reference) {
        return String.format("Unknown reference %s.",
                PP.str(reference));
    }
}
