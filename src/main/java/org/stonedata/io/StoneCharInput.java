package org.stonedata.io;

import org.stonedata.errors.InvalidSyntaxException;
import org.stonedata.util.PP;

public interface StoneCharInput {
    boolean isAlive();
    char pull();
    char peek();

    TextLocation getLocation();

    default boolean peek(char c) {
        return peek() == c;
    }

    default boolean tryPull(char c)  {
        if (peek() == c) {
            pull();
            return true;
        }
        return false;
    }

    default void expect(char expected)  {
        var actual = peek();
        if (actual != expected) {
            throw new InvalidSyntaxException("Expected char " + PP.str(expected) + " instead of " + PP.str(actual) + ".", getLocation());
        }
        pull();
    }
}
