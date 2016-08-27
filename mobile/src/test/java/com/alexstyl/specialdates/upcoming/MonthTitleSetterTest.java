package com.alexstyl.specialdates.upcoming;

import android.app.Activity;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;


public class MonthTitleSetterTest {

    @Mock
    private Activity mockActivity;

    private MonthTitleSetter monthTitleSetter;

    @Before
    public void setup() {
        initMocks(this);
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