package com.alexstyl.specialdates.home;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alexstyl.android.SimpleAnimatorListener;
import com.alexstyl.specialdates.AppComponent;
import com.alexstyl.specialdates.MementoApplication;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.analytics.Analytics;
import com.alexstyl.specialdates.analytics.Screen;
import com.alexstyl.specialdates.dailyreminder.DailyReminderNotifier;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.donate.DonateMonitor;
import com.alexstyl.specialdates.donate.DonationPreferences;
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsUpdater;
import com.alexstyl.specialdates.permissions.MementoPermissions;
import com.alexstyl.specialdates.ui.ViewFader;
import com.alexstyl.specialdates.ui.base.ThemedMementoActivity;
import com.alexstyl.specialdates.upcoming.DatePickerDialogFragment;
import com.alexstyl.specialdates.upcoming.view.ExposedSearchToolbar;
import com.google.android.gms.ads.MobileAds;
import com.novoda.notils.meta.AndroidUtils;

import javax.inject.Inject;

import org.jetbrains.annotations.Nullable;

import static android.view.View.OnClickListener;
import static com.novoda.notils.caster.Views.findById;

public class HomeActivity extends ThemedMementoActivity implements DatePickerDialogFragment.OnDateSetListener {

    private static final int CODE_PERMISSION = 150;
    public static final int PAGE_EVENTS = 0;
    public static final int PAGE_CONTACTS = 1;
    public static final int PAGE_SETTINGS = 2;
    public static final int CODE_ADD_EVENT = 120;

    private SearchTransitioner searchTransitioner;
    private FloatingActionButton actionButton;

    @Inject HomeNavigator navigator;
    @Inject Analytics analytics;
    @Inject DailyReminderNotifier dailyReminderNotifier;
    @Inject DonationPreferences donationPreferences;
    @Inject DonateMonitor donateMonitor;
    @Inject MementoPermissions permissions;
    @Inject PeopleEventsUpdater peopleEventsUpdater;

    private ViewPager viewPager;
    private DonationBannerView banner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        MobileAds.initialize(this, getString(R.string.admob_unit_id));

        AppComponent applicationModule = ((MementoApplication) getApplication()).getApplicationModule();
        applicationModule.inject(this);

        analytics.trackScreen(Screen.HOME);

        ExposedSearchToolbar toolbar = findById(this, R.id.home_toolbar);
        toolbar.setOnClickListener(onToolbarClickListener);
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.home_viewpager);
        final HomeViewPagerAdapter adapter = new HomeViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(adapter.getCount());

        LinearLayout content = findViewById(R.id.home_content);
        FrameLayout toolbarHolder = findViewById(R.id.home_toolbar_holder);
        searchTransitioner = new SearchTransitioner(this, navigator, content, toolbar, toolbarHolder, new ViewFader());

        setTitle(R.string.app_name);

        TabLayout tabLayout = findViewById(R.id.home_tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(HomeActivity.PAGE_EVENTS).setIcon(getTintedDrawable(R.drawable.ic_events));
        tabLayout.getTabAt(HomeActivity.PAGE_CONTACTS).setIcon(getTintedDrawable(R.drawable.ic_contacts));
        tabLayout.getTabAt(HomeActivity.PAGE_SETTINGS).setIcon(getTintedDrawable(R.drawable.ic_settings));

        actionButton = findViewById(R.id.home_add_event);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigator.toAddEvent(thisActivity(), CODE_ADD_EVENT);
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == HomeActivity.PAGE_SETTINGS
                        || (position == HomeActivity.PAGE_CONTACTS && positionOffset >= 0.5)) {
                    actionButton.hide();
                } else {
                    actionButton.show();
                }
            }
        });

        banner = findViewById(R.id.home_ad_banner);
        banner.setOnCloseBannerListener(new OnCloseBannerListener() {
            @Override
            public void onCloseButtonPressed() {
                navigator.toDonate(HomeActivity.this);
            }
        });

        if (ACTION_UPDATE_THEME.equals(getIntent().getAction())) {
            viewPager.setCurrentItem(HomeActivity.PAGE_SETTINGS);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!permissions.canReadAndWriteContacts()) {
            navigator.toContactPermission(this, CODE_PERMISSION);
        }
        if (viewPager.getCurrentItem() != PAGE_SETTINGS) {
            actionButton.show();
        }
        searchTransitioner.onActivityResumed();
        donateMonitor.addListener(donateMonitorListener);
        banner.setVisibility(bannerVisibility());
    }

    @Override
    protected void onStop() {
        super.onStop();
        donateMonitor.removeListener(donateMonitorListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_PERMISSION) {
            if (resultCode == RESULT_OK) {
                peopleEventsUpdater
                        .updateEvents()
                        .subscribe();
            } else {
                finishAffinity();
            }
        } else if (requestCode == CODE_ADD_EVENT && resultCode == RESULT_OK) {
            peopleEventsUpdater
                    .updateEvents()
                    .subscribe();
        }
    }

    private void hideBanner() {
        banner
                .animate()
                .yBy(banner.getHeight())
                .setInterpolator(new DecelerateInterpolator())
                .setListener(new SimpleAnimatorListener() {
                    @Override
                    public void onAnimationEnd(Animator animator) {
                        banner.setVisibility(View.GONE);
                    }
                }).start();
    }

    private int bannerVisibility() {
        if (donationPreferences.hasDonated()) {
            return View.GONE;
        } else {
            return View.VISIBLE;
        }
    }

    @Override
    public void onDateSelected(Date dateSelected) {
        navigator.toDateDetails(dateSelected, this);
    }

    @Override
    public boolean onSearchRequested() {
        actionButton.hide();
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

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == HomeActivity.PAGE_EVENTS) {
            super.onBackPressed();
        } else {
            viewPager.setCurrentItem(HomeActivity.PAGE_EVENTS);
        }
    }

    public static Intent getStartIntent(Context context) {
        return new Intent(context, HomeActivity.class);
    }

    private DonateMonitor.DonateMonitorListener donateMonitorListener = new DonateMonitor.DonateMonitorListener() {
        @Override
        public void onUserDonated() {
            Toast.makeText(HomeActivity.this, R.string.thanks_for_support, Toast.LENGTH_SHORT).show();
            hideBanner();
        }
    };
}
