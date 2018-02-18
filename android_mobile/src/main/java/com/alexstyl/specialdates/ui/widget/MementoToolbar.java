package com.alexstyl.specialdates.ui.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.theming.AttributeExtractor;

import javax.inject.Inject;

public class MementoToolbar extends Toolbar {
    
    @Inject AttributeExtractor attributeExtractor;

    public MementoToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);


        setMinimumHeight(getResources().getDimensionPixelSize(R.dimen.toolbar_minHeight));

        if (isInEditMode()) {
            return;
        }
        attributeExtractor = new AttributeExtractor();
    }
    
    public void displayNavigationIconAsUp() {
        int color = attributeExtractor.extractToolbarIconColors(getContext());
        Drawable closeIconDrawable = DrawableCompat.wrap(getResources().getDrawable(R.drawable.ic_action_left_semitransparent));
        DrawableCompat.setTintList(closeIconDrawable, ColorStateList.valueOf(color));
        setNavigationIcon(closeIconDrawable);
    }

    public void displayNavigationIconAsClose() {
        int color = attributeExtractor.extractToolbarIconColors(getContext());
        Drawable closeIconDrawable = DrawableCompat.wrap(getResources().getDrawable(R.drawable.ic_close_white));
        DrawableCompat.setTintList(closeIconDrawable, ColorStateList.valueOf(color));
        setNavigationIcon(closeIconDrawable);
    }

}
