package org.stonedata.io.impl;

import org.stonedata.io.StoneCharOutput;

public class NullOutput implements StoneCharOutput {
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
