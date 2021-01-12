package org.stonedata.producers;

import java.lang.reflect.Type;

public interface ArrayProducer extends Producer {
    Object beginInstance(String type);

    void add(Object instance, Object item);

    Type getComponentTypeHint();

    Object endInstance(Object instance);
}
