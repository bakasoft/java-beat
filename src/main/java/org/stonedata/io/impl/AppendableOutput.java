package org.stonedata.io.impl;

import org.stonedata.io.StoneCharOutput;

import java.io.IOException;

public class AppendableOutput implements StoneCharOutput {

    private final Appendable output;

    public AppendableOutput(Appendable output) {
        this.output = output;
    }

    @Override
    public void write(char value) throws IOException {
        output.append(value);
    }

    @Override
    public void write(CharSequence value) throws IOException {
        output.append(value);
    }

    @Override
    public void space() throws IOException {
        // do nothing
    }

    @Override
    public void line() throws IOException {
        // do nothing
    }

    @Override
    public void indent(int delta) throws IOException {
        // do nothing
    }
}
