package org.stonedata.repositories.standard;

import org.stonedata.producers.ArrayProducer;
import org.stonedata.producers.ObjectProducer;
import org.stonedata.producers.ValueProducer;
import org.stonedata.repositories.ProducerRepository;

import java.lang.reflect.Type;

public class NullProducerRepository implements ProducerRepository {
    public static final NullProducerRepository INSTANCE = new NullProducerRepository();

    private NullProducerRepository() {}

    @Override
    public ObjectProducer getObjectProducer(String typeName) {
        return null;
    }

    @Override
    public ObjectProducer getObjectProducer(Type typeHint) {
        return null;
    }

    @Override
    public ArrayProducer getArrayProducer(String typeName) {
        return null;
    }

    @Override
    public ArrayProducer getArrayProducer(Type typeHint) {
        return null;
    }

    @Override
    public ValueProducer getValueProducer(String typeName) {
        return null;
    }

    @Override
    public ValueProducer getValueProducer(Type typeHint) {
        return null;
    }
}
