package com.alexstyl.specialdates.theming;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class ThemingModule {

    @Provides
    Themer themer(ThemingPreferences preferences, AttributeExtractor attributeExtractor) {
        return new Themer(preferences, attributeExtractor);
    }

    @Provides
    AttributeExtractor attributeExtractor() {
        return new AttributeExtractor();
    }

    @Provides
    ThemingPreferences themingPreferences(Context context) {
        return ThemingPreferences.newInstance(context);
    }

}
