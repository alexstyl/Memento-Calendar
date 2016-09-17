package com.alexstyl.specialdates.upcoming;

import android.app.Activity;

import com.alexstyl.specialdates.Navigator;
import com.alexstyl.specialdates.permissions.ContactPermissionRequest;
import com.alexstyl.specialdates.permissions.ContactPermissionRequest.PermissionCallbacks;
import com.alexstyl.specialdates.permissions.PermissionChecker;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ContactPermissionRequestTest {

    private static final int CODE = ContactPermissionRequest.CONTACT_REQUEST;

    @Mock
    private Navigator mockNavigator;
    @Mock
    private PermissionCallbacks mockCallback;
    @Mock
    private PermissionChecker mockChecker;

    private ContactPermissionRequest permissions;

    @Before
    public void setUp() {
        permissions = new ContactPermissionRequest(mockNavigator, mockChecker, mockCallback);
        Mockito.doNothing().when(mockNavigator).toContactPermissionRequired(CODE);
    }

    @Test
    public void whenPermissionIsGrantedCallbackIsCalled() {
        permissions.onActivityResult(CODE, Activity.RESULT_OK, null);
        verify(mockCallback).onPermissionGranted();
    }

    @Test
    public void whenPermissionIsDeniedCallbackIsCalled() {
        permissions.onActivityResult(CODE, Activity.RESULT_CANCELED, null);

        verify(mockCallback).onPermissionDenied();
    }

}
