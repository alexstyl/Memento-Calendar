package com.alexstyl.specialdates.person

import android.content.Intent
import android.graphics.drawable.Drawable

data class ContactCallViewModel(val phoneLabel: String, val phoneNumber: String, val phoneicon: Drawable, val startIntent: Intent) : PersonDetailItem
