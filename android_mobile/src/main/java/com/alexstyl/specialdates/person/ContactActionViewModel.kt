package com.alexstyl.specialdates.person

import android.graphics.drawable.Drawable

data class ContactActionViewModel(val label: String, val identifier: String, val icon: Drawable, val runnable: Runnable) : PersonDetailItem
