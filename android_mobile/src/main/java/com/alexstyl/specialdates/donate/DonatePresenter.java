package com.alexstyl.specialdates.donate;

import com.alexstyl.resources.Strings;
import com.alexstyl.specialdates.LabelSetter;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.analytics.Analytics;
import com.alexstyl.specialdates.analytics.Screen;

class DonatePresenter {

    private final Analytics analytics;
    private final DonationService donationService;
    private final LabelSetter donateButtonLabel;
    private final Strings strings;

    DonatePresenter(Analytics analytics,
                    DonationService donationService,
                    LabelSetter donateButtonLabel,
                    Strings strings) {
        this.analytics = analytics;
        this.donationService = donationService;
        this.donateButtonLabel = donateButtonLabel;
        this.strings = strings;
    }

    void displaySelectedDonation(String amount) {
        donateButtonLabel.setLabel(strings.getString(R.string.donation_donate_amount, amount));
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
