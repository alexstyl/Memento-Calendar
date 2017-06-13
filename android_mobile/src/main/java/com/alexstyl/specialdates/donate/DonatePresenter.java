package com.alexstyl.specialdates.donate;

import com.alexstyl.resources.StringResources;
import com.alexstyl.specialdates.LabelSetter;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.analytics.Analytics;
import com.alexstyl.specialdates.analytics.Screen;

class DonatePresenter {

    private final Analytics analytics;
    private final DonationService donationService;
    private final LabelSetter donateButtonLabel;
    private final StringResources stringResources;

    DonatePresenter(Analytics analytics,
                    DonationService donationService,
                    LabelSetter donateButtonLabel,
                    StringResources stringResources) {
        this.analytics = analytics;
        this.donationService = donationService;
        this.donateButtonLabel = donateButtonLabel;
        this.stringResources = stringResources;
    }

    void displaySelectedDonation(String amount) {
        donateButtonLabel.setLabel(stringResources.getString(R.string.donation_donate_amount, amount));
    }

    void startPresenting(DonationCallbacks donationCallbacks) {
        analytics.trackScreen(Screen.DONATE);
        donationService.setup(donationCallbacks);
    }

    void placeDonation(Donation donation, int requestCode) {
        donationService.placeDonation(donation, requestCode);
        analytics.trackDonationStarted(donation);
    }

    void stopPresenting() {
        donationService.dispose();
    }
}
