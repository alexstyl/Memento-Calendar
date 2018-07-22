package com.alexstyl.specialdates.debug

import com.alexstyl.specialdates.debug.contacts.DebugContactsFragment
import com.alexstyl.specialdates.debug.dailyreminder.DebugDailyReminderFragment
import com.alexstyl.specialdates.debug.firebase.DebugFirebaseFragment

object OptionToFragment {
    val mappings = DebugOption.values().associateBy({ it }, { option ->
        when (option) {
            DebugOption.CONTACTS -> DebugContactsFragment()
            DebugOption.FIREBASE -> DebugFirebaseFragment()
            DebugOption.DAILY_REMINDER -> DebugDailyReminderFragment()
            DebugOption.OLD_OPTIONS -> DebugFragment()
        }
    })
}