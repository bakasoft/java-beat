package org.stonedata.references;

public interface ReferenceTracker {
    Object retrieve(String reference);

    void store(String reference, Object value);
}
