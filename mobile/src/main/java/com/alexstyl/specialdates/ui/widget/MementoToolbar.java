package com.alexstyl.specialdates.ui.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.theming.AttributeExtractor;

public class MementoToolbar extends Toolbar {

    private static final int[] PRIMARY = new int[]{R.attr.colorPrimary};

    private AttributeExtractor attributeExtractor;

    public MementoToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        int toolbarColor = fetchAccentColor(context);

        setBackgroundColor(toolbarColor);
        setMinimumHeight(getResources().getDimensionPixelSize(R.dimen.toolbar_minHeight));

        if (isInEditMode()) {
            return;
        }
        attributeExtractor = new AttributeExtractor();
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
        int color = attributeExtractor.extractToolbarIconColors(getContext());
        Drawable closeIconDrawable = DrawableCompat.wrap(getResources().getDrawable(R.drawable.ic_action_left_semitransparent));
        DrawableCompat.setTintList(closeIconDrawable, ColorStateList.valueOf(color));
        setNavigationIcon(closeIconDrawable);
    }

    public void setNavigationAsClose() {
        int color = attributeExtractor.extractToolbarIconColors(getContext());
        Drawable closeIconDrawable = DrawableCompat.wrap(getResources().getDrawable(R.drawable.ic_close_white));
        DrawableCompat.setTintList(closeIconDrawable, ColorStateList.valueOf(color));
        setNavigationIcon(closeIconDrawable);
    }

}
