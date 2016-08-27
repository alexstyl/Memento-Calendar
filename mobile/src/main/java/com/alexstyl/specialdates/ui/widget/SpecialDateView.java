package com.alexstyl.specialdates.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alexstyl.specialdates.R;

/**
 * <p>Created by alexstyl on 08/06/15.</p>
 */
public class SpecialDateView extends FrameLayout {

    private ImageView mIcon;
    private LinearLayout mEventsList;

    public SpecialDateView(Context context) {
        super(context);
        init();
    }

    public SpecialDateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SpecialDateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_category_contact_events, this, false);
        mIcon = (ImageView) view.findViewById(R.id.event_icon);
        mEventsList = (LinearLayout) view.findViewById(R.id.event_dates_list);
        addView(view);
    }

    public void addEventDate(View view) {
        this.mEventsList.addView(view);
    }

    public void clearEvents() {
        this.mEventsList.removeAllViews();
    }

    public void setEventIcon(int drawableRes) {
        mIcon.setImageResource(drawableRes);
    }
}
