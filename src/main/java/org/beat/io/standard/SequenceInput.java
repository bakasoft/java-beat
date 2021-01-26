package org.beat.io.standard;

import org.beat.io.CharInput;
import org.beat.io.TextLocation;

public class SequenceInput implements CharInput {

    private final CharSequence sequence;
    private final String resource;

    private int position;
    private int line;
    private int column;

    public SequenceInput(CharSequence sequence) {
        this(sequence, null);
    }

    public SequenceInput(CharSequence sequence, String resource) {
        this.sequence = sequence;
        this.resource = resource;
        this.position = 0;
    }

    @Override
    public boolean isAlive() {
        return position < sequence.length();
    }

    @Override
    public char pull() {
        if (position < sequence.length()) {
            var c = sequence.charAt(position);

            if (c == '\n') {
                line++;
                column = 0;
            }
            else {
                column++;
            }

            position++;

            return c;
        }

        return '\0';
    }

    @Override
    public char peek() {
        if (position < sequence.length()) {
            return sequence.charAt(position);
        }

        return '\0';
    }

    @Override
    public TextLocation getLocation() {
        return new TextLocation(line + 1, column + 1, resource);
    }

}
