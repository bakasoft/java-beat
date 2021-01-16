package org.stonedata.producers.impl;

import org.stonedata.types.GenericList;
import org.stonedata.errors.StoneException;
import org.stonedata.producers.ArrayProducer;
import org.stonedata.types.UntypedList;

import java.lang.reflect.Type;

public class UntypedGenericListProducer implements ArrayProducer {

    public static final UntypedGenericListProducer INSTANCE = new UntypedGenericListProducer();

    private UntypedGenericListProducer() {}

    @Override
    public Object beginInstance() {
        return new UntypedList();
    }

    @Override
    public void add(Object obj, Object item) {
        if (obj instanceof UntypedList) {
            ((UntypedList) obj).add(item);
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
