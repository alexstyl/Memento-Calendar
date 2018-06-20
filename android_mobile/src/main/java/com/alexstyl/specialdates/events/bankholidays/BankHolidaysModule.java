package com.alexstyl.specialdates.events.bankholidays;

import android.content.Context;

import com.alexstyl.specialdates.EasyPreferences;
import com.alexstyl.specialdates.events.namedays.calendar.OrthodoxEasterCalculator;

import dagger.Module;
import dagger.Provides;

@Module
public class BankHolidaysModule {

    @Provides
    BankHolidaysUserSettings userSettings(Context context) {
        EasyPreferences preferences = EasyPreferences.createForDefaultPreferences(context);
        return new BankHolidaysPreferences(preferences);
    }

    @Provides
    GreekBankHolidaysCalculator greekBankHolidaysCalculator(OrthodoxEasterCalculator calculator) {
        return new GreekBankHolidaysCalculator(calculator);
    }

    @Provides
    BankHolidayProvider provider(GreekBankHolidaysCalculator bankHolidaysCalculator) {
        return new BankHolidayProvider(bankHolidaysCalculator);
    }
}


