package com.alexstyl.specialdates.person

import com.alexstyl.specialdates.date.Date

data class ContactEventViewModel(val evenName: String, val dateLabel: String, val date: Date) : PersonDetailItem {
    override fun getLabel(): String {
        return evenName
    }

    override fun getValue(): String {
        return dateLabel
    }
}
