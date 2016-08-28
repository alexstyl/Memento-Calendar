package com.alexstyl.specialdates.images;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;

public class CustomFadeInDisplayer implements BitmapDisplayer {

    public static final int FADE_IN_TIME = 200;
    private final Resources mResources;
    private final ColorDrawable transparent;

    public CustomFadeInDisplayer(Resources resources) {
        this.mResources = resources;
        this.transparent = new ColorDrawable(resources.getColor(android.R.color.transparent));
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void display(Bitmap bitmap, ImageAware imageAware, LoadedFrom loadedFrom) {
        if ((loadedFrom == LoadedFrom.NETWORK) || (loadedFrom == LoadedFrom.DISC_CACHE)) {
            ImageView imageView = (ImageView) imageAware.getWrappedView();
            Drawable previous = imageView.getDrawable();
            if (previous == null) {
                previous = transparent;
            }

            final TransitionDrawable td =
                    new TransitionDrawable(
                            new Drawable[]{
                                    previous,
                                    new BitmapDrawable(mResources, bitmap)
                            }
                    );

            td.setCrossFadeEnabled(true);

            imageAware.setImageDrawable(td);
            td.startTransition(FADE_IN_TIME);

        } else {
            imageAware.setImageBitmap(bitmap);
        }

    }

}
