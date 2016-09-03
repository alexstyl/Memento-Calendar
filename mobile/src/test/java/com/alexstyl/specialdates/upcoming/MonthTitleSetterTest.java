package com.alexstyl.specialdates.upcoming;

import android.app.Activity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MonthTitleSetterTest {

    @Mock
    private Activity mockActivity;

    private MonthTitleSetter monthTitleSetter;

    @Before
    public void setup() {
        MonthLabels monthLabels = new MonthLabels(new String[]{"1", "2", "3", "4", "5", "6"});
        monthTitleSetter = new MonthTitleSetter(mockActivity, monthLabels);
    }

    @Test
    public void givenTheIndexOfASpecificMonth_whenUpdatingMonth_thenTheTitleIsProperlySet() {
        monthTitleSetter.updateWithMonth(1);

        verify(mockActivity).setTitle("1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenTheIndexOfΑΜonthOutOfBounds_thenExceptionisThrown() {
        monthTitleSetter.updateWithMonth(13);

        verify(mockActivity).setTitle("13");
    }

}
