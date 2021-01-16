package org.stonedata.producers.impl;

import org.stonedata.errors.StoneException;
import org.stonedata.producers.ObjectProducer;
import org.stonedata.types.MapObject;

import java.lang.reflect.Type;

public class UntypedGenericObjectProducer implements ObjectProducer {

    public static final UntypedGenericObjectProducer INSTANCE = new UntypedGenericObjectProducer();

    private UntypedGenericObjectProducer() {}

    @Override
    public Object beginInstance() {
        return new MapObject();
    }

    @Override
    public void set(Object obj, String key, Object value) {
        if (obj instanceof MapObject) {
            ((MapObject) obj).set(key, value);
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
