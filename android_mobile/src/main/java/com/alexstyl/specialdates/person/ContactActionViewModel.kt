package com.alexstyl.specialdates.person

import android.graphics.drawable.Drawable

data class ContactActionViewModel(val action: ContactAction, val icon: Drawable) : PersonDetailItem {
    override fun getIdentifier(): String {
        return action.value
    }

    override fun getLabel(): String {
        return action.label
    }
}
