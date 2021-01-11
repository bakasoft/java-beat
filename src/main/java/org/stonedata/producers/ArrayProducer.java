package org.stonedata.producers;

public interface ArrayProducer {
    Object beginInstance(String type);

    void add(Object instance, Object item);

    Object endInstance(Object instance);
}
