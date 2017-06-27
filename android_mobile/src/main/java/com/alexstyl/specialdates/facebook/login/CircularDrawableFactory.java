package com.alexstyl.specialdates.facebook.login;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;

import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;

public class CircularDrawableFactory {

    private final float strokeWidth;
    private final Resources resources;

    public CircularDrawableFactory(Resources resources, float strokeWidth) {
        this.resources = resources;
        this.strokeWidth = strokeWidth;
    }

    public Drawable from(@DrawableRes int drawableRes) {
        Bitmap icon = BitmapFactory.decodeResource(resources, drawableRes);
        return from(icon);
    }

    public Drawable from(Bitmap loadedImage) {
        return new CircleBitmapDisplayer.CircleDrawable(
                loadedImage,
                Color.WHITE,
                strokeWidth
        );
    }
}
