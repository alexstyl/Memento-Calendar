package com.alexstyl.specialdates.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.alexstyl.specialdates.AppComponent;
import com.alexstyl.specialdates.MementoApplication;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.analytics.Analytics;
import com.alexstyl.specialdates.analytics.Screen;
import com.alexstyl.specialdates.dailyreminder.DailyReminderNotifier;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.theming.ThemeMonitor;
import com.alexstyl.specialdates.theming.ThemingPreferences;
import com.alexstyl.specialdates.ui.ViewFader;
import com.alexstyl.specialdates.ui.base.ThemedMementoActivity;
import com.alexstyl.specialdates.upcoming.DatePickerDialogFragment;
import com.alexstyl.specialdates.upcoming.view.ExposedSearchToolbar;
import com.novoda.notils.meta.AndroidUtils;

import javax.inject.Inject;

import static android.view.View.OnClickListener;
import static com.novoda.notils.caster.Views.findById;

public class HomeActivity extends ThemedMementoActivity implements DatePickerDialogFragment.OnDateSetListener {


    private ThemeMonitor themeMonitor;

    private SearchTransitioner searchTransitioner;

    @Inject
    HomeNavigator navigator;
    @Inject
    Analytics analytics;
    @Inject
    DailyReminderNotifier dailyReminderNotifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        AppComponent applicationModule = ((MementoApplication) getApplication()).getApplicationModule();
        applicationModule.inject(this);

        themeMonitor = ThemeMonitor.startMonitoring(ThemingPreferences.newInstance(this));
        analytics.trackScreen(Screen.HOME);


        ExposedSearchToolbar toolbar = findById(this, R.id.home_toolbar);
        toolbar.setOnClickListener(onToolbarClickListener);
        setSupportActionBar(toolbar);


        ViewPager viewPager = findViewById(R.id.home_viewpager);
        HomeViewPagerAdapter adapter = new HomeViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(adapter.getCount());

        searchTransitioner = new SearchTransitioner(this, navigator, viewPager, toolbar, new ViewFader());

        setTitle(R.string.app_name);

        TabLayout tabLayout = findViewById(R.id.home_tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_events);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_contacts);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_settings);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (themeMonitor.hasThemeChanged()) {
            reapplyTheme();
        }
        searchTransitioner.onActivityResumed();
    }


    @Override
    public void onDateSelected(Date dateSelected) {
        navigator.toDateDetails(dateSelected, this);
    }

    @Override
    public boolean onSearchRequested() {
        searchTransitioner.transitionToSearch();
        return true;
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        dailyReminderNotifier.cancelAllEvents();
    }

    private final OnClickListener onToolbarClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            AndroidUtils.toggleKeyboard(v.getContext());
            onSearchRequested();
        }

    };

    public static Intent getStartIntent(Context context) {
        return new Intent(context, HomeActivity.class);
    }
}
