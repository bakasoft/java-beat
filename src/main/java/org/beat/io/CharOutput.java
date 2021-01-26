package org.beat.io;

public interface CharOutput {
    void write(char value);
    void write(CharSequence value);
    void space();
    void line();
    void indent(int delta);
}
