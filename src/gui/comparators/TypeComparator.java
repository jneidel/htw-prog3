package gui.comparators;

import gui.MediaContentBean;
import java.util.Comparator;

public class TypeComparator implements Comparator<MediaContentBean> {
    public int compare( MediaContentBean a, MediaContentBean b ) {
        if (a != null && b != null) {
            return a.src.getClassName().compareTo(b.src.getClassName());
        }
        return 0;
    }

    public String toString() {
        return "Media Type";
    }
}