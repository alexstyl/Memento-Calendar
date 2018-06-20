package com.alexstyl.specialdates.person

interface PersonView {
    fun displayPersonInfo(viewModel: PersonInfoViewModel)
    fun displayAvailableActions(viewModel: PersonAvailableActionsViewModel)

    fun showPersonAsVisible()
    fun showPersonAsHidden()
}
