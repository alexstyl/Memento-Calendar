package com.alexstyl.specialdates.images;

import android.content.res.Resources;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
@Singleton
public class ImageModule {

    private static final int CROSSFADE_DURATION = 200;
    private final Resources resources;

    public ImageModule(Resources resources) {
        this.resources = resources;
    }

    @Provides
    @Singleton
    ImageLoader providesImageLoader() {
        return new UILImageLoader(new CrossFadeBitmapDisplayer(resources, CROSSFADE_DURATION), new CrossFadeCircleBitmapDisplayer(resources, CROSSFADE_DURATION));
    }
}
