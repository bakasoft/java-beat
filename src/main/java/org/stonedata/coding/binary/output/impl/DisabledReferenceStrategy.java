package org.stonedata.coding.binary.output.impl;

import org.stonedata.coding.binary.output.ReferenceStrategy;

import java.util.OptionalLong;

public class DisabledReferenceStrategy implements ReferenceStrategy {

    @Override
    public boolean accepts(Object value) {
        return false;
    }

    @Override
    public OptionalLong lookUp(Object value) {
        return OptionalLong.empty();
    }

    @Override
    public long save(Object value) {
        throw new UnsupportedOperationException();
    }
}
