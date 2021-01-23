package org.stonedata.repositories.standard;

import org.stonedata.errors.ProducerNotFoundException;
import org.stonedata.producers.ArrayProducer;
import org.stonedata.producers.ObjectProducer;
import org.stonedata.producers.Producer;
import org.stonedata.producers.ValueProducer;
import org.stonedata.producers.standard.StandardArrayProducers;
import org.stonedata.producers.standard.StandardObjectProducers;
import org.stonedata.producers.standard.StandardValueProducers;
import org.stonedata.repositories.ProducerRepository;
import org.stonedata.util.PP;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class StandardProducerRepository implements ProducerRepository {

    private Map<String, Producer> nameProducers;
    private Map<Type, Producer> typeProducers;

    private <T extends Producer> T getProducerByName(String typeName, Class<T> producerClass) {
        if (nameProducers == null) {
            return null;
        }
        var producer = nameProducers.get(typeName);
        if (producer == null) {
            return null;
        }
        else if (producerClass.isInstance(producer)) {
            return producerClass.cast(producer);
        }
        else {
            throw new ProducerNotFoundException(String.format(
                    "Producer %s is not a %s.", PP.str(typeName), PP.type(producerClass)));
        }
    }

    private <T extends Producer> T getProducerByHint(Type typeHint, Class<T> producerClass) {
        if (typeProducers == null) {
            return null;
        }
        var producer = typeProducers.get(typeHint);
        if (producer == null) {
            return null;
        }
        else if (producerClass.isInstance(producer)) {
            return producerClass.cast(producer);
        }
        else {
            throw new ProducerNotFoundException(String.format(
                    "Producer %s is not a %s.", PP.str(typeHint), PP.type(producerClass)));
        }
    }

    public void register(String name, Producer producer) {
        Objects.requireNonNull(producer);
        Objects.requireNonNull(name);
        if (nameProducers == null) {
            nameProducers = new HashMap<>();
        }
        else if (nameProducers.containsKey(name)) {
            throw new RuntimeException();
        }
        nameProducers.put(name, producer);
    }

    public void register(Type type, Producer producer) {
        Objects.requireNonNull(producer);
        Objects.requireNonNull(type);
        if (typeProducers == null) {
            typeProducers = new HashMap<>();
        }
        else if (typeProducers.containsKey(type)) {
            throw new RuntimeException();
        }
        typeProducers.put(type, producer);
    }

    public boolean contains(String name) {
        return nameProducers != null && nameProducers.containsKey(name);
    }

    public boolean contains(Type type) {
        return typeProducers != null && typeProducers.containsKey(type);
    }

    @Override
    public ObjectProducer getObjectProducer(String typeName) {
        return getProducerByName(typeName, ObjectProducer.class);
    }

    @Override
    public ObjectProducer getObjectProducer(Type typeHint) {
        return getProducerByHint(typeHint, ObjectProducer.class);
    }

    @Override
    public ArrayProducer getArrayProducer(String typeName) {
        return getProducerByName(typeName, ArrayProducer.class);
    }

    @Override
    public ArrayProducer getArrayProducer(Type typeHint) {
        return getProducerByHint(typeHint, ArrayProducer.class);
    }

    @Override
    public ValueProducer getValueProducer(String typeName) {
        return getProducerByName(typeName, ValueProducer.class);
    }

    @Override
    public ValueProducer getValueProducer(Type typeHint) {
        return getProducerByHint(typeHint, ValueProducer.class);
    }

}
