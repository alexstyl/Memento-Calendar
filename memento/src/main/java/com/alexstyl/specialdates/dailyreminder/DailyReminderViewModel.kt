package com.alexstyl.specialdates.dailyreminder

import com.alexstyl.specialdates.Optional

data class DailyReminderViewModel(val summaryViewModel: SummaryNotificationViewModel,
                                  val contacts: List<ContactEventNotificationViewModel>,
                                  val namedays: Optional<NamedaysNotificationViewModel>,
                                  val bankHoliday: Optional<BankHolidayNotificationViewModel>)
