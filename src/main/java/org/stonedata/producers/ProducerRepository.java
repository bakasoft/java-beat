package org.stonedata.producers;

import java.lang.reflect.Type;

public interface ProducerRepository {
    ObjectProducer findObjectProducer(String typeName, Type typeHint);

    ArrayProducer findArrayProducer(String typeName, Type typeHint);

    ValueProducer findValueProducer(String typeName, Type typeHint);
}
