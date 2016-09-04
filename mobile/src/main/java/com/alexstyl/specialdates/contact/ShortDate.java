package com.alexstyl.specialdates.contact;

public interface ShortDate {

    /**
     * A string representation of the event as it is going to be used for Database transaction purposes.
     * </br> A ShortDate representation of the date "December 29, 1990" would be <i>1990-12-29</i> or <i>--12-29</i> if it is an annual event
     * (or in case of a birthday the year is unknown).
     * <p>If the event is an annual event, then the year <b>must</b> be ignored</p>
     */
    String toShortDate();
}
