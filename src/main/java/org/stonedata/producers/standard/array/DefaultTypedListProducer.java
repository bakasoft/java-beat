package org.stonedata.producers.standard.array;

import org.stonedata.producers.ArrayProducer;
import org.stonedata.types.array.DefaultList;
import org.stonedata.types.array.DefaultTypedListImpl;

import java.lang.reflect.Type;

public class DefaultTypedListProducer implements ArrayProducer {

    private final String typeName;

    public DefaultTypedListProducer(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public Object beginInstance() {
        return new DefaultTypedListImpl(typeName);
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
