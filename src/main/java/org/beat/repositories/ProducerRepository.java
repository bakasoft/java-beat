package org.beat.repositories;

import org.beat.producers.ArrayProducer;
import org.beat.producers.ObjectProducer;
import org.beat.producers.ValueProducer;

import java.lang.reflect.Type;

public interface ProducerRepository {
    ObjectProducer getObjectProducer(String typeName);

    ObjectProducer getObjectProducer(Type typeHint);

    ArrayProducer getArrayProducer(String typeName);

    ArrayProducer getArrayProducer(Type typeHint);

    ValueProducer getValueProducer(String typeName);

    ValueProducer getValueProducer(Type typeHint);
}
