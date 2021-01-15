package sample2.types;

import java.util.List;

abstract public class Node {

    private List<Node> children;

    public List<Node> getChildren() {
        return children;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }
}
