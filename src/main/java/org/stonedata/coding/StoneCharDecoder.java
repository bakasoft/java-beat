package org.stonedata.coding;

import org.stonedata.io.StoneCharInput;

import java.io.IOException;

public interface StoneCharDecoder {

    Object read(StoneCharInput input) throws IOException;

}
