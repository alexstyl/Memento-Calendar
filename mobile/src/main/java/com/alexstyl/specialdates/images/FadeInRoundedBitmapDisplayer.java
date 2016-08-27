package com.alexstyl.specialdates.images;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;

public class FadeInRoundedBitmapDisplayer extends CircleBitmapDisplayer {

    public static final int FADE_IN_TIME = 200;
    private final ColorDrawable transparent;

    public FadeInRoundedBitmapDisplayer(Resources resources) {
        this.transparent = new ColorDrawable(resources.getColor(android.R.color.transparent));
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void display(Bitmap bitmap, ImageAware imageAware, LoadedFrom loadedFrom) {

        if ((loadedFrom == LoadedFrom.NETWORK) || (loadedFrom == LoadedFrom.DISC_CACHE)) {
            fadeInBitmap(bitmap, (ImageView) imageAware.getWrappedView());
        } else {
            super.display(bitmap, imageAware, loadedFrom);
        }

    }

    private void fadeInBitmap(Bitmap bitmap, ImageView wrappedView) {
        Drawable circularDrawable = new CircleDrawable(bitmap, strokeColor, strokeWidth);
        Drawable previous = wrappedView.getDrawable() == null ? transparent : wrappedView.getDrawable();

        final TransitionDrawable td =
                new TransitionDrawable(
                        new Drawable[]{
                                previous,
                                circularDrawable
                        }
                );

        td.setCrossFadeEnabled(true);
        wrappedView.setImageDrawable(td);
        td.startTransition(FADE_IN_TIME);
    }

}
