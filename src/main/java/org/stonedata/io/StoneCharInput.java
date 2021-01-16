package org.stonedata.io;

import org.stonedata.errors.InvalidSyntaxException;
import org.stonedata.util.PP;

import java.io.IOException;

public interface StoneCharInput {
    boolean isAlive() throws IOException;
    char pull() throws IOException;
    char peek() throws IOException;

    TextLocation getLocation();

    default boolean peek(char c) throws IOException{
        return peek() == c;
    }

    default boolean tryPull(char c) throws IOException {
        if (peek() == c) {
            pull();
            return true;
        }
        return false;
    }

    default void expect(char expected) throws IOException {
        var actual = peek();
        if (actual != expected) {
            throw new InvalidSyntaxException(getLocation(), "Expected char " + PP.str(expected) + " instead of " + PP.str(actual) + ".");
        }
        pull();
    }
}
