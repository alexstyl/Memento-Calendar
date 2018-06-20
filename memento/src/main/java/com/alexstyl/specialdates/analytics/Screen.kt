package com.alexstyl.specialdates.analytics

enum class Screen(private val screenName: String) {
    HOME("upcoming"),
    ADD_EVENT("add event"),
    SEARCH("search"),
    SETTINGS("settings"),
    DATE_DETAILS("date details"),
    DONATE("donate"),
    CONTACT_PERMISSION_REQUESTED("contact permission"),
    WEAR_CONTACT_EVENTS("wear: contacts events"),
    PLAY_STORE("playstore"),
    GOOGLE_PLUS_COMMUNITY("google plus community"),
    EMAIL_SUPPORT("email support"),
    FACEBOOK_PROFILE("facebook_profile"),
    FACEBOOK_LOG_IN("facebook_login"),
    FACEBOOK_PAGE("facebook_page_url"),
    NAMEDAYS("namedays"),
    PERSON("person");

    fun screenName(): String {
        return screenName
    }

    override fun toString(): String {
        return screenName
    }
}
