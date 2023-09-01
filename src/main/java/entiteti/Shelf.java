package entiteti;

import java.util.ArrayList;
import java.util.List;

public class Shelf<T> {
    List<T> boxesOf = new ArrayList<>(0);

    public Shelf(List<T> boxesOf) {
        this.boxesOf = boxesOf;
    }

    public List<T> getBoxesOf() {
        return boxesOf;
    }

    public void setBoxesOf(List<T> boxesOf) {
        this.boxesOf = boxesOf;
    }

    @Override
    public String toString() {
        return "Shelf{" +
                "boxesOf=" + boxesOf +
                '}';
    }
}
