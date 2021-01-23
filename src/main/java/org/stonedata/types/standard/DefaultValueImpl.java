package org.stonedata.types.standard;

import org.stonedata.types.DefaultValue;

import java.util.ArrayList;
import java.util.List;

public class DefaultValueImpl extends ArrayList<Object> implements DefaultValue {
    public DefaultValueImpl() {}
    public DefaultValueImpl(List<Object> arguments) {
        super(arguments);
    }
}
