package org.stonedata.formats.binary.input;

public interface CreatorRepository {
    ObjectCreator findCreatorFor(int typeIndex);

    default BinaryDeserializer createDeserializer() {
        return new BinaryDeserializer(this);
    }
}
