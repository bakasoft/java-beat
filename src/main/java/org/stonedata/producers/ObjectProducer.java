package org.stonedata.producers;

import java.lang.reflect.Type;

public interface ObjectProducer extends Producer {
    Object beginInstance();

    void set(Object instance, String key, Object value);

    Type getTypeHint(String key);

    Object endInstance(Object instance);
}
