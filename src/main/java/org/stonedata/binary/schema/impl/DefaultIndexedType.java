package org.stonedata.binary.schema.impl;

import org.stonedata.util.PP;
import org.stonedata.binary.schema.IndexedType;

public class DefaultIndexedType implements IndexedType {
    private final int index;

    public DefaultIndexedType(int index) {
        this.index = index;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return String.format("Type<%s>", PP.hex(index));
    }
}
