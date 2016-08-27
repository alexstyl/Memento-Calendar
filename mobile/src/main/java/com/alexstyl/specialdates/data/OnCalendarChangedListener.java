package com.alexstyl.specialdates.data;

/**
 * Listener for all Calendar type classes so that whoever is interested to be notified for their updates
 * <p>Created by alexstyl on 01/08/15.</p>
 */
public interface OnCalendarChangedListener {

    /**
     * Called when the Calendar has been updated
     */
    void onCalendarUpdated();
}
