package com.alexstyl.specialdates.upcoming;

import com.alexstyl.resources.ColorResources;
import com.alexstyl.resources.DumbTestResources;
import com.alexstyl.resources.StringResources;
import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.Months;
import com.alexstyl.specialdates.date.TimePeriod;
import com.alexstyl.specialdates.events.bankholidays.BankHoliday;
import com.alexstyl.specialdates.events.namedays.NamesInADate;
import com.alexstyl.specialdates.events.peopleevents.StandardEventType;
import com.alexstyl.specialdates.upcoming.widget.list.NoAds;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.alexstyl.specialdates.contact.ContactFixture.aContact;
import static com.alexstyl.specialdates.date.Months.*;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UpcomingRowViewModelsBuilderTest {

    private static final Optional<Long> NO_DEVICE_EVENT_ID = Optional.absent();

    private UpcomingEventRowViewModelFactory upcomingEventRowViewModelFactory;

    @Mock
    private ColorResources mockColorResources;
    @Mock
    private StringResources mockStringResources;

    @Before
    public void setUp() {
        when(mockColorResources.getColor(anyInt())).thenReturn(5);
        when(mockStringResources.getString(anyInt())).thenReturn("mock-string");
        when(mockStringResources.getString(anyInt(), Matchers.any())).thenReturn("mock-string");

        Date today = Date.Companion.today();
        upcomingEventRowViewModelFactory = new UpcomingEventRowViewModelFactory(
                today,
                new UpcomingDateStringCreator(new DumbTestResources(), today),
                new ContactViewModelFactory(mockColorResources, mockStringResources)
        );

    }

    @Test
    public void givenASingleContactEvent_thenCreatesADateHeaderPlusAContactEvent() {
        ContactEvent event = aContactEventOn(Date.Companion.on(1, JANUARY, 2016));

        List<UpcomingRowViewModel> viewModels = builderFor(entireYear(2016))
                .withContactEvents(singletonList(event))
                .build();

        assertThat(viewModels.size()).isEqualTo(2);
        assertThat(viewModels.get(0)).isInstanceOf(DateHeaderViewModel.class);
        assertThat(viewModels.get(1)).isInstanceOf(UpcomingContactEventViewModel.class);
    }

    @Test
    public void givenTwoContactEvents_whenBothAreOnTheSameDate_thenCreatesADateHeaderPlusTwoContactEvents() {
        List<UpcomingRowViewModel> viewModels =
                builderFor(entireYear(1990))
                        .withContactEvents(asList(
                                aContactEventOn(Date.Companion.on(1, FEBRUARY, 1990)),
                                aContactEventOn(Date.Companion.on(1, FEBRUARY, 1990))
                        ))
                        .build();

        assertThat(viewModels.size()).isEqualTo(3);
        assertThat(viewModels.get(0)).isInstanceOf(DateHeaderViewModel.class);
        assertThat(viewModels.get(1)).isInstanceOf(UpcomingContactEventViewModel.class);
        assertThat(viewModels.get(2)).isInstanceOf(UpcomingContactEventViewModel.class);
    }

    @Test
    public void givenTwoContactEvents_whenBothAreOnDifferentDate_thenCreatesADateHeaderAndAContactEventForEachOne() {
        List<UpcomingRowViewModel> viewModels =
                builderFor(entireYear(1990))
                        .withContactEvents(asList(
                                aContactEventOn(Date.Companion.on(1, FEBRUARY, 1990)),
                                aContactEventOn(Date.Companion.on(3, FEBRUARY, 1990))
                        ))
                        .build();

        assertThat(viewModels.size()).isEqualTo(4);
        assertThat(viewModels.get(0)).isInstanceOf(DateHeaderViewModel.class);
        assertThat(viewModels.get(1)).isInstanceOf(UpcomingContactEventViewModel.class);
        assertThat(viewModels.get(2)).isInstanceOf(DateHeaderViewModel.class);
        assertThat(viewModels.get(3)).isInstanceOf(UpcomingContactEventViewModel.class);
    }

    @Test
    public void givenABankHoliday_thenCreatesADateHeaderPlusABankholidayModel() {
        List<BankHoliday> bankHolidays = singletonList(aBankHoliday());
        List<UpcomingRowViewModel> viewModels = builderFor(TimePeriod.Companion.between(aBankHoliday().getDate(), aBankHoliday().getDate()))
                .withBankHolidays(bankHolidays)
                .build();

        assertThat(viewModels.size()).isEqualTo(2);
        assertThat(viewModels.get(0)).isInstanceOf(DateHeaderViewModel.class);
        assertThat(viewModels.get(1)).isInstanceOf(BankHolidayViewModel.class);
    }

    @Test
    public void givenANameday_thenCreatesADateHeaderPlusANamedayModel() {
        Date date = Date.Companion.on(1, Months.APRIL, 2017);
        List<NamesInADate> namedays = namedayOf(date, "Maria");

        List<UpcomingRowViewModel> viewModels = builderFor(TimePeriod.Companion.between(date, date))
                .withNamedays(namedays)
                .build();

        assertThat(viewModels.size()).isEqualTo(2);
        assertThat(viewModels.get(0)).isInstanceOf(DateHeaderViewModel.class);
        assertThat(viewModels.get(1)).isInstanceOf(NamedaysViewModel.class);
    }

    @Test
    public void theDisplayOrderIsCorrect() {
        Date date = Date.Companion.on(1, Months.APRIL, 2017);

        BankHoliday bankHoliday = new BankHoliday("A bank holiday", date);
        List<BankHoliday> bankHolidays = singletonList(bankHoliday);
        List<NamesInADate> namedays = namedayOf(date, "Maria");

        List<UpcomingRowViewModel> viewModels = builderWithAds(entireYear(2017))
                .withContactEvents(asList(
                        aContactEventOn(Date.Companion.on(1, Months.APRIL, 2017)),
                        aContactEventOn(Date.Companion.on(2, Months.APRIL, 2017))
                ))
                .withNamedays(namedays)
                .withBankHolidays(bankHolidays)
                .build();

        assertThat(viewModels.size()).isEqualTo(7);
        assertThat(viewModels.get(0)).isInstanceOf(DateHeaderViewModel.class);
        assertThat(viewModels.get(1)).isInstanceOf(BankHolidayViewModel.class);
        assertThat(viewModels.get(2)).isInstanceOf(NamedaysViewModel.class);
        assertThat(viewModels.get(3)).isInstanceOf(UpcomingContactEventViewModel.class);
        assertThat(viewModels.get(4)).isInstanceOf(AdViewModel.class);
        assertThat(viewModels.get(5)).isInstanceOf(DateHeaderViewModel.class);
        assertThat(viewModels.get(6)).isInstanceOf(UpcomingContactEventViewModel.class);
    }

    private List<NamesInADate> namedayOf(Date date, String maria) {
        List<NamesInADate> namedays = new ArrayList<>();
        namedays.add(new NamesInADate(date, singletonList(maria)));
        return namedays;
    }

    private UpcomingRowViewModelsBuilder builderFor(TimePeriod timePeriod) {
        return new UpcomingRowViewModelsBuilder(timePeriod, upcomingEventRowViewModelFactory, new NoAds());
    }

    private UpcomingRowViewModelsBuilder builderWithAds(TimePeriod timePeriod) {
        return new UpcomingRowViewModelsBuilder(timePeriod, upcomingEventRowViewModelFactory, new UpcomingEventsFreeUserAdRules());
    }

    private static BankHoliday aBankHoliday() {
        return new BankHoliday("A bank holiday", Date.Companion.on(1, JANUARY, 1990));
    }

    private static ContactEvent aContactEventOn(Date date) {
        return new ContactEvent(NO_DEVICE_EVENT_ID, StandardEventType.BIRTHDAY, date, aContact());
    }

    private static TimePeriod entireYear(int year) {
        return TimePeriod.Companion.between(Date.Companion.startOfYear(year), Date.Companion.endOfYear(year));
    }

}
