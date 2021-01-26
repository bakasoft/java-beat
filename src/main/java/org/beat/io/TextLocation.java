package org.beat.io;

public class TextLocation {

    private final int line;
    private final int column;
    private final String resource;

    public TextLocation(int line, int column, String resource) {
        this.line = line;
        this.column = column;
        this.resource = resource;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    public String getResource() {
        return resource;
    }

}
