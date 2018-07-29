package com.alexstyl.specialdates.dailyreminder

import com.alexstyl.specialdates.contact.ImageURL

data class SummaryNotificationViewModel(val notificationId: Int,
                                        val title: CharSequence,
                                        val text: CharSequence,
                                        val lines: List<CharSequence>,
                                        val images: List<ImageURL>)
