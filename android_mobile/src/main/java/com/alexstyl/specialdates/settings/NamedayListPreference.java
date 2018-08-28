
package com.alexstyl.specialdates.settings;

import android.content.Context;
import android.preference.ListPreference;
import android.preference.Preference;
import android.support.annotation.StringRes;
import android.util.AttributeSet;

import com.alexstyl.specialdates.AppComponent;
import com.alexstyl.specialdates.MementoApplication;
import com.alexstyl.specialdates.Strings;
import com.alexstyl.specialdates.events.namedays.NamedayLocale;
import com.alexstyl.specialdates.events.namedays.NamedayUserSettings;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Comparator;

public class NamedayListPreference extends ListPreference {

    @Inject NamedayUserSettings userSettings;
    @Inject Strings strings;

    public NamedayListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        AppComponent applicationModule = ((MementoApplication) context.getApplicationContext()).getApplicationModule();
        applicationModule.inject(this);

        NamedayLocale[] locales = NamedayLocale.values();
        Arrays.sort(locales, COUNTRY_CODE_COMPARATOR);
        setEntriesAndValues(locales);

        NamedayLocale defaultLocale = userSettings.getSelectedLanguage();
        setDefaultValue(defaultLocale.getCountryCode());
    }

    private void setEntriesAndValues(NamedayLocale[] locales) {
        CharSequence[] entries = new CharSequence[locales.length];
        CharSequence[] entryValues = new CharSequence[locales.length];
        for (int i = 0; i < locales.length; i++) {
            entries[i] = locales[i].getCountryCode();
            entryValues[i] = strings.localeName(locales[i]);
        }
        setEntries(entryValues);
        setEntryValues(entries);
    }

    private static final Comparator<NamedayLocale> COUNTRY_CODE_COMPARATOR = new Comparator<NamedayLocale>() {
        @Override
        public int compare(NamedayLocale o1, NamedayLocale o2) {
            return o1.getCountryCode().compareTo(o2.getCountryCode());
        }
    };

    void setOnNamedayLocaleChangeListener(final OnNamedayLocaleChangeListener onNamedayLocaleChangeListener) {
        setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                NamedayLocale namedayLocale = NamedayLocale.Companion.from((String) newValue);
                return onNamedayLocaleChangeListener.onNamedayChanged(namedayLocale);
            }
        });
    }

    interface OnNamedayLocaleChangeListener {
        boolean onNamedayChanged(NamedayLocale locale);
    }
}
