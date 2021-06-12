package com.inspiringteam.xchange.ui.quakes

import android.view.View
import android.widget.TextView
import com.inspiringteam.xchange.R
import com.inspiringteam.xchange.util.DisplayUtils.GravityUtils
import com.inspiringteam.xchange.util.DisplayUtils.TimeUtils
import rx.functions.Action0

/**
 * View holder for the quake item.
 */
internal class QuakeItemViewHolder(private val rowView: View) : View.OnClickListener {
    private val titleTextView: TextView = rowView.findViewById(R.id.title_tv)
    private val magnitudeTextView: TextView = rowView.findViewById(R.id.magnitude_tv)
    private val timeStampTextView: TextView = rowView.findViewById(R.id.timestamp_tv)
    private val gravityTextView: TextView = rowView.findViewById(R.id.gravity_tv)
    private var clickAction: Action0? = null


    init {
        rowView.setOnClickListener(this)
    }

    fun bindItem(quakeItem: QuakeItem) {
        val dangerColorsArray = rowView.context.resources.getIntArray(R.array.danger_color_array)
        val magnitude = quakeItem.quake.magnitude ?: 0.0
        val dangerIndex = GravityUtils.toMagnitudeColor(magnitude)
        rowView.setBackgroundColor(dangerColorsArray[dangerIndex])
        magnitudeTextView.text = magnitude.toString()
        titleTextView.text = quakeItem.quake.location
        timeStampTextView.text =
            TimeUtils.toDuration(System.currentTimeMillis() - (quakeItem.quake.timeStamp ?: 0))
        val intro = rowView.context.resources.getString(R.string.quake_item_risk)
        val gravity = intro + GravityUtils.toGravityString(quakeItem.quake.gravity)
        gravityTextView.text = gravity
        clickAction = quakeItem.onClickAction
    }

    override fun onClick(v: View) {
        clickAction?.call()
    }

}