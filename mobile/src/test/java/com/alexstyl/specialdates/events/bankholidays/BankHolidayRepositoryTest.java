package com.alexstyl.specialdates.events.bankholidays;

import com.alexstyl.specialdates.events.DayDate;
import com.alexstyl.specialdates.events.namedays.calendar.EasterCalculator;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class BankHolidayRepositoryTest {

    private static final DayDate GREEK_INDEPENDENCE_DAY = DayDate.newInstance(25, DayDate.MARCH, 1990);

    @Mock
    private EasterCalculator calculator;
    private BankHolidayRepository repository;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        repository = new BankHolidayRepository(calculator);
        when(calculator.calculateEasterForYear(1990)).thenReturn(DayDate.newInstance(1, 1, 1990));
        when(calculator.calculateEasterForYear(1991)).thenReturn(DayDate.newInstance(1, 1, 1991));
    }

    @Test
    public void whenCheckingForSameYearTwice_thenEasterIsCalculatedOnlyOnce() {
        repository.calculateBankholidayFor(aDateInYear(1990));
        repository.calculateBankholidayFor(aDateInYear(1990));
        verify(calculator, times(1)).calculateEasterForYear(1990);
    }

    @Test
    public void whenCheckingForDifferentYearthenEasterIsRecalculated() {
        repository.calculateBankholidayFor(aDateInYear(1990));
        repository.calculateBankholidayFor(aDateInYear(1991));
        verify(calculator, times(1)).calculateEasterForYear(1990);
        verify(calculator, times(1)).calculateEasterForYear(1991);
    }

    @Test
    public void testThatAGreekKnownBankholidayIsCalculatedProperly() {
        DayDate date = GREEK_INDEPENDENCE_DAY;
        BankHoliday bankHoliday = repository.calculateBankholidayFor(date);
        assertThat(bankHoliday.getDate()).isEqualTo(DayDate.newInstance(25, DayDate.MARCH, 1990));
    }

    private DayDate aDateInYear(int year) {
        return DayDate.newInstance(1, 1, year);
    }
}
