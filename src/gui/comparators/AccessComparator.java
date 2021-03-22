package gui.comparators;

import gui.MediaContentBean;

import java.util.Comparator;

public class AccessComparator implements Comparator<MediaContentBean> {
    public int compare( MediaContentBean a, MediaContentBean b ) {
        if (a != null && b != null) {
            long av = a.src.getAccessCount();
            long bv = b.src.getAccessCount();
            return Long.compare( av, bv );
        }
        return 0;
    }

    public String toString() {
        return "Access Count";
    }
}