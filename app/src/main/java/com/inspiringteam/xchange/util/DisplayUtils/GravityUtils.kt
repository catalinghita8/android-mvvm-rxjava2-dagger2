package com.inspiringteam.xchange.util.DisplayUtils

import java.util.*

object GravityUtils {
    private val gravityStrings: MutableList<String> =
        Arrays.asList(" Very low", " Low", " Medium", " High", " Very High")

    fun toGravityString(g: Int): String {
        var index: Int = 0
        if (0 < g && g < 151) index = 0 else if (g < 251) index = 1 else if (g < 451) index =
            2 else if (g < 751) index = 3 else if (g < 1001) index = 4
        return gravityStrings.get(index)
    }

    fun toMagnitudeColor(m: Double): Int {
        var index: Int = 0
        if (0 < m && m < 1.2) index = 0 else if (m < 1.5) index = 1 else if (m < 2) index =
            2 else if (m < 2.5) index = 3 else if (m < 3) index = 4 else if (m < 4) index =
            5 else if (m < 4.5) index = 6 else if (m < 5) index = 7 else if (m < 5.5) index =
            8 else if (m < 6) index = 9 else if (m < 6.5) index = 10 else if (m < 100) index = 11
        return index
    }
}