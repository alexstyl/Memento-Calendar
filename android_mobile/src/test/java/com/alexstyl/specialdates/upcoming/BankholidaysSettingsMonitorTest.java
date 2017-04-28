package com.alexstyl.specialdates.upcoming;

import com.alexstyl.specialdates.events.bankholidays.BankHolidaysPreferences;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.Assert.assertFalse;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BankholidaysSettingsMonitorTest {

    private BankHolidaysMonitor monitor;
    @Mock
    private BankHolidaysPreferences mockPreferences;

    @Before
    public void setUp() {
        monitor = new BankHolidaysMonitor(mockPreferences);
        when(mockPreferences.isEnabled()).thenReturn(true);
        monitor.initialise();
    }

    @Test
    public void whenIsEnabledSettingIsUpdated_thenHasChangedReturnsTrue() {
        assertFalse(monitor.dataWasUpdated());
        when(mockPreferences.isEnabled()).thenReturn(false);
        assertThat(monitor.dataWasUpdated()).isTrue();

    }

    @Test
    public void refreshingData_neverReturnsUpdates() {
        monitor.refreshData();
        assertThat(monitor.dataWasUpdated()).isFalse();
    }
}
