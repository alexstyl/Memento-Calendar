package com.alexstyl.specialdates.addevent;

import com.alexstyl.specialdates.TestDateLabelCreator;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.events.peopleevents.StandardEventType;

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.fest.assertions.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ContactEventViewModelFactoryTest {

    private ContactEventViewModelFactory factory;

    @Before
    public void setUp() {
        factory = new ContactEventViewModelFactory(TestDateLabelCreator.forUS());
    }

    @Test
    public void alwaysCreatesOneViewModelForEachStandardEvent() {
        int eventsCount = StandardEventType.values().length;
        List<ContactEventViewModel> viewModels = factory.createViewModel(Collections.<ContactEvent>emptyList());
        assertThat(viewModels.size()).isEqualTo(eventsCount);
    }
}
