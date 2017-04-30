package com.alexstyl.specialdates.donate;

interface DonationService {
    void setup(DonationCallbacks listener);

    void placeDonation(Donation donation);

    void dispose();
}
