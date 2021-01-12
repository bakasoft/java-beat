package org.stonedata.producers.impl;

import org.stonedata.Stone;
import org.stonedata.errors.ProducerNotFoundException;
import org.stonedata.errors.StoneException;
import org.stonedata.producers.ArrayProducer;
import org.stonedata.producers.ObjectProducer;
import org.stonedata.producers.ProducerRepository;
import org.stonedata.producers.ValueProducer;

import java.lang.reflect.Type;

public class StandardProducerRepository implements ProducerRepository {

    private final Stone stone;

    public StandardProducerRepository(Stone stone) {
        this.stone = stone;
    }

    @Override
    public ObjectProducer findObjectProducer(String name, Type typeHint) {
        var producer = stone.getProducer(name);

        if (producer != null) {
            if (producer instanceof ObjectProducer) {
                return (ObjectProducer) producer;
            }
            else {
                throw new ProducerNotFoundException(String.format(
                        "Producer %s is not an object producer.", name));
            }
        }

        Type type = stone.getType(name);

        if (type == null) {
            type = typeHint;
        }

        var defaultProducer = DefaultProducers.tryCreateObjectProducer(type);

        if (defaultProducer != null) {
            if (name != null) {
                stone.putProducer(name, defaultProducer);
            }

            return defaultProducer;
        }

        return GenericObjectProducer.INSTANCE;
    }

    @Override
    public ArrayProducer findArrayProducer(String name, Type typeHint) {
        var producer = stone.getProducer(name);

        if (producer != null) {
            if (producer instanceof ArrayProducer) {
                return (ArrayProducer) producer;
            }
            else {
                throw new ProducerNotFoundException(String.format(
                        "Producer %s is not an array producer.", name));
            }
        }

        Type type = stone.getType(name);

        if (type == null) {
            type = typeHint;
        }

        var defaultProducer = DefaultProducers.tryCreateArrayProducer(type);

        if (defaultProducer != null) {
            if (name != null) {
                stone.putProducer(name, defaultProducer);
            }

            return defaultProducer;
        }

        return GenericListProducer.INSTANCE;
    }

    @Override
    public ValueProducer findValueProducer(String name, Type typeHint) {
        var producer = stone.getProducer(name);

        if (producer != null) {
            if (producer instanceof ValueProducer) {
                return (ValueProducer) producer;
            }
            else {
                throw new ProducerNotFoundException(String.format(
                        "Producer %s is not a value producer.", name));
            }
        }

        Type type = stone.getType(name);

        if (type == null) {
            type = typeHint;
        }

        var defaultProducer = DefaultProducers.tryCreateValueProducer(type);

        if (defaultProducer != null) {
            if (name != null) {
                stone.putProducer(name, defaultProducer);
            }

            return defaultProducer;
        }

        return GenericValueProducer.INSTANCE;
    }

}
