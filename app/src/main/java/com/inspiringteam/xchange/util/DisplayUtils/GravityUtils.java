package com.inspiringteam.xchange.util.DisplayUtils;

import android.util.Range;

import java.util.Arrays;
import java.util.List;

public final class GravityUtils {
    private static final List<String> gravityStrings = Arrays.asList(" Very low", " Low", " Medium", " High", " Very High");


    public static String toGravityString(int g) {
        int index = 0;

        if (0 < g && g < 151) index = 0;
        else if (g < 251) index = 1;
        else if(g < 451) index = 2;
        else if(g < 751) index = 3;
        else if(g < 1001) index = 4;

        return gravityStrings.get(index);
    }

    public static int toMagnitudeColor(double m) {
        int index = 0;

        if (0 < m && m < 1.2) index = 0;
        else if (m < 1.5) index = 1;
        else if(m < 2) index = 2;
        else if(m < 2.5) index = 3;
        else if(m < 3) index = 4;
        else if(m < 4) index = 5;
        else if(m < 4.5) index = 6;
        else if(m < 5) index = 7;
        else if(m < 5.5) index = 8;
        else if(m < 6) index = 9;
        else if(m < 6.5) index = 10;
        else if(m < 100) index = 11;

        return index;
    }
}
