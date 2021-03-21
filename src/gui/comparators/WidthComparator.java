package gui.comparators;

import gl.Video;
import gui.MediaContentBean;

import java.util.Comparator;

public class WidthComparator implements Comparator<MediaContentBean> {
    public int compare( MediaContentBean a, MediaContentBean b ) {
        if (a != null && b != null) {
            int av;
            try {
                av = ((Video) a.src).getWidth();
            } catch (Exception e) {
                return 1;
            }

            int bv;
            try {
                bv = ((Video) b.src).getWidth();
            } catch (Exception e) {
                return -1;
            }

            return Integer.compare( av, bv );
        }
        return 0;
    }

    public String toString() { return "Video Width"; }
}
