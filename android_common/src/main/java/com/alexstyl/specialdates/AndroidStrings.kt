package com.alexstyl.specialdates

import android.content.res.Resources
import com.alexstyl.specialdates.common.R
import com.alexstyl.specialdates.events.namedays.NamedayLocale
import com.alexstyl.specialdates.events.peopleevents.EventType
import com.alexstyl.specialdates.events.peopleevents.StandardEventType
import com.alexstyl.specialdates.person.StarSign

class AndroidStrings(private val resources: Resources) : Strings {
    override fun bankholidaySubtitle(): String = resources.getString(R.string.Bank_holiday_subtitle)

    override fun contacts(): String = resources.getString(R.string.contacts)

    override fun namedays(): String = resources.getString(R.string.namedays)

    override fun bankholidays(): String = resources.getString(R.string.Bank_holidays)

    override fun dailyReminder(): String = resources.getString(R.string.daily_reminder)

    override fun postOnFacebook(): String = resources.getString(R.string.Post_on_Facebook)

    override fun facebook(): String = resources.getString(R.string.Facebook)

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

    override fun localeName(locale: NamedayLocale): String = when (locale) {
        NamedayLocale.GREEK -> resources.getString(R.string.Greek)
        NamedayLocale.ROMANIAN -> resources.getString(R.string.Romanian)
        NamedayLocale.RUSSIAN -> resources.getString(R.string.Russian)
        NamedayLocale.LATVIAN -> resources.getString(R.string.Latvian_Traditional)
        NamedayLocale.LATVIAN_EXTENDED -> resources.getString(R.string.Latvian_Extended)
        NamedayLocale.SLOVAK -> resources.getString(R.string.Slovak)
        NamedayLocale.ITALIAN -> resources.getString(R.string.Italian)
        NamedayLocale.CZECH -> resources.getString(R.string.Czech)
        NamedayLocale.HUNGARIAN -> resources.getString(R.string.Hungarian)
    }

    override fun viewFacebookProfile(): String = resources.getString(R.string.View_Facebook_Profile)

    override fun importFromFacebook(): String = resources.getString(R.string.Import_from_Facebook)
}
