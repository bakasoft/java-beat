package org.beat.producers.standard.array;

import org.beat.producers.ArrayProducer;
import org.beat.types.DefaultList;
import org.beat.types.standard.DefaultListImpl;

import java.lang.reflect.Type;

public class DefaultListProducer implements ArrayProducer {
    @Override
    public Object beginInstance() {
        return new DefaultListImpl();
    }

    @Override
    public void add(Object instance, Object item) {
        ((DefaultList)instance).add(item);
    }

    @Override
    public Type getComponentTypeHint() {
        return null;
    }

    @Override
    public Object endInstance(Object instance) {
        return instance;
    }
}
