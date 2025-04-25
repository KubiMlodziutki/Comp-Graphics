package plakat.canvas;

import plakat.PosterElement;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ElementStore {

    private final List<PosterElement> elements = new ArrayList<>();
    private PosterElement selected;

    public void add(PosterElement posElem) {
        elements.add(posElem);
        selected = posElem;
    }

    public void toFront() {
        if (selected != null && elements.remove(selected)) {
            elements.add(selected);
        }
    }

    public void toBack() {
        if (selected != null && elements.remove(selected)) {
            elements.add(0, selected);
        }
    }

    public void delete() {
        if (selected != null) {
            elements.remove(selected);
            selected = null;
        }
    }

    public void clearSelection() {
        selected = null;
    }

    public List<PosterElement> all() {
        return Collections.unmodifiableList(elements);
    }

    public PosterElement getSelected() {
        return selected;
    }

    public void setAll(List<PosterElement> list) {
        elements.clear();
        elements.addAll(list);
        selected = null;
    }

    public boolean pick(Point2D p) {
        selected = null;
        for (PosterElement posElem : new ArrayList<>(elements)) {
            if (posElem.hit(p)) selected = posElem;
        }
        return selected != null;
    }
}