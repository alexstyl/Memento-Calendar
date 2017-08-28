package com.alexstyl.resources

import android.content.res.Resources
import com.alexstyl.specialdates.common.R

/**
 * Wrapper class of Android's [Resources] string related methods
 */
internal class AndroidStrings(private val resources: Resources) : Strings {
    override fun facebookMessenger(): String = resources.getString(R.string.facebook_messenger)

    override fun viewConversation(): String = resources.getString(R.string.View_conversation)
}
