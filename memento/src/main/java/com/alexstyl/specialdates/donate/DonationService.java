package com.alexstyl.specialdates.donate;

public interface DonationService {
    void setup(DonationCallbacks listener);

    void placeDonation(Donation donation, int requestCode);

    void dispose();

    void restoreDonations();
}
