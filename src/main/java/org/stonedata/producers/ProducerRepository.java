package org.stonedata.producers;

import java.lang.reflect.Type;

public interface ProducerRepository {
    ObjectProducer findObjectProducer(String type, Type typeHint);

    ArrayProducer findArrayProducer(String type, Type typeHint);

    ValueProducer findValueProducer(String type, Type typeHint);
}
