package com.alexstyl.specialdates.images;

import android.content.res.Resources;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

class UniversalImageLoader {

    public static com.nostra13.universalimageloader.core.ImageLoader newInstance() {
        return ImageLoader.getInstance();
    }

    static DisplayImageOptions buildSquareImageOptions(Resources resources) {
        DisplayImageOptions.createSimple();
        return new DisplayImageOptions.Builder()
                .displayer(new CustomFadeInDisplayer(resources))
                .cacheInMemory(true)
                .showImageOnLoading(android.R.color.transparent)
                .cacheOnDisk(false)
                .build();
    }

    static DisplayImageOptions buildCircleImageOptions(Resources resources) {
        return new DisplayImageOptions.Builder()
                .displayer(new FadeInRoundedBitmapDisplayer(resources))
                .showImageOnLoading(android.R.color.transparent)
                .cacheInMemory(true)
                .cacheOnDisk(false) // don't cache it on disk.
                .bitmapConfig(Bitmap.Config.RGB_565).build();
    }

    static DisplayImageOptions buildWidgetImageOptions() {
        DisplayImageOptions.createSimple();
        return new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .showImageOnLoading(android.R.color.transparent)
                .cacheOnDisk(false)
                .build();
    }
}
