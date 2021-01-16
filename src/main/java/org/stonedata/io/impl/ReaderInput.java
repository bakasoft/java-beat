package org.stonedata.io.impl;

import org.stonedata.io.StoneCharInput;
import org.stonedata.io.TextLocation;

import java.io.IOException;
import java.io.Reader;

public class ReaderInput implements StoneCharInput {

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

    private Character load() throws IOException {
        if (buffer == null) {
            var c = reader.read();

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
    public boolean isAlive() throws IOException {
        return load() != null;
    }

    @Override
    public char pull() throws IOException {
        var c = load();

        if (c == null) {
            throw new IOException();
        }

        buffer = null;

        return c;
    }

    @Override
    public char peek() throws IOException {
        var c = load();

        if (c == null) {
            throw new IOException();
        }

        return c;
    }

    @Override
    public TextLocation getLocation() {
        return new TextLocation(line, column, resource);
    }

}
