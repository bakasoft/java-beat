package org.stonedata.binary.input;

import org.stonedata.binary.schema.IndexedField;
import org.stonedata.binary.schema.IndexedType;

public interface ObjectCreator {
    IndexedType getTypeIndex();

    Object newInstance();

    IndexedField findField(int index);

    void setValue(Object object, IndexedField field, Value value);
}
