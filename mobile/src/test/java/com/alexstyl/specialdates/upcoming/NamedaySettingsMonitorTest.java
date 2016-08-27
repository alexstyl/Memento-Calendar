package com.alexstyl.specialdates.upcoming;

import com.alexstyl.specialdates.events.namedays.NamedayLocale;
import com.alexstyl.specialdates.events.namedays.NamedayPreferences;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class NamedaySettingsMonitorTest {

    private NamedaySettingsMonitor monitor;

    @Mock
    private NamedayPreferences preferencesMock;

    private static final boolean DEFAULT_NAMEDAYS_ENABLED = true;
    private static final boolean DEFAULT_CONTACTS_ONLY = false;

    private static final NamedayLocale DEFAULT_LOCALE = NamedayLocale.gr;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        initialiseMonitor();
    }

    private void initialiseMonitor() {
        monitor = new NamedaySettingsMonitor(preferencesMock);
        when(preferencesMock.isEnabled()).thenReturn(DEFAULT_NAMEDAYS_ENABLED);
        when(preferencesMock.getSelectedLanguage()).thenReturn(DEFAULT_LOCALE);
        when(preferencesMock.isEnabledForContactsOnly()).thenReturn(DEFAULT_CONTACTS_ONLY);
    }

    @Test
    public void whenSelectedNamedayLanguageChanges_thenMonitorReturnsTrue() {
        when(preferencesMock.getSelectedLanguage()).thenReturn(NamedayLocale.ro);
        assertThat(monitor.dataWasUpdated()).isTrue();

    }

    @Test
    public void whenSameNamedayLanguageIsSelected_thenMonitorReturnsFalse() {
        when(preferencesMock.getSelectedLanguage()).thenReturn(DEFAULT_LOCALE);
        monitor.initialise();
        monitor.refreshData();
        assertThat(monitor.dataWasUpdated()).isFalse();
    }

}
