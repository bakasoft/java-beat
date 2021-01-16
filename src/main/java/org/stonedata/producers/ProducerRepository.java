package org.stonedata.producers;

import java.lang.reflect.Type;

public interface ProducerRepository {
    ObjectProducer getObjectProducer(String typeName, Type typeHint);

    ArrayProducer getArrayProducer(String typeName, Type typeHint);

    ValueProducer getValueProducer(String typeName, Type typeHint);
}
