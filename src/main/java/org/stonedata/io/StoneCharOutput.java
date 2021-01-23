package org.stonedata.io;

public interface StoneCharOutput {
    void write(char value);
    void write(CharSequence value);
    void space();
    void line();
    void indent(int delta);
}
