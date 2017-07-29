package com.alexstyl.resources;

import android.content.res.Resources;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
@Singleton
public class ResourcesModule {

    private final Resources resources;

    public ResourcesModule(Resources resources) {
        this.resources = resources;
    }

    @Provides
    @Singleton
    StringResources providesStringResources() {
        return new AndroidStringResources(resources);
    }

    @Provides
    @Singleton
    DimensionResources providesDimensionResources() {
        return new AndroidDimensionResources(resources);
    }

    @Provides
    @Singleton
    ColorResources providesColorResources() {
        return new AndroidColorResources(resources);
    }
}
