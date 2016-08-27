package com.alexstyl.specialdates.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class AvatarLayout extends LinearLayout {

    public AvatarLayout(Context context) {
        super(context);
    }

    public AvatarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public AvatarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        super.setOrientation(HORIZONTAL);
    }

    @Override
    public void setOrientation(int orientation) {

    }

    @Override
    public ColorImageView getChildAt(int index) {
        return (ColorImageView) super.getChildAt(index);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        int width = r - l;

        int childCount = getChildCount();
        int childrenSize = 0;
        for (int i = childCount; i > 0; i--) {
            View view = getChildAt(i - 1);
            int paddings = view.getPaddingRight();
            LinearLayout.LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
            int margins = layoutParams.rightMargin;
            int childSize = view.getMeasuredWidth() + (paddings + margins);
            childrenSize += childSize;
            if (width < childrenSize) {
                childrenSize -= childSize;
                view.setVisibility(View.GONE);
            } else {
                childrenSize += view.getPaddingLeft() + layoutParams.leftMargin;
            }

        }
    }
}
