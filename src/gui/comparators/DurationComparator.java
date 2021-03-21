package gui.comparators;

import gui.MediaContentBean;

import java.util.Comparator;

public class DurationComparator implements Comparator<MediaContentBean> {
    public int compare( MediaContentBean a, MediaContentBean b ) {
        if (a != null && b != null) {
            return a.src.getLength().compareTo(b.src.getLength());
        }
        return 0;
    }

    public String toString() {
        return "Duration";
    }
}