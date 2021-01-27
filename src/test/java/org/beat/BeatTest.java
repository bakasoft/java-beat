package org.beat;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BeatTest {

    public static class Node {
        public String id;
    }

    @Test
    void testBuilderWithReferenceGenerator() {
        var node1 = new Node();
        var node2 = new Node();
        var beat = Beat.builder()
                .withReferenceGenerator(Node.class, node -> node.id)
                .build();

        node1.id = "1";
        node2.id = "2";

        var text = beat.writeText(List.of(node1, node2, node1, node2));

        assertEquals("[<1>{id:1},<2>{id:2},<1>,<2>]", text);
    }

    @Test
    void testBuilderWithHashReference() {
        var beat = Beat.builder()
                .withHashReference(String.class)
                .build();
        var text = beat.writeText(new String[]{"a", "b", "a", "b"});

        assertEquals("[<14dad5dc61>(a),<14dad5dc62>(b),<14dad5dc61>,<14dad5dc62>]", text);
    }

    @Test
    void testBuilderWithValueReference() {
        var obj1 = new Object();
        var obj2 = new Object();
        var beat = Beat.builder()
                .withValueReference(obj1, "1")
                .withValueReference(obj2, "2")
                .build();

        var text = beat.writeText(List.of(obj1, obj2, obj1, obj2));

        assertEquals("[<1>{},<2>{},<1>,<2>]", text);
    }

}
