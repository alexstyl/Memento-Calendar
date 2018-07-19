package com.alexstyl.specialdates.theming

import android.content.Context
import android.content.res.Resources

import com.alexstyl.specialdates.EasyPreferences

import dagger.Module
import dagger.Provides

@Module
class ThemingModule {

    @Provides
    internal fun themer(preferences: ThemingPreferences, attributeExtractor: AttributeExtractor, resources: Resources): Themer {
        return Themer(preferences, attributeExtractor, resources)
    }

    @Provides
    internal fun attributeExtractor(): AttributeExtractor {
        return AttributeExtractor()
    }

    @Provides
    internal fun themingPreferences(context: Context): ThemingPreferences {
        val defaultPreferences = EasyPreferences.createForDefaultPreferences(context)
        return ThemingPreferences(defaultPreferences)
    }

}
