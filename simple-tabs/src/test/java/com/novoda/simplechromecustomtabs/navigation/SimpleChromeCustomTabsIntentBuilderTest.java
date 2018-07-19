package com.novoda.simplechromecustomtabs.navigation;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.customtabs.CustomTabsIntent;

import com.novoda.simplechromecustomtabs.connection.Connection;
import com.novoda.simplechromecustomtabs.connection.Session;

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class SimpleChromeCustomTabsIntentBuilderTest {

    private static final int ANY_ANIM_RES = 0;
    private static final int ANY_COLOR = 0;
    private static final android.app.PendingIntent ANY_PENDING_INTENT = null;
    private static final String ANY_LABEL = "anyLabel";
    private static final String ANY_DESCRIPTION = "any_description";
    private static final Bitmap ANY_ICON = null;
    private static final Context ANY_CONTEXT = RuntimeEnvironment.application;

    @Mock
    private Connection mockConnection;
    @Mock
    private List<Composer> mockComposers;

    private SimpleChromeCustomTabsIntentBuilder simpleChromeCustomTabsIntentBuilder;

    @Before
    public void setUp() {
        initMocks(this);

        simpleChromeCustomTabsIntentBuilder = new SimpleChromeCustomTabsIntentBuilder(mockConnection, mockComposers);
        when(mockComposers.iterator()).thenReturn(Collections.<Composer>emptyListIterator());
        when(mockConnection.getSession()).thenReturn(mock(Session.class));
    }

    @Test(expected = IllegalStateException.class)
    public void givenThereIsNoConnection_whenCreatingIntent_thenDeveloperErrorIsThrown() {
        givenIsDisconnected();

        simpleChromeCustomTabsIntentBuilder.createIntent();
    }

    @Test
    public void givenThereIsAConnection_whenCreatingIntent_thenSessionIsRetrieved() {
        givenIsConnected();

        simpleChromeCustomTabsIntentBuilder.createIntent();

        verify(mockConnection).getSession();
    }

    @Test
    public void givenThereIsAConnection_whenCreatingIntent_thenIntentIsCreated() {
        givenIsConnected();

        CustomTabsIntent resultIntent = simpleChromeCustomTabsIntentBuilder.createIntent();

        assertThat(resultIntent).isNotNull();
    }

    @Test
    public void givenToolbarColorIsSet_thenToolbarColorComposerIsAdded() {
        simpleChromeCustomTabsIntentBuilder.withToolbarColor(ANY_COLOR);

        verify(mockComposers).add(any(ToolbarColorComposer.class));
    }

    @Test
    public void givenUrlBarHidingIsSet_thenUrlBarHidingComposerIsAdded() {
        simpleChromeCustomTabsIntentBuilder.withUrlBarHiding();

        verify(mockComposers).add(any(UrlBarHidingComposer.class));
    }

    @Test
    public void givenMenuItemIsSet_thenMenuItemComposerIsAdded() {
        simpleChromeCustomTabsIntentBuilder.withMenuItem(ANY_LABEL, ANY_PENDING_INTENT);

        verify(mockComposers).add(any(MenuItemComposer.class));
    }

    @Test
    public void givenDefaultShareMenuItemIsSet_thenDefaultShareMenuItemComposerIsAdded() {
        simpleChromeCustomTabsIntentBuilder.withDefaultShareMenuItem();

        verify(mockComposers).add(any(DefaultShareMenuItemComposer.class));
    }

    @Test
    public void givenActionButtonIsSet_thenActionButtonComposerIsAdded() {
        simpleChromeCustomTabsIntentBuilder.withActionButton(ANY_ICON, ANY_DESCRIPTION, ANY_PENDING_INTENT, false);

        verify(mockComposers).add(any(ActionButtonComposer.class));
    }

    @Test
    public void givenCloseButtonIconIsSet_thenCloseButtonIconComposerIsAdded() {
        simpleChromeCustomTabsIntentBuilder.withCloseButtonIcon(ANY_ICON);

        verify(mockComposers).add(any(CloseButtonIconComposer.class));
    }

    @Test
    public void givenExitAnimationIsSet_thenExitAnimationComposerIsAdded() {
        simpleChromeCustomTabsIntentBuilder.withExitAnimations(ANY_CONTEXT, ANY_ANIM_RES, ANY_ANIM_RES);

        verify(mockComposers).add(any(ExitAnimationsComposer.class));
    }

    @Test
    public void givenStartAnimationIsSet_thenStartAnimationComposerIsAdded() {
        simpleChromeCustomTabsIntentBuilder.withStartAnimations(ANY_CONTEXT, ANY_ANIM_RES, ANY_ANIM_RES);

        verify(mockComposers).add(any(StartAnimationsComposer.class));
    }

    @Test
    public void givenShowingTitleIsSet_thenShowingTitleComposerIsAdded() {
        simpleChromeCustomTabsIntentBuilder.showingTitle();

        verify(mockComposers).add(any(ShowTitleComposer.class));
    }

    private void givenIsDisconnected() {
        when(mockConnection.isDisconnected()).thenReturn(true);
    }

    private void givenIsConnected() {
        when(mockConnection.isConnected()).thenReturn(true);
    }

}
