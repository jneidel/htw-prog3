package gui.comparators;

import gui.MediaContentBean;

import java.util.Comparator;

public class BitrateComparator implements Comparator<MediaContentBean> {
    public int compare( MediaContentBean a, MediaContentBean b ) {
        if (a != null && b != null) {
            Long av = a.src.getBitrate();
            Long bv = b.src.getBitrate();
            return av.compareTo( bv );
        }
        return 0;
    }

    public String toString() {
        return "Bitrate";
    }
}