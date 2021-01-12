package org.stonedata.producers.impl;

import org.stonedata.errors.StoneException;
import org.stonedata.producers.ArrayProducer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TypedListProducer implements ArrayProducer {

    private final Class<?> componentType;

    public TypedListProducer(Class<?> componentType) {
        this.componentType = componentType;
    }

    @Override
    public Object beginInstance(String type) {
        return new ArrayList<>();
    }

    @Override
    public void add(Object instance, Object item) {
        var list = recoverList(instance);
        if (item == null) {
            if (componentType.isPrimitive()) {
                throw new StoneException();
            }
            list.add(null);
        }
        else if (componentType.isInstance(item)) {
            forcedAdd(list, item);
        }
        else {
            throw new StoneException();
        }
    }

    @Override
    public Type getComponentTypeHint() {
        return componentType;
    }

    @Override
    public Object endInstance(Object instance) {
        return recoverList(instance);
    }

    private static ArrayList<?> recoverList(Object instance) {
        if (instance instanceof ArrayList) {
            return (ArrayList<?>)instance;
        }
        else {
            throw new StoneException();
        }
    }

    @SuppressWarnings("unchecked")
    private static void forcedAdd(List<?> list, Object item) {
        var unsafeList = (List<Object>)list;

        unsafeList.add(item);
    }
}
