package org.stonedata.formats.binary.input;

import org.stonedata.formats.binary.schema.IndexedField;
import org.stonedata.formats.binary.schema.IndexedType;

public interface ObjectCreator {
    IndexedType getTypeIndex();

    Object newInstance();

    IndexedField findField(int index);

    void setValue(Object object, IndexedField field, Value value);
}
