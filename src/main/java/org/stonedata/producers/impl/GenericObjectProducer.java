package org.stonedata.producers.impl;

import org.stonedata.types.GenericObject;
import org.stonedata.errors.StoneException;
import org.stonedata.producers.ObjectProducer;

import java.lang.reflect.Type;

public class GenericObjectProducer implements ObjectProducer {

    public static final GenericObjectProducer INSTANCE = new GenericObjectProducer();

    private GenericObjectProducer() {}

    @Override
    public Object beginInstance(String type) {
        return new GenericObject(type);
    }

    @Override
    public void set(Object obj, String key, Object value) {
        if (obj instanceof GenericObject) {
            ((GenericObject) obj).set(key, value);
        }
        else {
            throw new StoneException();
        }
    }

    @Override
    public Type getTypeHint(String key) {
        return null;
    }

    @Override
    public Object endInstance(Object instance) {
        return instance;
    }
}
