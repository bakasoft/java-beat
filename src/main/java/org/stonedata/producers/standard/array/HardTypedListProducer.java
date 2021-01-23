package org.stonedata.producers.standard.array;

import org.stonedata.errors.StoneException;
import org.stonedata.errors.UnsupportedValueException;
import org.stonedata.producers.ArrayProducer;
import org.stonedata.util.PP;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HardTypedListProducer<T> implements ArrayProducer {

    private final Class<T> componentType;

    public HardTypedListProducer(Class<T> componentType) {
        this.componentType = componentType;
    }

    @Override
    public Object beginInstance() {
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
            throw new UnsupportedValueException(
                    String.format("Expected an instance of %s instead of %s.", PP.type(componentType), PP.typeOf(item)));
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
    public static void forcedAdd(List<?> list, Object item) {
        var unsafeList = (List<Object>)list;

        unsafeList.add(item);
    }
}
