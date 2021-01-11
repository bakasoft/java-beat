package org.stonedata.binary.output;

import org.stonedata.binary.schema.IndexedField;
import org.stonedata.binary.schema.IndexedType;

import java.util.Collection;

public interface ObjectDescriber {
    boolean accepts(Object any);

    IndexedType getTypeIndex();

    Collection<IndexedField> getFields();

    Object getValue(Object obj, IndexedField field);
}
