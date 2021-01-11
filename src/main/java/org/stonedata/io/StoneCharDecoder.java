package org.stonedata.io;

import java.io.IOException;

public interface StoneCharDecoder {

    Object read(StoneCharInput input) throws IOException;

}
