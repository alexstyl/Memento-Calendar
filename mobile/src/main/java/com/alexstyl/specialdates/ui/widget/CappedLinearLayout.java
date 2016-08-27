package com.alexstyl.specialdates.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * <p>Created by alexstyl on 01/08/15.</p>
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class CappedLinearLayout extends LinearLayout {

    public CappedLinearLayout(Context context) {
        super(context);
    }

    public CappedLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CappedLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public CappedLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
