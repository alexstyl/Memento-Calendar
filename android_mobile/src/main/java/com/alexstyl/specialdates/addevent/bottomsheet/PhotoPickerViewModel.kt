package com.alexstyl.specialdates.addevent.bottomsheet

import android.content.Intent
import android.graphics.drawable.Drawable
import java.net.URI

data class PhotoPickerViewModel(
        val activityIcon: Drawable,
        val label: String,
        val intent: Intent,
        val absolutePath: URI)

