package com.alexstyl.specialdates.events.namedays;

import com.alexstyl.specialdates.date.AnnualEvent;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class NameCelebrationsTest {

    @Test
    public void testName() throws Exception {
        NameCelebrations celebrations = new NameCelebrations("Alex", new AnnualEvent(1, 12));
    }
}
