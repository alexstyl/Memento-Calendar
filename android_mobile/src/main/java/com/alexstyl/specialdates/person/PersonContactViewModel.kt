package com.alexstyl.specialdates.person

data class PersonContactViewModel(val events: List<ContactEventViewModel>, val calls: List<ContactPhoneViewModel>, val messages: List<ContactPhoneViewModel>)
