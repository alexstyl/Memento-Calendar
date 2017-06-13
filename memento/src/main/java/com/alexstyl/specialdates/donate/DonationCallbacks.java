package com.alexstyl.specialdates.donate;

public interface DonationCallbacks {
    void onDonateException(String message);

    void onDonationFinished(Donation donation);
}
