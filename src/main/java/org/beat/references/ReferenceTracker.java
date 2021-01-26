package org.beat.references;

public interface ReferenceTracker {
    Object retrieve(String reference);

    void store(String reference, Object value);
}
