package com.alexstyl.specialdates.person

data class PersonContactViewModel(val events: List<ContactEventViewModel>, val actions: List<ContactActionViewModel>, val messages: List<ContactActionViewModel>)
