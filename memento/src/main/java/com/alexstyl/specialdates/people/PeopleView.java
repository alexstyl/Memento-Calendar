package com.alexstyl.specialdates.people;

import java.util.List;

public interface PeopleView {
    void displayPeople(List<PeopleRowViewModel> viewModels);

    void showLoading();
}
