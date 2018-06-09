package com.alexstyl.specialdates.addevent.bottomsheet

import android.content.Intent
import android.graphics.drawable.Drawable

data class PhotoPickerViewModel(
        val activityIcon: Drawable,
        val label: String,
        val intent: Intent,
        val absolutePath: String)

