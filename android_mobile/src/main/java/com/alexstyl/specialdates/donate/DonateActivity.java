package com.alexstyl.specialdates.donate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.analytics.Analytics;
import com.alexstyl.specialdates.analytics.AnalyticsProvider;
import com.alexstyl.specialdates.analytics.Screen;
import com.alexstyl.specialdates.ui.base.ThemedMementoActivity;
import com.novoda.notils.caster.Views;

public class DonateActivity extends ThemedMementoActivity {

    private Analytics analytics;

    public static Intent createIntent(Context context) {
        return new Intent(context, DonateActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        analytics = AnalyticsProvider.getAnalytics(this);
        analytics.trackScreen(Screen.DONATE);

        setContentView(R.layout.activity_donate);

        View donate1 = Views.findById(this, R.id.btn1);
        View donate2 = Views.findById(this, R.id.btn2);
        View donate3 = Views.findById(this, R.id.btn3);

    }
}
