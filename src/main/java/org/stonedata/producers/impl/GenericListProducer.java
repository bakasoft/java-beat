package org.stonedata.producers.impl;

import org.stonedata.util.GenericList;
import org.stonedata.errors.StoneException;
import org.stonedata.producers.ArrayProducer;

import java.lang.reflect.Type;

public class GenericListProducer implements ArrayProducer {

    public static final GenericListProducer INSTANCE = new GenericListProducer();

    private GenericListProducer() {}

    @Override
    public Object beginInstance(String type) {
        return new GenericList(type);
    }

    @Override
    public void add(Object obj, Object item) {
        if (obj instanceof GenericList) {
            ((GenericList) obj).add(item);
        }
        else {
            throw new StoneException();
        }
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
