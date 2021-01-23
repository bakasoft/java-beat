package org.stonedata.formats.binary.output;

import org.stonedata.formats.binary.schema.IndexedField;
import org.stonedata.formats.binary.schema.IndexedType;

import java.util.Collection;

public interface ObjectDescriber {
    boolean accepts(Object any);

    IndexedType getTypeIndex();

    Collection<IndexedField> getFields();

    Object getValue(Object obj, IndexedField field);
}
