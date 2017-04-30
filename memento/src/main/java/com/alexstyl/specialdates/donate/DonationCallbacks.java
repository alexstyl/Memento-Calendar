package com.alexstyl.specialdates.donate;

interface DonationCallbacks {
    void onDonateException(String message);

    void onDonationFinished(Donation donation);
}
