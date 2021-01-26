package org.beat.io.standard;

import org.beat.errors.BeatException;
import org.beat.io.CharOutput;

import java.io.IOException;

public class AppendableOutput implements CharOutput {

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
            throw new BeatException(e);
        }
    }

    @Override
    public void write(CharSequence value) {
        try {
            output.append(value);
        }
        catch (IOException e) {
            throw new BeatException(e);
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
