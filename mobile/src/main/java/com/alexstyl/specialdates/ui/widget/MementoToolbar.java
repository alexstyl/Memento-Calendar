package com.alexstyl.specialdates.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.theming.Themer;

public class MementoToolbar extends Toolbar {

    private static final int[] PRIMARY = new int[]{R.attr.colorPrimary};

    private Themer themer;

    public MementoToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        int toolbarColor = fetchAccentColor(context);

        setBackgroundColor(toolbarColor);
        setMinimumHeight(getResources().getDimensionPixelSize(R.dimen.toolbar_minHeight));

        if (isInEditMode()) {
            return;
        }
        themer = Themer.get(context);
    }

    @ColorInt
    private int fetchAccentColor(Context context) {
        TypedValue typedValue = new TypedValue();
        TypedArray a = context.obtainStyledAttributes(typedValue.data, PRIMARY);
        int color = a.getColor(0, 0);
        a.recycle();
        return color;
    }

    public void displayAsUp() {
        if (themer.isActivityUsingDarkIcons(getContext())) {
            setNavigationIcon(R.drawable.ic_action_left_semitransparent);
        } else {
            setNavigationIcon(R.drawable.ic_action_arrow_light_back);
        }
    }
}
