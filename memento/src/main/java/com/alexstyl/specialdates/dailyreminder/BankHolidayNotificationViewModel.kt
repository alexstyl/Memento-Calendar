package com.alexstyl.specialdates.dailyreminder

import com.alexstyl.specialdates.date.Date

data class BankHolidayNotificationViewModel(val date: Date, val holidayName: String) : NotificationViewModel
