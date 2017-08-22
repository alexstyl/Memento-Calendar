package com.alexstyl.specialdates.ui.widget;

import android.content.res.Resources;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
@Singleton
public class ViewModule {

    private final Resources resources;

    public ViewModule(Resources resources) {
        this.resources = resources;
    }

    @Provides
    @Singleton
    public LetterPainter providesLetterPainter() {
        return new LetterPainter(resources);
    }
}
