package com.novoda.simplechromecustomtabs.connection;

import android.app.Activity;
import android.net.Uri;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class SimpleChromeCustomTabsConnectionTest {

    private static Uri ANY_URI = mock(Uri.class);

    @Mock
    private Binder mockBinder;
    @Mock
    private Activity mockActivity;
    @Mock
    private ConnectedClient mockConnectedClient;
    @Mock
    private Session mockSession;

    private SimpleChromeCustomTabsConnection simpleChromeCustomTabsConnection;

    @Before
    public void setUp() {
        initMocks(this);

        when(mockActivity.getApplicationContext()).thenReturn(RuntimeEnvironment.application);
        when(mockConnectedClient.newSession()).thenReturn(mock(Session.class));

        simpleChromeCustomTabsConnection = new SimpleChromeCustomTabsConnection(mockBinder);
    }

    @Test
    public void whenBindingActivityToService_thenApplicationContextIsBound() {
        simpleChromeCustomTabsConnection.connectTo(mockActivity);

        verify(mockBinder).bindCustomTabsServiceTo(mockActivity.getApplicationContext(), simpleChromeCustomTabsConnection);
    }

    @Test
    public void whenUnbindingActivityToService_thenApplicationContextIsUnbound() {
        simpleChromeCustomTabsConnection.disconnectFrom(mockActivity);

        verify(mockBinder).unbindCustomTabsService(mockActivity.getApplicationContext());
    }

    @Test
    public void givenAConnectedClient_whenOnServiceConnectedIsCalled_thenClientStartsNewSession() {
        givenAConnectedClient();

        simpleChromeCustomTabsConnection.onServiceConnected(mockConnectedClient);

        verify(mockConnectedClient).newSession();
    }

    @Test
    public void givenADisconnectedClient_whenOnServiceConnectedIsCalled_thenClientDoesNotStartNewSession() {
        givenADisconnectedClient();

        simpleChromeCustomTabsConnection.onServiceConnected(mockConnectedClient);

        verify(mockConnectedClient, never()).newSession();
    }

    @Test
    public void givenAnAlreadyStartedSession_whenOnServiceDisconnectedIsCalled_thenClientDisconnects() {
        givenAnAlreadyStartedSession();

        simpleChromeCustomTabsConnection.onServiceDisconnected();

        verify(mockConnectedClient).disconnect();
    }

    @Test
    public void givenADisconnectedClient_whenOnServiceDisconnectedIsCalled_thenTheClientDoesnNotDisconnect() {
        givenADisconnectedClient();

        simpleChromeCustomTabsConnection.onServiceDisconnected();

        verify(mockConnectedClient, never()).disconnect();
    }

    @Test
    public void givenAUrlAwaitingToBeWarmedUp_andAConnectedClient_whenOnServiceConnectedIsCalled_thenUrlIsWarmedUp() {
        givenAUrlAwaitingToBeWarmedUp();
        givenAConnectedClient();

        simpleChromeCustomTabsConnection.onServiceConnected(mockConnectedClient);

        verify(mockSession).mayLaunch(ANY_URI);
    }

    @Test
    public void givenAnAlreadyStartedSession_whenMayLaunchIsCalled_thenUrlIsWarmedUp() {
        givenAnAlreadyStartedSession();

        simpleChromeCustomTabsConnection.mayLaunch(ANY_URI);

        verify(mockSession).mayLaunch(ANY_URI);
    }

    @Test
    public void givenAnAlreadyStartedSession_andAnEmptyUrl_whenMayLaunchIsCalled_thenUrlIsNotWarmedUp() {
        givenAnAlreadyStartedSession();

        simpleChromeCustomTabsConnection.mayLaunch(Uri.EMPTY);

        verifyZeroInteractions(mockSession);
    }

    @Test
    public void givenAnAlreadyStartedSession_andAnNullUrl_whenMayLaunchIsCalled_thenUrlIsNotWarmedUp() {
        givenAnAlreadyStartedSession();

        simpleChromeCustomTabsConnection.mayLaunch(null);

        verifyZeroInteractions(mockSession);
    }

    @Test
    public void givenASessionIsNotStarted_whenMayLaunchIsCalled_thenUrlIsNotWarmedUp() {
        givenASessionIsNotStarted();

        simpleChromeCustomTabsConnection.mayLaunch(ANY_URI);

        verifyZeroInteractions(mockSession);
    }

    private void givenAConnectedClient() {
        when(mockConnectedClient.stillConnected()).thenReturn(true);
        when(mockConnectedClient.newSession()).thenReturn(mockSession);
    }

    private void givenADisconnectedClient() {
        when(mockConnectedClient.stillConnected()).thenReturn(false);
    }

    private void givenAnAlreadyStartedSession() {
        givenAConnectedClient();
        simpleChromeCustomTabsConnection.onServiceConnected(mockConnectedClient);
    }

    private void givenASessionIsNotStarted() {
        //no-op
    }

    private void givenAUrlAwaitingToBeWarmedUp() {
        givenADisconnectedClient();
        simpleChromeCustomTabsConnection.mayLaunch(ANY_URI);
    }

}
