package com.novoda.simplechromecustomtabs.navigation;

import android.app.Activity;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;

import com.novoda.simplechromecustomtabs.connection.Connection;
import com.novoda.simplechromecustomtabs.connection.Session;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class SimpleChromeCustomTabsWebNavigatorTest {

    private static final Uri ANY_URL = Uri.EMPTY;
    private static final CustomTabsIntent ANY_INTENT = new CustomTabsIntent.Builder().build();

    @Mock
    private Connection mockConnection;
    @Mock
    private IntentCustomizer mockIntentCustomizer;
    @Mock
    private NavigationFallback mockNavigationFallback;
    @Mock
    private Activity mockActivity;
    @Mock
    private SimpleChromeCustomTabsIntentBuilder mockSimpleChromeCustomTabsIntentBuilder;

    private SimpleChromeCustomTabsWebNavigator webNavigator;

    @Before
    public void setUp() {
        initMocks(this);

        when(mockIntentCustomizer.onCustomiseIntent(any(SimpleChromeCustomTabsIntentBuilder.class))).thenReturn(mockSimpleChromeCustomTabsIntentBuilder);
        when(mockSimpleChromeCustomTabsIntentBuilder.createIntent()).thenReturn(ANY_INTENT);
        when(mockConnection.getSession()).thenReturn(Session.NULL_SESSION);

        webNavigator = new SimpleChromeCustomTabsWebNavigator(mockConnection);
    }

    @Test
    public void givenThereIsNoConnection_whenNavigatingWithFallback_thenNavigatesWithFallback() {
        when(mockConnection.isConnected()).thenReturn(false);

        webNavigator.withFallback(mockNavigationFallback).navigateTo(ANY_URL, mockActivity);

        verify(mockNavigationFallback).onFallbackNavigateTo(ANY_URL);
    }

    @Test
    public void givenThereIsNoConnection_whenNavigatingWithoutFallback_thenNothingHappens() {
        when(mockConnection.isConnected()).thenReturn(false);

        webNavigator.navigateTo(ANY_URL, mockActivity);

        verifyZeroInteractions(mockNavigationFallback);
    }

    @Test
    public void givenThereIsAConnection_whenNavigatingWithFallback_thenNavigatesWithoutFallback() {
        when(mockConnection.isConnected()).thenReturn(true);

        webNavigator.withFallback(mockNavigationFallback).navigateTo(ANY_URL, mockActivity);

        verifyZeroInteractions(mockNavigationFallback);
    }

    @Test
    public void givenThereIsAConnection_whenNavigatingWithIntentCustomizer_thenIntentIsCustomised() {
        when(mockConnection.isConnected()).thenReturn(true);

        webNavigator.withIntentCustomizer(mockIntentCustomizer).navigateTo(ANY_URL, mockActivity);

        verify(mockIntentCustomizer).onCustomiseIntent(any(SimpleChromeCustomTabsIntentBuilder.class));
    }

    @Test
    public void givenThereAreCallbacks_whenReleaseIsCalled_thenCallbacksAreReleased() {
        webNavigator.withFallback(mockNavigationFallback).withIntentCustomizer(mockIntentCustomizer);

        webNavigator.release();

        assertThat(webNavigator.intentCustomizer).isNull();
        assertThat(webNavigator.navigationFallback).isNull();
    }

}
