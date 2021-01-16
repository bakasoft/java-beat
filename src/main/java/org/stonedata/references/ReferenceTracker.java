package org.stonedata.references;

public interface ReferenceTracker {
    Object retrieve(String typeName, String reference);

    void store(String typeName, Object value, String reference);
}
