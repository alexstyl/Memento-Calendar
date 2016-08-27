package com.alexstyl.specialdates.search;

import com.alexstyl.specialdates.contact.Contact;

import java.util.List;

/**
 * <p>Created by alexstyl on 19/04/15.</p>
 */
public interface Search<T extends Contact> {

    /**
     * Searches the device for contacts of the given name
     *
     * @param searchQuery The query for the search
     * @param namedays    Whether to search for namedays
     * @param counter     The number of results to search
     * @return
     */
    List<T> findContactsWithEvents(String searchQuery, boolean namedays, int counter);

}
