package com.alexstyl.specialdates.upcoming;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.alexstyl.resources.ColorResources;
import com.alexstyl.resources.DimensionResources;
import com.alexstyl.resources.StringResources;
import com.alexstyl.specialdates.AppComponent;
import com.alexstyl.specialdates.ExternalNavigator;
import com.alexstyl.specialdates.MementoApplication;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.analytics.Action;
import com.alexstyl.specialdates.analytics.Analytics;
import com.alexstyl.specialdates.analytics.Screen;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.facebook.FacebookPreferences;
import com.alexstyl.specialdates.images.ImageLoader;
import com.alexstyl.specialdates.support.AskForSupport;
import com.alexstyl.specialdates.theming.ThemeMonitor;
import com.alexstyl.specialdates.theming.ThemingPreferences;
import com.alexstyl.specialdates.ui.ViewFader;
import com.alexstyl.specialdates.ui.base.ThemedMementoActivity;
import com.alexstyl.specialdates.upcoming.view.ExposedSearchToolbar;
import com.alexstyl.specialdates.util.Notifier;
import com.novoda.notils.caster.Views;
import com.novoda.notils.meta.AndroidUtils;

import javax.inject.Inject;

import static android.view.View.OnClickListener;
import static com.novoda.notils.caster.Views.findById;

public class UpcomingEventsActivity extends ThemedMementoActivity implements DatePickerDialogFragment.OnDateSetListener {

    private Notifier notifier;
    private AskForSupport askForSupport;
    private ThemeMonitor themeMonitor;

    private MainNavigator navigator;
    private ExternalNavigator externalNavigator;
    private SearchTransitioner searchTransitioner;
    private DrawerLayout drawerLayout;

    private UpcomingEventsPreferences preferences;
    @Inject Analytics analytics;
    @Inject StringResources stringResource;
    @Inject DimensionResources dimensions;
    @Inject ColorResources colorResources;
    @Inject ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_events);

        AppComponent applicationModule = ((MementoApplication) getApplication()).getApplicationModule();
        applicationModule.inject(this);

        themeMonitor = ThemeMonitor.startMonitoring(ThemingPreferences.newInstance(this));
        analytics.trackScreen(Screen.HOME);

        navigator = new MainNavigator(analytics, this, stringResource, FacebookPreferences.newInstance(this));
        externalNavigator = new ExternalNavigator(this, analytics);

        ExposedSearchToolbar toolbar = findById(this, R.id.memento_toolbar);
        toolbar.setOnClickListener(onToolbarClickListener);
        setSupportActionBar(toolbar);

        ViewGroup activityContent = findById(this, R.id.main_content);
        searchTransitioner = new SearchTransitioner(this, navigator, activityContent, toolbar, new ViewFader());

        notifier = Notifier.newInstance(this, stringResource, colorResources, dimensions, imageLoader);

        findById(this, R.id.upcoming_events_add_event).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                navigator.toAddEvent();
            }
        });
        askForSupport = new AskForSupport(this);

        setTitle(R.string.app_name);

        final NavigationView navigationView = Views.findById(this, R.id.navigation_view);
        drawerLayout = Views.findById(this, R.id.drawer);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                boolean handled = handleNavigationItem(itemId);
                if (handled) {
                    drawerLayout.closeDrawer(Gravity.START);
                }

                // returning true highlights the row - we don't want that
                return false;
            }

            private boolean handleNavigationItem(int itemId) {
                switch (itemId) {
                    case R.id.nav_github_link:
                        navigator.toGithubPage();
                        return true;
                    case R.id.nav_settings:
                        navigator.toSettings();
                        return true;
                    case R.id.nav_invite_friend:
                        navigator.toAppInvite();
                        return true;
                    case R.id.nav_donate:
                        navigator.toDonate();
                        return true;
                    case R.id.nav_import_facebook:
                        navigator.toFacebookImport();
                        return true;
                    default:
                        return false;
                }
            }
        });
        preferences = UpcomingEventsPreferences.newInstance(thisActivity());

        if (!preferences.isTheUserAwareOfNavigationDrawer()) {
            drawerLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    drawerLayout.openDrawer(Gravity.START, true);
                    preferences.triggerNavigationDrawerDisplayed();
                }
            }, 400L);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (askForSupport.shouldAskForRating()) {
            askForSupport.askForRatingFromUser(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (themeMonitor.hasThemeChanged()) {
            reapplyTheme();
        }
        searchTransitioner.onActivityResumed();
        externalNavigator.connectTo(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        externalNavigator.disconnectTo(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mainactivity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(Gravity.START);
                return true;
            case R.id.action_select_date:
                showSelectDateDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showSelectDateDialog() {
        analytics.trackAction(Action.SELECT_DATE);
        DatePickerDialogFragment
                .newInstance(Date.Companion.today())
                .show(getSupportFragmentManager(), "date_picker");
    }

    @Override
    public void onDateSelected(Date dateSelected) {
        navigator.toDateDetails(dateSelected);
    }

    @Override
    public boolean onSearchRequested() {
        searchTransitioner.transitionToSearch();
        return true;
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        notifier.cancelAllEvents();
    }

    private final OnClickListener onToolbarClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            AndroidUtils.toggleKeyboard(v.getContext());
            onSearchRequested();
        }

    };

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.START)) {
            drawerLayout.closeDrawer(Gravity.START);
        } else {
            super.onBackPressed();
        }
    }

    public static Intent getStartIntent(Context context, Date date) {
        // TODO actually use the date
        Intent intent = new Intent(context, UpcomingEventsActivity.class);
        return intent;
    }

    @Override
    public boolean onKeyMenuPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.START)) {
            drawerLayout.closeDrawer(Gravity.START);
        } else {
            drawerLayout.openDrawer(Gravity.START, true);
        }
        return true;
    }
}
