package com.alexstyl.specialdates

import android.content.res.Resources
import com.alexstyl.specialdates.Strings
import com.alexstyl.specialdates.common.R
import com.alexstyl.specialdates.events.peopleevents.EventType
import com.alexstyl.specialdates.events.peopleevents.StandardEventType
import com.alexstyl.specialdates.person.StarSign

class AndroidStrings(private val resources: Resources) : Strings {
    override fun facebookMessenger(): String = resources.getString(R.string.facebook_messenger)

    override fun viewConversation(): String = resources.getString(R.string.View_conversation)

    override fun nameOf(starSign: StarSign): String = when (starSign) {
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

    override fun turnsAge(age: Int): String = resources.getString(R.string.turns_age, age);

    override fun inviteFriend(): String = resources.getString(R.string.Invite_friend)

    override fun todaysNamedays(numberOfNamedays: Int): String = resources.getQuantityString(R.plurals.todays_nameday, numberOfNamedays)

    override fun donateAmount(amount: String): String = resources.getString(R.string.donation_donate_amount, amount)

    override fun eventOnDate(eventLabel: String, dateLabel: String): String = resources.getString(R.string.eventlabel_on_dateLabel, eventLabel, dateLabel)

    override fun appName(): String = resources.getString(R.string.app_name)

    override fun shareText(): String = resources.getString(R.string.share_text)

    override fun today(): String = resources.getString(R.string.today)

    override fun tomorrow(): String = resources.getString(R.string.tomorrow)

    override fun todayCelebrateTwo(nameOne: String, nameTwo: String): String = resources.getString(R.string.today_celebrates_two, nameOne, nameTwo)

    override fun todayCelebrateMany(name: String, numberLeft: Int): String = resources.getString(R.string.today_celebrates_many, name, numberLeft)

    override fun nameOfEvent(event: EventType): String = when (event) {
        StandardEventType.BIRTHDAY -> resources.getString(R.string.birthday)
        StandardEventType.NAMEDAY -> resources.getString(R.string.nameday)
        StandardEventType.ANNIVERSARY -> resources.getString(R.string.Anniversary)
        StandardEventType.OTHER -> resources.getString(R.string.Other)
        StandardEventType.CUSTOM -> resources.getString(R.string.Custom)
        else -> {
            throw IllegalStateException("$event has no name")
        }
    }
}
