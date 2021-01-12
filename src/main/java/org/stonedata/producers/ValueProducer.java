package org.stonedata.producers;

import java.util.List;

public interface ValueProducer extends Producer {
    Object newInstance(String type, List<?> arguments);
}
