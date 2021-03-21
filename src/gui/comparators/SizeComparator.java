package gui.comparators;

import gui.MediaContentBean;

import java.util.Comparator;

public class SizeComparator implements Comparator<MediaContentBean> {
    public int compare( MediaContentBean a, MediaContentBean b ) {
        if (a != null && b != null) {
            return a.src.getSize().compareTo(b.src.getSize());
        }
        return 0;
    }

    public String toString() {
        return "Size";
    }
}