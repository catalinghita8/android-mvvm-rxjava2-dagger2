package com.inspiringteam.xchange.ui.quakes;

import android.view.View;
import android.widget.TextView;

import com.inspiringteam.xchange.R;
import com.inspiringteam.xchange.util.DisplayUtils.GravityUtils;
import com.inspiringteam.xchange.util.DisplayUtils.TimeUtils;

import rx.functions.Action0;

/**
 * View holder for the quake item.
 */
class QuakeItemViewHolder implements View.OnClickListener {

    private View mRow;

    private TextView mTitle;

    private TextView mMag;

    private TextView mTimeStamp;

    private TextView mGravity;

    private Action0 mOnItemClickAction;

    public QuakeItemViewHolder(View rowView) {
        mRow = rowView;

        mTitle = rowView.findViewById(R.id.title_tv);
        mMag = rowView.findViewById(R.id.magnitude_tv);
        mTimeStamp = rowView.findViewById(R.id.timestamp_tv);
        mGravity = rowView.findViewById(R.id.gravity_tv);

        rowView.setOnClickListener(this);
    }

    public void bindItem(QuakeItem quakeItem) {
        int[] dangerColorsArray = mRow.getContext()
                .getResources().getIntArray(R.array.danger_color_array);
        double magnitude = quakeItem.getQuake().getMagnitude();
        int dangerIndex = GravityUtils.toMagnitudeColor(magnitude);
        mRow.setBackgroundColor(dangerColorsArray[dangerIndex]);

        mMag.setText(String.valueOf(magnitude));
        mTitle.setText(quakeItem.getQuake().getLocation());
        mTimeStamp.setText(TimeUtils
                .toDuration(System.currentTimeMillis() - quakeItem.getQuake().getTimeStamp()));

        String intro = mRow.getContext().getResources().getString(R.string.quake_item_risk);
        String gravity = intro + GravityUtils.toGravityString(quakeItem.getQuake().getGravity());
        mGravity.setText(gravity);

        mOnItemClickAction = quakeItem.getOnClickAction();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickAction != null) {
            mOnItemClickAction.call();
        }
    }
}