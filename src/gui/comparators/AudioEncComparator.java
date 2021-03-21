package gui.comparators;

import gl.Audio;
import gl.AudioVideo;
import gui.MediaContentBean;

import java.util.Comparator;

public class AudioEncComparator implements Comparator<MediaContentBean> {
    public int compare( MediaContentBean a, MediaContentBean b ) {
        if (a != null && b != null) {
            String av = null;
            try {
                av = ((Audio) a.src).getEncoding();
            } catch (Exception e) {}
            try {
                av = ((AudioVideo) a.src).getAudioEncoding();
            } catch (Exception e) {}
            if ( av == null )
                return 1;

            String bv = null;
            try {
                bv = ((Audio) b.src).getEncoding();
            } catch (Exception e) {}
            try {
                bv = ((AudioVideo) b.src).getAudioEncoding();
            } catch (Exception e) {}
            if ( bv == null )
                return -1;

            return av.compareTo( bv );
        }
        return 0;
    }

    public String toString() {
        return "Audio Encoding";
    }
}