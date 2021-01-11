package org.stonedata.coding.binary.input.impl;

import org.stonedata.coding.binary.input.CreatorRepository;
import org.stonedata.coding.binary.input.ObjectCreator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DefaultCreatorRepository implements CreatorRepository {

    private final List<ObjectCreator> creators;

    public DefaultCreatorRepository() {
        this.creators = new ArrayList<>();
    }

    public void addCreator(ObjectCreator creator) {
        creators.add(creator);
    }

    public List<ObjectCreator> getCreators() {
        return Collections.unmodifiableList(creators);
    }

    public ObjectCreator findCreatorFor(int typeIndex) {
        for (var creator : creators) {
            if (creator.getTypeIndex().getIndex() == typeIndex) {
                return creator;
            }
        }

        throw new RuntimeException("No creator for: " + typeIndex);
    }

}
