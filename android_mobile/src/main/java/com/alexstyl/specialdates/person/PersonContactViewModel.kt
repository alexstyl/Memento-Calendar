package com.alexstyl.specialdates.person

data class PersonContactViewModel(val events: List<ContactEventViewModel>, val calls: List<ContactCallViewModel>, val messages: List<ContactCallViewModel>)
