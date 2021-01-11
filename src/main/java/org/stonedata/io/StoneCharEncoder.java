package org.stonedata.io;

import java.io.IOException;

public interface StoneCharEncoder {

    void write(Object value, StoneCharOutput output) throws IOException;


}
