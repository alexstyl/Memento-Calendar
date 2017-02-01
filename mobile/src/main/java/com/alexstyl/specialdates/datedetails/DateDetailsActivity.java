package com.alexstyl.specialdates.datedetails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.alexstyl.specialdates.BuildConfig;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.analytics.AnalyticsProvider;
import com.alexstyl.specialdates.analytics.Screen;
import com.alexstyl.specialdates.android.AndroidStringResources;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.MonthInt;
import com.alexstyl.specialdates.support.AskForSupport;
import com.alexstyl.specialdates.ui.activity.MainActivity;
import com.alexstyl.specialdates.ui.base.ThemedActivity;
import com.alexstyl.specialdates.ui.widget.MementoToolbar;
import com.alexstyl.specialdates.upcoming.UpcomingDateStringCreator;
import com.novoda.notils.caster.Views;

import java.util.List;

public class DateDetailsActivity extends ThemedActivity {

    private static final String EXTRA_DAY = BuildConfig.APPLICATION_ID + ".dayOfMonth";
    private static final String EXTRA_MONTH = BuildConfig.APPLICATION_ID + ".month";
    private static final String EXTRA_YEAR = BuildConfig.APPLICATION_ID + ".year";
    private static final String EXTRA_SOURCE = BuildConfig.APPLICATION_ID + ".source";
    /**
     * The activity was launched because the user tapped on a notification
     */
    private static final int SOURCE_NOTIFICATION = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_details);

        AnalyticsProvider.getAnalytics(this).trackScreen(Screen.DATE_DETAILS);
        MementoToolbar toolbar = Views.findById(this, R.id.memento_toolbar);
        toolbar.displayAsUp();
        setSupportActionBar(toolbar);

        Date displayingDate = getDateFrom(getIntent());

        if (getIntent().hasExtra(EXTRA_SOURCE)) {
            int intExtra = getIntent().getIntExtra(EXTRA_SOURCE, -1);
            if (intExtra == SOURCE_NOTIFICATION) {
                new AskForSupport(context()).requestForRatingSooner();
            }
        }

        if (savedInstanceState == null) {
            Fragment fragment = DateDetailsFragment.newInstance(displayingDate);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();

        }

        String titleDate = new UpcomingDateStringCreator(new AndroidStringResources(getResources()), Date.today())
                .createLabelFor(displayingDate);

        setTitle(titleDate);
    }

    private Date getDateFrom(Intent intent) {
        Bundle extras = intent.getExtras();
        if (intent.getData() != null && "com.android.calendar".equals(intent.getData().getAuthority())) {
            List<String> pathSegments = intent.getData().getPathSegments();
            if (pathSegments.size() == 2) {
                if (pathSegments.get(0).equals("time")) {
                    String timeInMillis = pathSegments.get(1);
                    return Date.fromMillis(Long.valueOf(timeInMillis));
                }
            }
        }

        int year = extras.getInt(EXTRA_YEAR);
        @MonthInt int month = extras.getInt(EXTRA_MONTH);
        int dayOfMonth = extras.getInt(EXTRA_DAY);

        return Date.on(dayOfMonth, month, year);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(DateDetailsActivity.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
                return true;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public static Intent getStartIntent(Context context, Date date) {
        Intent i = new Intent(context, DateDetailsActivity.class);
        i.putExtra(DateDetailsActivity.EXTRA_DAY, date.getDayOfMonth());
        i.putExtra(DateDetailsActivity.EXTRA_MONTH, date.getMonth());
        i.putExtra(DateDetailsActivity.EXTRA_YEAR, date.getYear());
        return i;
    }

    public static Intent getStartIntentFromExternal(Context context, Date date) {
        Intent startIntent = getStartIntent(context, date);
        startIntent.putExtra(EXTRA_SOURCE, SOURCE_NOTIFICATION);
        return startIntent;
    }

}
