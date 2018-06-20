package com.alexstyl.specialdates.theming;

import android.content.Context;
import android.content.res.Resources;

import dagger.Module;
import dagger.Provides;

@Module
public class ThemingModule {

    @Provides
    Themer themer(ThemingPreferences preferences, AttributeExtractor attributeExtractor, Resources resources) {
        return new Themer(preferences, attributeExtractor, resources);
    }

    @Provides
    AttributeExtractor attributeExtractor() {
        return new AttributeExtractor();
    }

    @Provides
    ThemingPreferences themingPreferences(Context context) {
        return ThemingPreferences.Companion.newInstance(context);
    }

}
