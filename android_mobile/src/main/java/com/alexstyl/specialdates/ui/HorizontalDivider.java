package com.alexstyl.specialdates.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.alexstyl.specialdates.R;

public class HorizontalDivider extends View {

    public HorizontalDivider(Context context, AttributeSet attrs) {
        super(context, attrs);
        int dividerHeight = getResources().getDimensionPixelSize(R.dimen.horizontal_divider_height);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dividerHeight));

        int dividerColor = getResources().getColor(R.color.divider_grey);
        setBackgroundColor(dividerColor);
    }
}
