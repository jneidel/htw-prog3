package gui.comparators;

import gl.*;
import gui.MediaContentBean;

import java.util.Comparator;

public class HolderComparator implements Comparator<MediaContentBean> {
    public int compare( MediaContentBean a, MediaContentBean b ) {
        if (a != null && b != null) {
            String av = null;
            try {
                av = ((LicensedAudio) a.src).getHolder();
            } catch (Exception e) {}
            try {
                av = ((LicensedVideo) a.src).getHolder();
            } catch (Exception e) {}
            try {
                av = ((LicensedAudioVideo) a.src).getHolder();
            } catch (Exception e) {}
            if ( av == null )
                return 1;

            String bv = null;
            try {
                bv = ((LicensedAudio) b.src).getHolder();
            } catch (Exception e) {}
            try {
                bv = ((LicensedVideo) b.src).getHolder();
            } catch (Exception e) {}
            try {
                bv = ((LicensedAudioVideo) b.src).getHolder();
            } catch (Exception e) {}
            if ( bv == null )
                return -1;

            return av.compareTo( bv );
        }
        return 0;
    }

    public String toString() { return "License Holder"; }
}