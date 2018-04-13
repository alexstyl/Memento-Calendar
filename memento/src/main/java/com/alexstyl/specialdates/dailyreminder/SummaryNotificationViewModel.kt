package com.alexstyl.specialdates.dailyreminder

import java.net.URI

data class SummaryNotificationViewModel(val notificationId: Int,
                                        val title: CharSequence,
                                        val text: CharSequence,
                                        val lines: List<CharSequence>,
                                        val images: List<URI>)
