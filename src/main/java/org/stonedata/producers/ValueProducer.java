package org.stonedata.producers;

import java.util.List;

public interface ValueProducer {
    Object newInstance(String type, List<?> arguments);
}