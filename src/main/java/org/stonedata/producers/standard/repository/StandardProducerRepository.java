package org.stonedata.producers.standard.repository;

import org.stonedata.Stone;
import org.stonedata.errors.ProducerNotFoundException;
import org.stonedata.producers.ArrayProducer;
import org.stonedata.producers.ObjectProducer;
import org.stonedata.producers.ProducerRepository;
import org.stonedata.producers.ValueProducer;
import org.stonedata.producers.standard.StandardArrayProducers;
import org.stonedata.producers.standard.StandardObjectProducers;
import org.stonedata.producers.standard.StandardValueProducers;

import java.lang.reflect.Type;

public class StandardProducerRepository implements ProducerRepository {

    private final Stone stone;

    public StandardProducerRepository(Stone stone) {
        this.stone = stone;
    }

    @Override
    public ObjectProducer getObjectProducer(String typeName, Type typeHint) {
        var producer = stone.getProducer(typeName);
        if (producer != null) {
            if (producer instanceof ObjectProducer) {
                return (ObjectProducer) producer;
            } else {
                throw new ProducerNotFoundException(String.format(
                        "Producer %s is not an object producer.", typeName));
            }
        }

        var standardProducer = StandardObjectProducers.tryCreate(typeHint);
        if (standardProducer != null && typeName != null) {
            stone.registerProducer(typeName, standardProducer);
        }
        return standardProducer;
    }

    @Override
    public ArrayProducer getArrayProducer(String typeName, Type typeHint) {
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

        var standardProducer = StandardArrayProducers.tryCreate(typeHint);
        if (standardProducer != null && typeName != null) {
            stone.registerProducer(typeName, standardProducer);
        }
        return standardProducer;
    }

    @Override
    public ValueProducer getValueProducer(String typeName, Type typeHint) {
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

        var standardProducer = StandardValueProducers.tryCreate(typeHint);
        if (standardProducer != null && typeName != null) {
            stone.registerProducer(typeName, standardProducer);
        }
        return standardProducer;
    }

}
