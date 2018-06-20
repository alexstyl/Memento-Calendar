package com.alexstyl.specialdates.people

class NoContactsViewModel : PeopleRowViewModel {

    override fun equals(other: Any?): Boolean {
        return other is NoContactsViewModel
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

}
