package com.inspiringteam.xchange.ui.quakes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.inspiringteam.xchange.R;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

final class QuakesAdapter extends BaseAdapter{
    private List<QuakeItem> mQuakes;

    public QuakesAdapter(List<QuakeItem> quakes) {
        setList(quakes);
    }

    public void replaceData(List<QuakeItem> quakes) {
        setList(quakes);
        notifyDataSetChanged();
    }

    private void setList(List<QuakeItem> quakes) {
        mQuakes = checkNotNull(quakes);
    }

    @Override
    public int getCount() {
        return mQuakes.size();
    }

    @Override
    public QuakeItem getItem(int i) {
        return mQuakes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rowView = view;
        QuakeItemViewHolder viewHolder;

        if (rowView == null) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            rowView = inflater.inflate(R.layout.quake_item, viewGroup, false);

            viewHolder = new QuakeItemViewHolder(rowView);
            rowView.setTag(viewHolder);
        } else {
            viewHolder = (QuakeItemViewHolder) rowView.getTag();
        }

        final QuakeItem quakeItem = getItem(i);
        viewHolder.bindItem(quakeItem);

        return rowView;
    }
}
