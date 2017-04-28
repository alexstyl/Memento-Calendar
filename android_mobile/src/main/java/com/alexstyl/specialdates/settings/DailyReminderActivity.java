package com.alexstyl.specialdates.settings;

import android.os.Bundle;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.ui.base.MementoPreferenceActivity;
import com.alexstyl.specialdates.ui.widget.MementoToolbar;
import com.novoda.notils.caster.Views;

public class DailyReminderActivity extends MementoPreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dailyreminder);

        MementoToolbar toolbar = Views.findById(this, R.id.memento_toolbar);
        setSupportActionBar(toolbar);
        toolbar.displayAsUp();
    }

}
