package com.alexstyl.specialdates.contact.actions;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class LabeledActionTest {

    @StringRes
    private static final int nameResId = 25;
    @DrawableRes
    private static final int LABEL_ID = 51;
    @Mock
    private Context mockContext;
    @Mock
    IntentAction mockAction;

    @Test
    public void testName() throws Exception {

        LabeledAction action = new LabeledAction(nameResId, mockAction, LABEL_ID);
        action.fire(mockContext);
        verify(mockAction).onStartAction(mockContext);
    }

}
