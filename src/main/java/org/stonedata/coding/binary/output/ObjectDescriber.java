package org.stonedata.coding.binary.output;

import org.stonedata.coding.binary.schema.IndexedField;
import org.stonedata.coding.binary.schema.IndexedType;

import java.util.Collection;

public interface ObjectDescriber {
    boolean accepts(Object any);

    IndexedType getTypeIndex();

    Collection<IndexedField> getFields();

    Object getValue(Object obj, IndexedField field);
}
