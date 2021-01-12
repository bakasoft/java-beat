package org.stonedata.producers.impl;

import org.stonedata.errors.StoneException;
import org.stonedata.producers.ArrayProducer;
import org.stonedata.producers.ObjectProducer;
import org.stonedata.producers.ProducerRepository;
import org.stonedata.producers.ValueProducer;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

public class StandardProducerRepository implements ProducerRepository {

    private final Map<String, Object> makers;

    private ObjectProducer genericObjectProducer;
    private ArrayProducer genericArrayProducer;
    private ValueProducer genericValueProducer;

    public StandardProducerRepository() {
        this.makers = new LinkedHashMap<>();
        this.genericObjectProducer = new GenericObjectProducer();
        this.genericArrayProducer = new GenericListProducer();
        this.genericValueProducer = new GenericValueProducer();
    }

    public void addObjectMakerFor(Class<?> type) {
        addObjectMakerFor(type, type.getSimpleName());
    }

    public void addObjectMakerFor(Class<?> type, String name) {
        makers.put(name, new ClassObjectProducer(type));
    }

    private void addMakerG(String type, Object maker) {
        if (makers.containsKey(type)) {
            throw new RuntimeException();
        }
        makers.put(type, maker);
    }

    private <T> T findMakerG(Class<T> typeClass, String typeName, T defaultValue, Type typeHint) {
        var maker = makers.get(typeName);

        // TODO consider typeHint

        if (maker != null) {
            if (typeClass.isInstance(maker)) {
                return typeClass.cast(maker);
            }
            else {
                throw new StoneException();
            }
        }
        else if (defaultValue == null) {
            throw new StoneException("No maker for " + typeName);
        }
        else {
            return defaultValue;
        }
    }

    public void addMaker(String type, ObjectProducer maker) {
        addMakerG(type, maker);
    }

    public void addMaker(String type, ArrayProducer maker) {
        addMakerG(type, maker);
    }

    public void addMaker(String type, ValueProducer maker) {
        addMakerG(type, maker);
    }

    @Override
    public ObjectProducer findObjectProducer(String type, Type typeHint) {
        return findMakerG(ObjectProducer.class, type, genericObjectProducer, typeHint);
    }

    @Override
    public ArrayProducer findArrayProducer(String type, Type typeHint) {
        return findMakerG(ArrayProducer.class, type, genericArrayProducer, typeHint);
    }

    @Override
    public ValueProducer findValueProducer(String type, Type typeHint) {
        return findMakerG(ValueProducer.class, type, genericValueProducer, typeHint);
    }

    public ObjectProducer getGenericObjectMaker() {
        return genericObjectProducer;
    }

    public void setGenericObjectMaker(ObjectProducer genericObjectProducer) {
        this.genericObjectProducer = genericObjectProducer;
    }

    public ArrayProducer getGenericArrayMaker() {
        return genericArrayProducer;
    }

    public void setGenericArrayMaker(ArrayProducer genericArrayProducer) {
        this.genericArrayProducer = genericArrayProducer;
    }

    public ValueProducer getGenericValueMaker() {
        return genericValueProducer;
    }

    public void setGenericValueMaker(ValueProducer genericValueProducer) {
        this.genericValueProducer = genericValueProducer;
    }
}
