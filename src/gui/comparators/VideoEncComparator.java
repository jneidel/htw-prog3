package gui.comparators;

import gl.Video;
import gui.MediaContentBean;

import java.util.Comparator;

public class VideoEncComparator implements Comparator<MediaContentBean> {
    public int compare( MediaContentBean a, MediaContentBean b ) {
        if (a != null && b != null) {
            String av;
            try {
                av = ((Video) a.src).getEncoding();
            } catch (Exception e) {
                return 1;
            }

            String bv;
            try {
                bv = ((Video) b.src).getEncoding();
            } catch (Exception e) {
                return -1;
            }

            return av.compareTo( bv );
        }
        return 0;
    }

    public String toString() { return "Video Encoding"; }
}