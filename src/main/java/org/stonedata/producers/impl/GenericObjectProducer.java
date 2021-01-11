package org.stonedata.producers.impl;

import org.stonedata.util.GenericObject;
import org.stonedata.errors.StoneException;
import org.stonedata.producers.ObjectProducer;

public class GenericObjectProducer implements ObjectProducer {

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
    public Object endInstance(Object instance) {
        return instance;
    }
}
