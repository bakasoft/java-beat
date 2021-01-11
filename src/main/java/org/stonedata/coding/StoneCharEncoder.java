package org.stonedata.coding;

import org.stonedata.io.StoneCharOutput;

import java.io.IOException;

public interface StoneCharEncoder {

    void write(Object value, StoneCharOutput output) throws IOException;


}
