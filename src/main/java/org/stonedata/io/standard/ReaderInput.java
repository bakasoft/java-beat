package org.stonedata.io.standard;

import org.stonedata.errors.StoneException;
import org.stonedata.io.CharInput;
import org.stonedata.io.TextLocation;

import java.io.IOException;
import java.io.Reader;

public class ReaderInput implements CharInput {

    private final String resource;
    private final Reader reader;

    private Character buffer;
    private int line;
    private int column;

    public ReaderInput(Reader reader) {
        this(reader, null);
    }

    public ReaderInput(Reader reader, String resource) {
        this.resource = resource;
        this.reader = reader;
    }

    private Character load() {
        if (buffer == null) {
            int c;

            try {
                c = reader.read();
            }
            catch (IOException e) {
                throw new StoneException(e);
            }

            if (c == -1) {
                return null;
            }
            else if (c == '\n') {
                line++;
                column = 0;
            }
            else {
                column++;
            }

            buffer = (char)c;
        }

        return buffer;
    }

    @Override
    public boolean isAlive() {
        return load() != null;
    }

    @Override
    public char pull() {
        var c = load();

        if (c == null) {
            throw new StoneException();
        }

        buffer = null;

        return c;
    }

    @Override
    public char peek() {
        var c = load();

        if (c == null) {
            throw new StoneException();
        }

        return c;
    }

    @Override
    public TextLocation getLocation() {
        return new TextLocation(line, column, resource);
    }

}
