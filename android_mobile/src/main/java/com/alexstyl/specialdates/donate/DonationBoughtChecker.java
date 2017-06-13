package com.alexstyl.specialdates.donate;

import com.alexstyl.specialdates.donate.util.IabException;
import com.alexstyl.specialdates.donate.util.IabHelper;
import com.alexstyl.specialdates.donate.util.Inventory;

import java.util.List;

class DonationBoughtChecker {

    private final IabHelper iabHelper;

    public DonationBoughtChecker(IabHelper iabHelper) {
        this.iabHelper = iabHelper;
    }

    boolean hasDonated(List<Donation> donations) throws IabException {
        Inventory inventory = iabHelper.queryInventory();
        for (Donation donation : donations) {
            if (inventory.hasPurchase(donation.getIdentifier())) {
                return true;
            }
        }
        return false;
    }
}
