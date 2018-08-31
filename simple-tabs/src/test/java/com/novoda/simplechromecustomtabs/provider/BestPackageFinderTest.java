package com.novoda.simplechromecustomtabs.provider;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(RobolectricTestRunner.class)
public class BestPackageFinderTest {

    private static final String DEFAULT_PACKAGE = "default.package";
    private static final String OTHER_PACKAGE = "other.package";

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private PackageManager mockPackageManager;
    @Mock
    private Activity mockActivity;

    private BestPackageFinder bestPackageFinder;

    @Before
    public void setUp() {
        bestPackageFinder = new BestPackageFinder();
        when(mockActivity.getPackageManager()).thenReturn(mockPackageManager);
    }

    @Test
    public void returnsEmptyPackageIfNoPackageSupportingCustomTabsIsFound() {
        givenThatThereIsNoPackagesSupportingCustomTabs();

        assertThat(bestPackageFinder.findBestPackage(mockActivity)).isEmpty();
    }

    @Test
    public void returnsDefaultPackageIfPresentOnTheListOfPackagesSupportingCustomTabs() {
        givenThatDefaultPackageIs(DEFAULT_PACKAGE);
        givenThatPackagesSupportingCustomTabsIs(DEFAULT_PACKAGE);

        assertThat(bestPackageFinder.findBestPackage(mockActivity)).isEqualTo(DEFAULT_PACKAGE);
    }

    @Test
    public void returnsFirstInListOfPackagesSupportingCustomTabsIfDefaultNotPresent() {
        givenThatDefaultPackageIs(DEFAULT_PACKAGE);
        givenThatPackagesSupportingCustomTabsIs(OTHER_PACKAGE);

        assertThat(bestPackageFinder.findBestPackage(mockActivity)).isEqualTo(OTHER_PACKAGE);
    }

    private void givenThatThereIsNoPackagesSupportingCustomTabs() {
        when(mockPackageManager.queryIntentActivities(any(Intent.class), anyInt())).thenReturn(Collections.<ResolveInfo>emptyList());
    }

    private void givenThatDefaultPackageIs(String packageName) {
        ResolveInfo resolveInfo = createResolveInfoFor(packageName);

        when(mockPackageManager.resolveActivity(any(Intent.class), anyInt())).thenReturn(resolveInfo);
    }

    private ResolveInfo createResolveInfoFor(String packageName) {
        ResolveInfo resolveInfo = new ResolveInfo();
        resolveInfo.activityInfo = new ActivityInfo();
        resolveInfo.activityInfo.packageName = packageName;

        return resolveInfo;
    }

    private void givenThatPackagesSupportingCustomTabsIs(String packageName) {
        List<ResolveInfo> resolveInfoList = new ArrayList<>();
        ResolveInfo defaultPackageResolveInfo = createResolveInfoFor(packageName);
        resolveInfoList.add(defaultPackageResolveInfo);

        when(mockPackageManager.queryIntentActivities(any(Intent.class), anyInt())).thenReturn(resolveInfoList);
        when(mockPackageManager.resolveService(any(Intent.class), anyInt())).thenReturn(defaultPackageResolveInfo);
    }

}
