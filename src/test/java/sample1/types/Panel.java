package sample1.types;

import java.util.List;

public class Panel implements Widget {

    private Layout layout;
    private List<Widget> elements;

    public List<Widget> getElements() {
        return elements;
    }

    public void setElements(List<Widget> elements) {
        this.elements = elements;
    }

    public Layout getLayout() {
        return layout;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }
}
