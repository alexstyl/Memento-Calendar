package com.alexstyl.specialdates.search

import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.events.namedays.NameCelebrations

data class NamedaySearchResultViewModel(val nameday: String, val namedays: List<NamedayDateViewModel>) : SearchResultViewModel

data class NamedayDateViewModel(val dateLabel: String, val date: Date)