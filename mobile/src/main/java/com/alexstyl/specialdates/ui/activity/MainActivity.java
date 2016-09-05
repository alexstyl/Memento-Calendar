package com.alexstyl.specialdates.ui.activity;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.about.AboutActivity;
import com.alexstyl.specialdates.addevent.AddBirthdayActivity;
import com.alexstyl.specialdates.analytics.Analytics;
import com.alexstyl.specialdates.analytics.Screen;
import com.alexstyl.specialdates.search.SearchActivity;
import com.alexstyl.specialdates.settings.MainPreferenceActivity;
import com.alexstyl.specialdates.support.AskForSupport;
import com.alexstyl.specialdates.support.SupportDonateDialog;
import com.alexstyl.specialdates.ui.ThemeReapplier;
import com.alexstyl.specialdates.ui.base.ThemedActivity;
import com.alexstyl.specialdates.util.Notifier;
import com.alexstyl.specialdates.util.Utils;
import com.alexstyl.specialdates.widgetprovider.TodayWidgetProvider;
import com.novoda.notils.caster.Views;

/*
 * The activity was first launched with MainActivity being in package.ui.activity
  * For that reason, it needs to stay here so that we don't remove ourselves from the user's desktop
 */
public class MainActivity extends ThemedActivity {

    private Notifier notifier;
    private AskForSupport askForSupport;
    private ThemeReapplier reapplier;
    private Analytics analytics;
    private Toolbar toolbar;
    private FloatingActionButton addBirthdayFAB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        reapplier = new ThemeReapplier(this);

        analytics = Analytics.get(this);
        analytics.trackScreen(Screen.HOME);

        toolbar = Views.findById(this, R.id.memento_toolbar);
        toolbar.setOnClickListener(onToolbarClickListener);
        setSupportActionBar(toolbar);

        notifier = Notifier.newInstance(this);

        addBirthdayFAB = Views.findById(this, R.id.fab_add);
        addBirthdayFAB.setOnClickListener(startAddBirthdayOnClick);
        askForSupport = new AskForSupport(this);
        setTitle(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (reapplier.hasThemeChanged()) {
            reapplyTheme();
        } else if (askForSupport.shouldAskForRating()) {
            askForSupport.askForRatingFromUser(this);
        }
        addBirthdayFAB.show();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    private void startAddBirthdayActivity() {
        Intent intent = new Intent(this, AddBirthdayActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mainactivity, menu);

        boolean hasBilling = SupportDonateDialog.isBillingAvailable(this);
        menu.findItem(R.id.action_donate).setVisible(hasBilling);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                openSettings();
                break;
            case R.id.action_about:
                openAboutScreen();
                break;
            case R.id.action_donate:
                openDonateDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openDonateDialog() {
        analytics.trackScreen(Screen.DONATE);
        SupportDonateDialog.displayDialog(this);
    }

    private void openAboutScreen() {
        startActivity(new Intent(this, AboutActivity.class));

    }

    private void openSettings() {
        startActivity(new Intent(this, MainPreferenceActivity.class));
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onSearchRequested() {
        navigateToSearch();
        return true;
    }

    private final View.OnClickListener onToolbarClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            navigateToSearchWithAnimation();
        }

    };

    private void navigateToSearchWithAnimation() {
        addBirthdayFAB.hide();
        if (Utils.hasLollipop()) {
            Intent i = new Intent(this, SearchActivity.class);

            View sharedView = toolbar;
            String transitionName = getString(R.string.search_transition_name);

            ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, sharedView, transitionName);
            startActivity(i, transitionActivityOptions.toBundle());
        } else {
            navigateToSearch();
        }
    }

    private void navigateToSearch() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        notifier.cancelAllEvents();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isFinishing()) {
            TodayWidgetProvider.updateWidgets(this);
        }
    }

    private final View.OnClickListener startAddBirthdayOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startAddBirthdayActivity();
        }
    };
}
