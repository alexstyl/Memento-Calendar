package com.novoda.simplechromecustomtabs.connection;

import android.support.customtabs.CustomTabsClient;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class ConnectedClientTest {

    @Mock
    private CustomTabsClient mockCustomTabsClient;

    private ConnectedClient connectedClient;

    @Before
    public void setUp() {
        initMocks(this);
        connectedClient = new ConnectedClient(mockCustomTabsClient);
    }

    @Test(expected = IllegalStateException.class)
    public void givenThatClientIsDisconnected_whenStartingNewSession_thenDeveloperErrorIsThrown() {
        connectedClient.disconnect();

        connectedClient.newSession();
    }

    @Test
    public void givenThatClientIsConnected_whenStartingNewSession_thenNewSessionIsStarted() {
        connectedClient.newSession();

        verify(mockCustomTabsClient).newSession(null);
    }

    @Test
    public void givenThatClientIsConnected_whenCheckingIfStillConnected_thenReturnsTrue() {
        boolean stillConnected = connectedClient.stillConnected();

        assertThat(stillConnected).isTrue();
    }

    @Test
    public void givenThatClientIsDisconnected_whenCheckingIfStillConnected_thenReturnsTrue() {
        connectedClient.disconnect();

        boolean stillConnected = connectedClient.stillConnected();

        assertThat(stillConnected).isFalse();
    }

    @Test
    public void givenThatClientIsConnected_whenStartingNewSession_thenClientIsWarmedUp() {
        connectedClient.newSession();

        verify(mockCustomTabsClient).warmup(0);
    }

    @Test(expected = IllegalStateException.class)
    public void givenThatClientIsDisconnected_whenStartingNewSession_thenClientIsNotWarmedUp() {
        connectedClient.disconnect();

        connectedClient.newSession();

        verify(mockCustomTabsClient, never()).warmup(anyInt());
    }

}
