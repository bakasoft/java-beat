package org.stonedata.errors;

import org.stonedata.util.PP;

public class UnsupportedValueException extends StoneException {
    public UnsupportedValueException(Object value) {
        super("Unsupported value: " + PP.str(value));
    }
}
