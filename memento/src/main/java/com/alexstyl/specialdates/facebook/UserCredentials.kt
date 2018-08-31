package com.alexstyl.specialdates.facebook

data class UserCredentials(val uid: Long, val key: String, val name: String) {

    val isAnnonymous: Boolean
        get() = ANONYMOUS == this

    companion object {
        @JvmField
        val ANONYMOUS = UserCredentials(-1, "", "")
    }
}
