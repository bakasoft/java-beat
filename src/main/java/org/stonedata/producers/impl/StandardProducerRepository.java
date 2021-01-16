package org.stonedata.producers.impl;

import org.stonedata.Stone;
import org.stonedata.errors.ProducerNotFoundException;
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
    public ObjectProducer findObjectProducer(String typeName, Type typeHint) {
        var producer = stone.getProducer(typeName);
        if (producer != null) {
            if (producer instanceof ObjectProducer) {
                return (ObjectProducer) producer;
            } else {
                throw new ProducerNotFoundException(String.format(
                        "Producer %s is not an object producer.", typeName));
            }
        }

        var standardProducer = StandardObjectProducers.create(typeHint, typeName);
        if (typeName != null) {
            stone.registerProducer(typeName, standardProducer);
        }
        return standardProducer;
    }

    @Override
    public ArrayProducer findArrayProducer(String typeName, Type typeHint) {
        var producer = stone.getProducer(typeName);
        if (producer != null) {
            if (producer instanceof ArrayProducer) {
                return (ArrayProducer) producer;
            }
            else {
                throw new ProducerNotFoundException(String.format(
                        "Producer %s is not an array producer.", typeName));
            }
        }

        var standardProducer = StandardArrayProducers.create(typeHint, typeName);
        if (typeName != null) {
            stone.registerProducer(typeName, standardProducer);
        }
        return standardProducer;
    }

    @Override
    public ValueProducer findValueProducer(String typeName, Type typeHint) {
        var producer = stone.getProducer(typeName);
        if (producer != null) {
            if (producer instanceof ValueProducer) {
                return (ValueProducer) producer;
            }
            else {
                throw new ProducerNotFoundException(String.format(
                        "Producer %s is not a value producer.", typeName));
            }
        }

        var standardProducer = StandardValueProducers.create(typeHint, typeName);
        if (typeName != null) {
            stone.registerProducer(typeName, standardProducer);
        }
        return standardProducer;
    }

}
