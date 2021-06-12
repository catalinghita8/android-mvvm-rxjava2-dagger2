package com.inspiringteam.xchange.ui.quakes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.inspiringteam.xchange.R
import java.util.*

internal class QuakesAdapter(private var quakes: MutableList<QuakeItem>) : BaseAdapter() {

    fun replaceData(quakes: MutableList<QuakeItem>) {
        this.quakes = quakes
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return quakes.size
    }

    override fun getItem(i: Int): QuakeItem {
        return quakes[i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var rowView = convertView
        val viewHolder: QuakeItemViewHolder
        if (rowView == null) {
            val inflater = LayoutInflater.from(parent?.context)
            rowView = inflater.inflate(R.layout.quake_item, parent, false)
            viewHolder = QuakeItemViewHolder(rowView)
            rowView.tag = viewHolder
        } else
            viewHolder = rowView.tag as QuakeItemViewHolder

        val quakeItem = getItem(position)
        viewHolder.bindItem(quakeItem)
        return rowView!!
    }

}