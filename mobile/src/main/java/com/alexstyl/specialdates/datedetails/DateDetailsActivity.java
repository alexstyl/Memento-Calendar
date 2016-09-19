package com.alexstyl.specialdates.datedetails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.alexstyl.specialdates.BuildConfig;
import com.alexstyl.specialdates.ErrorTracker;
import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.date.DateFormatUtils;
import com.alexstyl.specialdates.date.DayDate;
import com.alexstyl.specialdates.support.AskForSupport;
import com.alexstyl.specialdates.ui.activity.MainActivity;
import com.alexstyl.specialdates.ui.base.ThemedActivity;
import com.alexstyl.specialdates.ui.widget.MementoToolbar;
import com.novoda.notils.caster.Views;

public class DateDetailsActivity extends ThemedActivity {

    private static final String EXTRA_DAY = BuildConfig.APPLICATION_ID + ".dayOfMonth";
    private static final String EXTRA_MONTH = BuildConfig.APPLICATION_ID + ".month";
    private static final String EXTRA_YEAR = BuildConfig.APPLICATION_ID + ".year";
    private static final String EXTRA_SOURCE = BuildConfig.APPLICATION_ID + ".source";
    /**
     * The activity was lauched because the user tapped on a notification
     */
    private static final int SOURCE_NOTIFICATION = 1;

    private DayDate displayingDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_details);
        MementoToolbar toolbar = Views.findById(this, R.id.memento_toolbar);
        toolbar.displayAsUp();
        setSupportActionBar(toolbar);

        Optional<DayDate> receivedDate = getCalendarFromIntent(getIntent());
        if (!receivedDate.isPresent()) {
            finish();
            ErrorTracker.track(new NullPointerException("Tried to open DateDetails with no date in the intent:[" + getIntent() + "]"));
            return;
        }
        this.displayingDate = receivedDate.get();

        if (getIntent().hasExtra(EXTRA_SOURCE)) {
            int intExtra = getIntent().getIntExtra(EXTRA_SOURCE, -1);
            if (intExtra == SOURCE_NOTIFICATION) {
                new AskForSupport(context()).requestForRatingSooner();
            }
        }

        if (savedInstanceState == null) {

            final int year = displayingDate.getYear();
            final int month = displayingDate.getMonth();
            final int day = displayingDate.getDayOfMonth();

            Fragment fragment = DateDetailsFragment.newInstance(year, month, day);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();

        }

        String titleDate = DateFormatUtils.formatTimeStampString(
                DateDetailsActivity.this, displayingDate.toMillis(),
                true
        );
        setTitle(titleDate);
    }

    private Optional<DayDate> getCalendarFromIntent(Intent intent) {
        if (intent.hasExtra(EXTRA_DAY)) {
            Bundle extras = intent.getExtras();
            int year = extras.getInt(EXTRA_YEAR);
            int month = extras.getInt(EXTRA_MONTH);
            int dayOfMonth = extras.getInt(EXTRA_DAY);

            return new Optional<>(DayDate.newInstance(dayOfMonth, month, year));
        }
        return Optional.absent();

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

    /**
     * Starts the {@link DateDetailsActivity}, that will display details about
     * the given date
     *
     * @param context The context to use
     */
    public static void startActivity(Context context, int month,
                                     int dayOfMonth, int year) {
        Intent startIntent = getStartIntent(context, dayOfMonth, month, year);
        context.startActivity(startIntent);
    }

    /**
     * @param context    The context to use
     * @param dayOfMonth The day of the month to display
     * @param month      The month to display
     * @param year       The year to display
     */
    public static Intent getStartIntent(Context context, int dayOfMonth, int month,
                                        int year) {
        Intent i = new Intent(context, DateDetailsActivity.class);
        i.putExtra(DateDetailsActivity.EXTRA_DAY, dayOfMonth);
        i.putExtra(DateDetailsActivity.EXTRA_MONTH, month);
        i.putExtra(DateDetailsActivity.EXTRA_YEAR, year);
        return i;
    }

    public static Intent getStartIntentFromExternal(Context context, int dayOfMonth, int month, int year) {
        Intent startIntent = getStartIntent(context, dayOfMonth, month, year);
        startIntent.putExtra(EXTRA_SOURCE, SOURCE_NOTIFICATION);
        return startIntent;
    }

}
