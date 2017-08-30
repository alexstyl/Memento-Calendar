package com.alexstyl.resources;

import android.content.res.Resources;

import com.alexstyl.specialdates.AndroidStrings;
import com.alexstyl.specialdates.Strings;

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
    Strings providesString() {
        return new AndroidStrings(resources);
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
