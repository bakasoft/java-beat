package org.stonedata.producers;

public interface ObjectProducer {
    Object beginInstance(String type);

    void set(Object instance, String key, Object value);

    // TODO add get type of a field so it can be defaulted to type-less values

    Object endInstance(Object instance);
}
