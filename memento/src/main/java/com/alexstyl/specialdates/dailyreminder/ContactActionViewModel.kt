package com.alexstyl.specialdates.dailyreminder

class ContactActionViewModel(val id: Int, val label: String, val type: ActionType)

enum class ActionType {
    CALL,
    SEND_WISH
}
