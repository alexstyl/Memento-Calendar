package com.alexstyl.specialdates.addevent

import android.support.annotation.DrawableRes
import com.alexstyl.specialdates.Optional
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.events.peopleevents.EventType

data class AddEventContactEventViewModel(val hintText: String,
                                         val eventType: EventType,
                                         val date: Optional<Date>,
                                         val clearVisibility: Boolean,
                                         @param:DrawableRes val eventIconRes: Int)
