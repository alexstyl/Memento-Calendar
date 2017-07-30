package com.alexstyl.specialdates.images;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
@Singleton
public class ImageModule {

    @Provides
    @Singleton
    ImageLoader providesImageLoader(){
        return new UILImageLoader();
    }
}
