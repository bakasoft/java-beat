package org.beat.io;

import org.beat.errors.InvalidSyntaxException;
import org.beat.util.PP;

public interface CharInput {
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
