package com.inspiringteam.xchange.util.DisplayUtils;

import com.inspiringteam.xchange.data.models.Quake;

import java.util.Collections;
import java.util.List;

public final class SortUtils {


    public static List<Quake> sortByNewest(List<Quake> list) {
        Collections.sort(list, (e1, e2) -> e1.getTimeStamp().compareTo(e2.getTimeStamp()));
        Collections.reverse(list);

        return list;
    }
}
