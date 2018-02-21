package com.alexstyl.specialdates.facebook

interface FacebookUserSettings {

    val isLoggedIn: Boolean
    
    fun store(userCredentials: UserCredentials)

    fun retrieveCredentials(): UserCredentials
}
