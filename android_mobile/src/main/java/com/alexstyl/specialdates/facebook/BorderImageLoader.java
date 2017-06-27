package com.alexstyl.specialdates.facebook;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import com.alexstyl.resources.DimensionResources;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.facebook.login.CircularDrawableFactory;
import com.alexstyl.specialdates.images.ImageLoader;
import com.alexstyl.specialdates.images.SimpleOnImageLoadedCallback;

class BorderImageLoader {

    private final DimensionResources resources;
    private final ImageLoader imageLoader;
    private CircularDrawableFactory circularDrawableFactory;

    BorderImageLoader(DimensionResources resources, ImageLoader imageLoader, CircularDrawableFactory circularDrawableFactory) {
        this.resources = resources;
        this.imageLoader = imageLoader;
        this.circularDrawableFactory = circularDrawableFactory;
    }

    public static BorderImageLoader newInstance(Resources resources, DimensionResources dimensionResources, ImageLoader loader) {
        int strokeSize = dimensionResources.getPixelSize(R.dimen.facebook_avatar_stroke);
        CircularDrawableFactory circularDrawableFactory = new CircularDrawableFactory(resources, strokeSize);
        return new BorderImageLoader(dimensionResources, loader, circularDrawableFactory);
    }

    public void loadImage(final ImageView imageView, Uri uri) {
        imageLoader.loadImage(uri, imageView, new SimpleOnImageLoadedCallback() {
            @Override
            public void onImageLoaded(Bitmap loadedImage) {
                Drawable from = circularDrawableFactory.from(loadedImage);
                imageView.setImageDrawable(from);
            }
        });
    }
}
