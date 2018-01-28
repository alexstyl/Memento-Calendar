package com.alexstyl.specialdates.person;

interface PersonView {
    void displayPersonInfo(PersonInfoViewModel viewModel);
    void displayAvailableActions(PersonAvailableActionsViewModel viewModel);

    void showPersonAsVisible();
    void showPersonAsHidden();
}
