package com.alexstyl.specialdates.people

interface PeopleView {
    fun displayPeople(viewModels: List<PeopleRowViewModel>)
    fun showLoading()
}
