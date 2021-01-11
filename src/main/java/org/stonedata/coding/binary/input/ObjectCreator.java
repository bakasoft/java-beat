package org.stonedata.coding.binary.input;

import org.stonedata.coding.binary.schema.IndexedField;
import org.stonedata.coding.binary.schema.IndexedType;

public interface ObjectCreator {
    IndexedType getTypeIndex();

    Object newInstance();

    IndexedField findField(int index);

    void setValue(Object object, IndexedField field, Value value);
}
