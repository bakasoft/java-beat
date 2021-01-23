package org.stonedata.io.standard;

import org.stonedata.errors.StoneException;
import org.stonedata.io.CharOutput;

import java.io.IOException;

public class PrettyPrintOutput implements CharOutput {

    private final Appendable output;

    private int tabs;

    public PrettyPrintOutput(Appendable output) {
        this.output = output;
        this.tabs = 0;
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
        try {
            output.append(' ');
        }
        catch (IOException e) {
            throw new StoneException(e);
        }
    }

    @Override
    public void line() {
        try {
            output.append('\n');

            output.append("  ".repeat(tabs));
        }
        catch (IOException e) {
            throw new StoneException(e);
        }
    }

    @Override
    public void indent(int delta) {
        tabs += delta;
    }
}
