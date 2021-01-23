package org.stonedata.producers.standard.array;

import org.stonedata.producers.ArrayProducer;
import org.stonedata.types.DefaultList;
import org.stonedata.types.standard.DefaultListImpl;

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
