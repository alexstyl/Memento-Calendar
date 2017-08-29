package com.alexstyl.resources

import android.content.res.Resources
import com.alexstyl.specialdates.common.R
import com.alexstyl.specialdates.person.StarSign

internal class AndroidStrings(private val resources: Resources) : Strings {
    override fun facebookMessenger(): String = resources.getString(R.string.facebook_messenger)

    override fun viewConversation(): String = resources.getString(R.string.View_conversation)

    override fun nameOf(starSign: StarSign): String {
        when (starSign) {
            StarSign.AQUARIUS -> resources.getString(R.string.starsigns_aquarius)
            StarSign.PISCES -> resources.getString(R.string.starsigns_pisces)
            StarSign.ARIES -> resources.getString(R.string.starsigns_aries)
            StarSign.TAURUS -> resources.getString(R.string.starsigns_taurus)
            StarSign.GEMINI -> resources.getString(R.string.starsigns_gemini)
            StarSign.CANCER -> resources.getString(R.string.starsigns_cancer)
            StarSign.LEO -> resources.getString(R.string.starsigns_leo)
            StarSign.VIRGO -> resources.getString(R.string.starsigns_virgo)
            StarSign.LIBRA -> resources.getString(R.string.starsigns_libra)
            StarSign.SCORPIO -> resources.getString(R.string.starsigns_scorpio)
            StarSign.SAGITTARIUS -> resources.getString(R.string.starsigns_sagittarius)
            StarSign.CAPRICORN -> resources.getString(R.string.starsigns_capricorn)
        }
        throw IllegalArgumentException("Unable to resolve name of $starSign")
    }

    override fun turnsAge(age: Int): String = resources.getString(R.string.turns_age, age);

    override fun inviteFriend(): String = resources.getString(R.string.Invite_friend)

    override fun todaysNamedays(numberOfNamedays: Int): String = resources.getQuantityString(R.plurals.todays_nameday, numberOfNamedays)
}
