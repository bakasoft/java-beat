package org.stonedata.repositories;

import org.stonedata.producers.ArrayProducer;
import org.stonedata.producers.ObjectProducer;
import org.stonedata.producers.ValueProducer;

import java.lang.reflect.Type;

public interface ProducerRepository {
    ObjectProducer getObjectProducer(String typeName);

    ObjectProducer getObjectProducer(Type typeHint);

    ArrayProducer getArrayProducer(String typeName);

    ArrayProducer getArrayProducer(Type typeHint);

    ValueProducer getValueProducer(String typeName);

    ValueProducer getValueProducer(Type typeHint);
}
