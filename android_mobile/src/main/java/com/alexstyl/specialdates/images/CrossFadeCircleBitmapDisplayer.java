package com.alexstyl.specialdates.images;

import android.content.res.Resources;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;

class CrossFadeCircleBitmapDisplayer extends CrossFadeBitmapDisplayer {

    CrossFadeCircleBitmapDisplayer(Resources resources, int duration) {
        super(resources, duration);
    }

    @Override
    public void display(Bitmap bitmap, ImageAware imageAware, LoadedFrom loadedFrom) {
        CircleBitmapDisplayer.CircleDrawable circleDrawable = new CircleBitmapDisplayer.CircleDrawable(bitmap, 0, 0);
        display(imageAware, loadedFrom, circleDrawable);
    }
}
