package org.stonedata.io.impl;

import org.stonedata.errors.StoneException;
import org.stonedata.io.StoneCharOutput;

import java.io.IOException;

public class AppendableOutput implements StoneCharOutput {

    private final Appendable output;

    public AppendableOutput(Appendable output) {
        this.output = output;
    }

    @Override
    public void write(char value) {
        try {
            output.append(value);
        }
        catch (IOException e) {
            throw new StoneException(e);
        }
    }

    @Override
    public void write(CharSequence value) {
        try {
            output.append(value);
        }
        catch (IOException e) {
            throw new StoneException(e);
        }
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
