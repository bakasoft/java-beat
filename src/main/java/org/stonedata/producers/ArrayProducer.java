package org.stonedata.producers;

public interface ArrayProducer {
    Object newInstance(String type);

    void add(Object obj, Object item);

    // build()
}
