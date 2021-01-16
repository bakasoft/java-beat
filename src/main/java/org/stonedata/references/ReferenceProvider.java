package org.stonedata.references;

public interface ReferenceProvider {

    String getReference(String typeName, Object value);

}
