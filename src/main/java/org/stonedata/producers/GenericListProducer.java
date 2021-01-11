package org.stonedata.producers;

import org.stonedata.common.GenericList;
import org.stonedata.errors.StoneException;

public class GenericListProducer implements ArrayProducer {

    @Override
    public Object newInstance(String type) {
        return new GenericList(type);
    }

    @Override
    public void add(Object obj, Object item) {
        if (obj instanceof GenericList) {
            ((GenericList) obj).add(item);
        }
        else {
            throw new StoneException();
        }
    }

}
