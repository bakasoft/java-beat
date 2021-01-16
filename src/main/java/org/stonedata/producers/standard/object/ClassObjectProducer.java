package org.stonedata.producers.standard.object;

import org.stonedata.errors.ConversionException;
import org.stonedata.producers.ObjectProducer;
import org.stonedata.util.ReflectUtils;
import org.stonedata.errors.StoneException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class ClassObjectProducer implements ObjectProducer {

    private final Class<?> type;
    private final Supplier<Object> maker;
    private final Map<String, BiConsumer<Object, Object>> setters;
    private final Map<String, Type> typeHints;

    public ClassObjectProducer(Class<?> type) {
        this.type = type;
        this.typeHints = new HashMap<>();
        this.setters = generateSetters(type, typeHints);
        this.maker = generateMaker(type);
    }

    private Supplier<Object> generateMaker(Class<?> type) {
        Constructor<?> ctr;

        try {
            ctr = type.getConstructor();
        }
        catch (NoSuchMethodException e) {
            throw new StoneException(e);
        }

        return () -> {
            try {
                return ctr.newInstance();
            }
            catch (InstantiationException e) {
                throw new StoneException(e);
            }
            catch (IllegalAccessException e) {
                throw new StoneException(e);
            }
            catch (InvocationTargetException e) {
                throw new StoneException(e.getTargetException());
            }
        };
    }

    private static Map<String, BiConsumer<Object, Object>> generateSetters(Class<?> type, Map<String, Type> typeHints) {
        var setters = new LinkedHashMap<String, BiConsumer<Object, Object>>();

        for (var field : type.getFields()) {
            var name = field.getName();
            var dataType = field.getType();
            var dataTypeHint = field.getGenericType();

            typeHints.put(name, dataTypeHint);
            setters.put(name, (obj, value) -> {
                try {
                    field.set(obj, ReflectUtils.convertTo(value, dataType));
                }
                catch (IllegalAccessException e) {
                    throw new StoneException(e);
                }
            });
        }

        for (var method : type.getMethods()) {
            var name = method.getName();
            if (name.startsWith("set") && method.getParameterCount() == 1) {
                name = name.substring(3, 4).toLowerCase() + name.substring(4);
                var dataType = method.getParameters()[0].getType();
                var dataTypeHint = method.getParameters()[0].getParameterizedType();  // TODO check if this is OK
                typeHints.put(name, dataTypeHint);
                setters.put(name, (obj, value) -> {
                    try {
                        method.invoke(obj, ReflectUtils.convertTo(value, dataType));
                    } catch (IllegalAccessException e) {
                        throw new StoneException(e);
                    } catch (InvocationTargetException e) {
                        throw new StoneException(e.getTargetException());
                    }
                });
            }
        }

        return setters;
    }

    @Override
    public Object beginInstance() {
        return maker.get();
    }

    @Override
    public void set(Object obj, String key, Object value) {
        var setter = setters.get(key);

        if (setter == null) {
            throw new StoneException("not setter for " + type.getName() + "[" + key + "]");
        }

        try {
            setter.accept(obj, value);
        }
        catch (ConversionException e) {
            throw new StoneException(obj.getClass().getSimpleName() + "[" + key + "]: " + e.getMessage(), e);
        }
    }

    @Override
    public Type getTypeHint(String key) {
        return typeHints.get(key);
    }

    @Override
    public Object endInstance(Object instance) {
        return type.cast(instance);
    }
}
