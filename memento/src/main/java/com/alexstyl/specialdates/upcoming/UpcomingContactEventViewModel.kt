package com.alexstyl.specialdates.upcoming

import android.support.annotation.ColorInt
import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.contact.ImageURL

data class UpcomingContactEventViewModel(val contact: Contact,
                                         val contactName: String,
                                         val eventLabel: String,
                                         @param:ColorInt val eventColor: Int,
                                         val backgroundVariant: Int,
                                         val contactImagePath: ImageURL) : UpcomingRowViewModel {
    override val viewType: Int
        get() = UpcomingRowViewType.CONTACT_EVENT
    override val id: Long
        get() = contact.contactID
}
