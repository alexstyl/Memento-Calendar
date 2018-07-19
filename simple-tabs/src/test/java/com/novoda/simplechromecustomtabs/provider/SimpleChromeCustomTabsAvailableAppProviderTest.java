package com.novoda.simplechromecustomtabs.provider;

import android.app.Activity;

import java.util.concurrent.Executor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class SimpleChromeCustomTabsAvailableAppProviderTest {

    private static final String NON_EMPTY_PACKAGE = "non.empty.package";

    @Mock
    private BestPackageFinder mockBestPackageFinder;
    @Mock
    private SimpleChromeCustomTabsAvailableAppProvider.PackageFoundCallback mockPackageFoundCallback;
    @Mock
    private Activity mockActivity;

    private SimpleChromeCustomTabsAvailableAppProvider simpleChromeCustomTabsAvailableAppProvider;

    @Before
    public void setUp() {
        initMocks(this);

        simpleChromeCustomTabsAvailableAppProvider = new SimpleChromeCustomTabsAvailableAppProvider(mockBestPackageFinder, new StubExecutor());
    }

    @Test
    public void findBestPackageDelegatesToPackageFinder() {
        simpleChromeCustomTabsAvailableAppProvider.findBestPackage(mockPackageFoundCallback, mockActivity);

        verify(mockBestPackageFinder).findBestPackage(mockActivity);
    }

    @Test
    public void packageIsFoundIfNotNullOrEmpty() {
        givenThatPackageNameIsNotNullOrEmpty();

        simpleChromeCustomTabsAvailableAppProvider.findBestPackage(mockPackageFoundCallback, mockActivity);

        verify(mockPackageFoundCallback).onPackageFound(NON_EMPTY_PACKAGE);
    }

    @Test
    public void packageIsNotFoundIfNull() {
        givenThatPackageIsNull();

        simpleChromeCustomTabsAvailableAppProvider.findBestPackage(mockPackageFoundCallback, mockActivity);

        verify(mockPackageFoundCallback).onPackageNotFound();
    }

    @Test
    public void packageNotFoundIfEmpty() {
        givenThatPackageIsEmpty();

        simpleChromeCustomTabsAvailableAppProvider.findBestPackage(mockPackageFoundCallback, mockActivity);

        verify(mockPackageFoundCallback).onPackageNotFound();
    }

    @Test
    public void ifPackageIsFoundThenPackageNotFoundWillNotBeCalled() {
        givenThatPackageIsFound();

        simpleChromeCustomTabsAvailableAppProvider.findBestPackage(mockPackageFoundCallback, mockActivity);

        verify(mockPackageFoundCallback, never()).onPackageNotFound();
    }

    @Test
    public void ifPackageIsNotFoundThenPackageFoundWillNotBeCalled() {
        givenThatPackageIsNotFound();

        simpleChromeCustomTabsAvailableAppProvider.findBestPackage(mockPackageFoundCallback, mockActivity);

        verify(mockPackageFoundCallback, never()).onPackageFound(anyString());
    }

    private void givenThatPackageNameIsNotNullOrEmpty() {
        when(mockBestPackageFinder.findBestPackage(mockActivity)).thenReturn(NON_EMPTY_PACKAGE);
    }

    private void givenThatPackageIsNull() {
        when(mockBestPackageFinder.findBestPackage(mockActivity)).thenReturn(null);
    }

    private void givenThatPackageIsEmpty() {
        when(mockBestPackageFinder.findBestPackage(mockActivity)).thenReturn("");
    }

    private void givenThatPackageIsFound() {
        givenThatPackageNameIsNotNullOrEmpty();
    }

    private void givenThatPackageIsNotFound() {
        givenThatPackageIsEmpty();
    }

    private static class StubExecutor implements Executor {
        @Override
        public void execute(Runnable command) {
            command.run();
        }
    }

}
