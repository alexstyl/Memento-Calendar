package com.alexstyl.specialdates.images;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;

/**
 * A {@linkplain BitmapDisplayer} that cross fades the original set Drawable of the view with the newly loaded one.
 */
class CrossFadeBitmapDisplayer implements BitmapDisplayer {
    private final int fadeInTime;
    private final Resources resources;
    private final ColorDrawable transparent;

    CrossFadeBitmapDisplayer(Resources resources, int duration) {
        this.fadeInTime = duration;
        this.resources = resources;
        this.transparent = new ColorDrawable(resources.getColor(android.R.color.transparent));
    }

    @Override
    public void display(Bitmap bitmap, ImageAware imageAware, LoadedFrom loadedFrom) {
        BitmapDrawable bitmapDrawable = new BitmapDrawable(resources, bitmap);
        display(imageAware, loadedFrom, bitmapDrawable);
    }

    protected void display(ImageAware imageAware, LoadedFrom loadedFrom, Drawable drawable) {
        if (loadedFrom == LoadedFrom.MEMORY_CACHE) {
            imageAware.setImageDrawable(drawable);
        } else {
            ImageView imageView = (ImageView) imageAware.getWrappedView();
            Drawable previous = imageView.getDrawable();
            if (previous == null) {
                previous = transparent;
            }

            TransitionDrawable transition = new TransitionDrawable(
                    new Drawable[]{
                            previous,
                            drawable
                    }
            );

            transition.setCrossFadeEnabled(true);
            imageAware.setImageDrawable(transition);
            transition.startTransition(fadeInTime);

        }
    }

}
