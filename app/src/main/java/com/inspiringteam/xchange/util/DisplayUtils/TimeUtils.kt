package com.inspiringteam.xchange.util.DisplayUtils

import java.util.*
import java.util.concurrent.TimeUnit

object TimeUtils {
    private val times: MutableList<Long> = Arrays.asList(
        TimeUnit.DAYS.toMillis(365),
        TimeUnit.DAYS.toMillis(30),
        TimeUnit.DAYS.toMillis(1),
        TimeUnit.HOURS.toMillis(1),
        TimeUnit.MINUTES.toMillis(1),
        TimeUnit.SECONDS.toMillis(1)
    )
    private val timesString: MutableList<String> =
        Arrays.asList("year", "month", "day", "hour", "minute", "second")

    fun toDuration(duration: Long): String {
        val res: StringBuffer = StringBuffer()
        for (i in times.indices) {
            val current: Long = times.get(i)
            val temp: Long = duration / current
            if (temp > 0) {
                res.append(temp).append(" ").append(timesString.get(i))
                    .append(if (temp != 1L) "s" else "").append(" ago")
                break
            }
        }
        if (("" == res.toString())) return "Just now" else return res.toString()
    }
}