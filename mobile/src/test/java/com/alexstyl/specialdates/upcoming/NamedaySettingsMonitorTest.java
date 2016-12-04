package com.alexstyl.specialdates.upcoming;

import com.alexstyl.specialdates.events.namedays.NamedayLocale;
import com.alexstyl.specialdates.events.namedays.NamedayPreferences;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NamedaySettingsMonitorTest {

    private NamedaySettingsMonitor monitor;

    @Mock
    private NamedayPreferences mockPreferences;

    private static final boolean DEFAULT_NAMEDAYS_ENABLED = true;
    private static final boolean DEFAULT_CONTACTS_ONLY = false;
    private static final boolean DEFAULT_FULL_NAME = false;

    private static final NamedayLocale DEFAULT_LOCALE = NamedayLocale.GREEK;

    @Before
    public void setUp() throws Exception {
        initialiseMonitor();
    }

    private void initialiseMonitor() {
        when(mockPreferences.isEnabled()).thenReturn(DEFAULT_NAMEDAYS_ENABLED);
        when(mockPreferences.getSelectedLanguage()).thenReturn(DEFAULT_LOCALE);
        when(mockPreferences.isEnabledForContactsOnly()).thenReturn(DEFAULT_CONTACTS_ONLY);
        when(mockPreferences.shouldLookupAllNames()).thenReturn(DEFAULT_FULL_NAME);
        monitor = new NamedaySettingsMonitor(mockPreferences);
    }

    @Test
    public void whenSelectedNamedayLanguageChanges_thenMonitorReturnsTrue() {
        when(mockPreferences.getSelectedLanguage()).thenReturn(NamedayLocale.ROMANIAN);
        assertThat(monitor.dataWasUpdated()).isTrue();
    }

    @Test
    public void whenFullnameChanges_thenMonitorReturnsTrue() {
        when(mockPreferences.shouldLookupAllNames()).thenReturn(true);
        assertThat(monitor.dataWasUpdated()).isTrue();
    }

    @Test
    public void whenSameNamedayLanguageIsSelected_thenMonitorReturnsFalse() {
        when(mockPreferences.getSelectedLanguage()).thenReturn(DEFAULT_LOCALE);
        monitor.initialise();
        monitor.refreshData();
        assertThat(monitor.dataWasUpdated()).isFalse();
    }

}
