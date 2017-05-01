package com.alexstyl.specialdates.donate;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.alexstyl.resources.StringResources;
import com.alexstyl.specialdates.ErrorTracker;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.TextViewLabelSetter;
import com.alexstyl.specialdates.analytics.Analytics;
import com.alexstyl.specialdates.analytics.AnalyticsProvider;
import com.alexstyl.specialdates.android.AndroidStringResources;
import com.alexstyl.specialdates.donate.util.IabHelper;
import com.alexstyl.specialdates.images.UILImageLoader;
import com.alexstyl.specialdates.ui.base.MementoActivity;
import com.novoda.notils.caster.Views;

public class DonateActivity extends MementoActivity {

    private static final int REQUEST_CODE = 1004;
    private static final Uri DEV_IMAGE_URI = Uri.parse("http://alexstyl.com/memento-calendar/dev.jpg");

    private DonatePresenter donatePresenter;
    private SeekBar donateBar;

    @Override
    protected boolean shouldUseHomeAsUp() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_donate);

        final Toolbar toolbar = Views.findById(this, R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView avatar = Views.findById(this, R.id.donate_avatar);
        UILImageLoader.createLoader(getResources()).loadImage(DEV_IMAGE_URI, avatar);

        AppBarLayout appBarLayout = Views.findById(this, R.id.app_bar_layout);

        appBarLayout.addOnOffsetChangedListener(new HideStatusBarListener(getWindow()));

        StringResources stringResources = new AndroidStringResources(getResources());
        Analytics analytics = AnalyticsProvider.getAnalytics(this);

        DonationService donationService = new AndroidDonationService(new IabHelper(this, AndroidDonationConstants.PUBLIC_KEY), this);
        Button donateButton = Views.findById(this, R.id.donate_place_donation);
        donatePresenter = new DonatePresenter(analytics, donationService, new TextViewLabelSetter(donateButton), stringResources);
        donateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Donation donation = AndroidDonation.valueOfIndex(donateBar.getProgress());
                donatePresenter.placeDonation(donation, REQUEST_CODE);
            }
        });
        setupDonateBar();

        donatePresenter.startPresenting(donationCallbacks());

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
                ErrorTracker.track(new RuntimeException(message));
                finish();
            }

            @Override
            public void onDonationFinished(Donation donation) {
                DonationPreferences prefs = DonationPreferences.newInstance(DonateActivity.this);
                prefs.markAsDonated();

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
