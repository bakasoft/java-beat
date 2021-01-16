package org.stonedata.repositories;

import org.stonedata.producers.ArrayProducer;
import org.stonedata.producers.ObjectProducer;
import org.stonedata.producers.ValueProducer;

import java.lang.reflect.Type;

public interface ProducerRepository {
    ObjectProducer getObjectProducer(String typeName, Type typeHint);

    ArrayProducer getArrayProducer(String typeName, Type typeHint);

    ValueProducer getValueProducer(String typeName, Type typeHint);
}
