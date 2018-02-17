package com.alexstyl.specialdates.donate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.alexstyl.android.Version;
import com.alexstyl.specialdates.AppComponent;
import com.alexstyl.specialdates.CrashAndErrorTracker;
import com.alexstyl.specialdates.MementoApplication;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.Strings;
import com.alexstyl.specialdates.TextViewLabelSetter;
import com.alexstyl.specialdates.analytics.Analytics;
import com.alexstyl.specialdates.donate.util.IabHelper;
import com.alexstyl.specialdates.images.ImageLoader;
import com.alexstyl.specialdates.ui.base.MementoActivity;
import com.novoda.notils.caster.Views;

import javax.inject.Inject;
import java.net.URI;

public class DonateActivity extends MementoActivity {

    private static final int REQUEST_CODE = 1004;
    private static final int SCROLL_DOWN_ANIMATION_DELAY = 2000;
    private static final URI DEV_IMAGE_URI = URI.create("http://alexstyl.com/memento-calendar/dev.jpg");
    private static final int VELOCITY_Y = 50;

    private DonatePresenter donatePresenter;
    private SeekBar donateBar;
    private CoordinatorLayout coordinator;

    @Inject Analytics analytics;
    @Inject Strings strings;
    @Inject ImageLoader imageLoader;
    @Inject IabHelper iabHelper;
    @Inject DonationPreferences donationPreferences;
    @Inject CrashAndErrorTracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_donate);

        AppComponent applicationModule = ((MementoApplication) getApplication()).getApplicationModule();
        applicationModule.inject(this);

        Toolbar toolbar = Views.findById(this, R.id.toolbar);
        setSupportActionBar(toolbar);

        coordinator = Views.findById(this, R.id.donate_coordinator);
        ImageView avatar = Views.findById(this, R.id.donate_avatar);
        imageLoader
                .load(DEV_IMAGE_URI)
                .into(avatar);

        final AppBarLayout appBarLayout = Views.findById(this, R.id.app_bar_layout);
        final NestedScrollView scrollView = Views.findById(this, R.id.scroll);

        if (Version.hasLollipop()) {
            appBarLayout.addOnOffsetChangedListener(new HideStatusBarListener(getWindow()));
        }

        DonationService donationService = new AndroidDonationService(iabHelper, this, donationPreferences, analytics, tracker);
        final Button donateButton = Views.findById(this, R.id.donate_place_donation);
        donateButton.requestFocus();

        donatePresenter = new DonatePresenter(analytics, donationService, new TextViewLabelSetter(donateButton), strings);
        donateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Donation donation = AndroidDonation.valueOfIndex(donateBar.getProgress());
                donatePresenter.placeDonation(donation, REQUEST_CODE);
            }
        });
        setupDonateBar();

        donatePresenter.startPresenting(donationCallbacks());

        scrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollToDonate();
            }

            private void scrollToDonate() {
                CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
                AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
                behavior.onNestedFling(coordinator, appBarLayout, null, 0, VELOCITY_Y, true);
            }
        }, SCROLL_DOWN_ANIMATION_DELAY);

    }

    private void setupDonateBar() {
        donateBar = Views.findById(this, R.id.donation_bar);
        donateBar.setOnSeekBarChangeListener(new SimpleOnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String amount = AndroidDonation.valueOfIndex(progress).getAmount();
                donatePresenter.displaySelectedDonation(amount);
            }
        });
        AndroidDonation[] values = AndroidDonation.values();
        donateBar.setMax(values.length - 1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        donatePresenter.stopPresenting();
    }

    private DonationCallbacks donationCallbacks() {
        return new DonationCallbacks() {

            @Override
            public void onDonateException(String message) {
                tracker.track(new RuntimeException(message));
                finish();
            }

            @Override
            public void onDonationFinished(Donation donation) {
                DonateMonitor.getInstance().onDonationUpdated();

                Toast.makeText(DonateActivity.this, R.string.donate_thanks_for_donating, Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            }
        };
    }

    public static Intent createIntent(Context context) {
        return new Intent(context, DonateActivity.class);
    }
}
