package com.alexstyl.specialdates.person

import android.graphics.drawable.Drawable
import com.alexstyl.android.ViewVisibility

data class ContactActionViewModel(val action: ContactAction, @get:ViewVisibility val labelVisibility: Int, val icon: Drawable) : PersonDetailItem {
    override val value: String
        get() = action.value


    override val label: String
        get() = action.label

}
