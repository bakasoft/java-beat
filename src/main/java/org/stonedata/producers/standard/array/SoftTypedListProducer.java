package org.stonedata.producers.standard.array;

import org.stonedata.errors.StoneException;
import org.stonedata.producers.ArrayProducer;
import org.stonedata.types.GenericList;

import java.lang.reflect.Type;

public class SoftTypedListProducer implements ArrayProducer {

    private final String type;

    public SoftTypedListProducer(String type) {
        this.type = type;
    }

    @Override
    public Object beginInstance() {
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
