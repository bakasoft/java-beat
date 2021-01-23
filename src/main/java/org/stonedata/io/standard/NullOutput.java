package org.stonedata.io.standard;

import org.stonedata.io.CharOutput;

public class NullOutput implements CharOutput {
    @Override
    public void write(char value) {
        // do nothing
    }

    @Override
    public void write(CharSequence value) {
        // do nothing
    }

    @Override
    public void space() {
        // do nothing
    }

    @Override
    public void line() {
        // do nothing
    }

    @Override
    public void indent(int delta) {
        // do nothing
    }
}
