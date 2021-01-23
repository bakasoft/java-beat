package org.stonedata.references.impl;

import org.stonedata.references.ReferenceProvider;

public class NullReferenceProvider implements ReferenceProvider {

    public static final NullReferenceProvider INSTANCE = new NullReferenceProvider();

    private NullReferenceProvider() {}

    @Override
    public String getReference(Object value) {
        return null;
    }
}
