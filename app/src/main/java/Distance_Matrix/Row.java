package Distance_Matrix;

import java.util.List;

public class Row {

    private List<Element> elements = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public Row() {
    }

    /**
     *
     * @param elements
     */
    public Row(List<Element> elements) {
        super();
        this.elements = elements;
    }

    public List<Element> getElements() {
        return elements;
    }

    public void setElements(List<Element> elements) {
        this.elements = elements;
    }

    public Row withElements(List<Element> elements) {
        this.elements = elements;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Row.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("elements");
        sb.append('=');
        sb.append(((this.elements == null)?"<null>":this.elements));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
