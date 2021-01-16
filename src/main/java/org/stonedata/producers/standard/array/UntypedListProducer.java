package org.stonedata.producers.standard.array;

import org.stonedata.errors.StoneException;
import org.stonedata.producers.ArrayProducer;
import org.stonedata.types.array.UntypedList;

import java.lang.reflect.Type;

public class UntypedListProducer implements ArrayProducer {

    public static final UntypedListProducer INSTANCE = new UntypedListProducer();

    private UntypedListProducer() {}

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
