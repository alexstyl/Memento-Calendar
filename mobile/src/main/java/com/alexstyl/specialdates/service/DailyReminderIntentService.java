package com.alexstyl.specialdates.service;

import android.Manifest;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.alexstyl.specialdates.BuildConfig;
import com.alexstyl.specialdates.date.DayDate;
import com.alexstyl.specialdates.dailyreminder.DailyReminderDebugPreferences;
import com.alexstyl.specialdates.events.ContactEvents;
import com.alexstyl.specialdates.events.bankholidays.BankHoliday;
import com.alexstyl.specialdates.events.bankholidays.BankHolidaysPreferences;
import com.alexstyl.specialdates.events.bankholidays.GreekBankHolidays;
import com.alexstyl.specialdates.events.namedays.NamedayLocale;
import com.alexstyl.specialdates.events.namedays.NamedayPreferences;
import com.alexstyl.specialdates.events.namedays.NamesInADate;
import com.alexstyl.specialdates.events.namedays.calendar.EasterCalculator;
import com.alexstyl.specialdates.events.namedays.calendar.NamedayCalendar;
import com.alexstyl.specialdates.events.namedays.calendar.resource.NamedayCalendarProvider;
import com.alexstyl.specialdates.permissions.PermissionChecker;
import com.alexstyl.specialdates.receiver.EventReceiver;
import com.alexstyl.specialdates.settings.MainPreferenceActivity;
import com.alexstyl.specialdates.util.Notifier;
import com.novoda.notils.logger.simple.Log;

import java.util.Calendar;
import java.util.List;

/**
 * A service that looks up all events on the specified date and notifies the user about it
 * <p>NOTE: The DailyReminder service will display</p>
 */
public class DailyReminderIntentService extends IntentService {

    private static final int REQUEST_CODE = 0;
    private NamedayPreferences namedayPreferences;
    private NamedayCalendarProvider namedayCalendarProvider;

    private BankHolidaysPreferences bankHolidaysPreferences;
    private PermissionChecker checker;

    public DailyReminderIntentService() {
        super("DailyReminder");
    }

    private Notifier notifier;

    public static void startService(Context context) {
        Intent service = new Intent(context, DailyReminderIntentService.class);
        context.startService(service);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        notifier = Notifier.newInstance(this);
        namedayPreferences = NamedayPreferences.newInstance(this);
        namedayCalendarProvider = NamedayCalendarProvider.newInstance(this.getResources());
        bankHolidaysPreferences = BankHolidaysPreferences.newInstance(this);
        checker = new PermissionChecker(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        rescheduleAlarm(this);
        PeopleEventsProvider provider = PeopleEventsProvider.newInstance(this);
        DayDate today = getDayDateToDisplay();

        if (hasContactPermission()) {
            ContactEvents celebrationDate = provider.getCelebrationDateFor(today);
            if (containsAnyContactEvents(celebrationDate)) {
                notifier.forDailyReminder(celebrationDate);
            }
        }
        if (namedaysAreEnabledForAllCases()) {
            notifyForNamedaysFor(today);
        }
        if (bankholidaysAreEnabled()) {
            notifyForBankholidaysFor(today);
        }
    }

    private boolean hasContactPermission() {
        return checker.hasPermission(Manifest.permission.READ_CONTACTS);
    }

    private DayDate getDayDateToDisplay() {
        if (BuildConfig.DEBUG) {
            DailyReminderDebugPreferences preferences = DailyReminderDebugPreferences.newInstance(this);
            if (preferences.isFakeDateEnabled()) {
                DayDate selectedDate = preferences.getSelectedDate();
                Log.d("Using DEBUG date to display: " + selectedDate);
                return selectedDate;
            }
        }
        return DayDate.today();
    }

    private boolean containsAnyContactEvents(ContactEvents celebrationDate) {
        return celebrationDate.size() > 0;
    }

    private boolean namedaysAreEnabledForAllCases() {
        return namedayPreferences.isEnabled() && !namedayPreferences.isEnabledForContactsOnly();
    }

    private void notifyForNamedaysFor(DayDate date) {
        NamedayLocale locale = namedayPreferences.getSelectedLanguage();
        NamedayCalendar namedayCalendar = namedayCalendarProvider.loadNamedayCalendarForLocale(locale, date.getYear());
        NamesInADate names = namedayCalendar.getAllNamedayOn(date);
        if (containsNames(names)) {
            notifier.forNamedays(names.getNames(), date);
        }
    }

    private boolean bankholidaysAreEnabled() {
        return bankHolidaysPreferences.isEnabled();
    }

    private void notifyForBankholidaysFor(DayDate date) {
        BankHoliday bankHoliday = findBankholidayFor(date);
        if (bankHoliday != null) {
            notifier.forBankholiday(date, bankHoliday);
        }
    }

    private BankHoliday findBankholidayFor(DayDate date) {
        EasterCalculator calculator = new EasterCalculator();
        DayDate easter = calculator.calculateEasterForYear(date.getYear());
        List<BankHoliday> bankHolidays = new GreekBankHolidays(easter).getBankHolidaysForYear();
        for (BankHoliday bankHoliday : bankHolidays) {
            if (bankHoliday.getDate().equals(date)) {
                return bankHoliday;
            }
        }
        return null;
    }

    private boolean containsNames(NamesInADate names) {
        return names.nameCount() > 0;
    }

    public static void resetAlarm(Context context) {
        cancelAlarm(context);
        rescheduleAlarm(context);
    }

    public static void rescheduleAlarm(Context context) {
        context = context.getApplicationContext();

        Intent myIntent = new Intent(context, EventReceiver.class);
        myIntent.setAction(EventReceiver.ACTION_START_DAILYREMINDER);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, REQUEST_CODE, myIntent, 0
        );

        Calendar firingCal = Calendar.getInstance();
        Calendar currentCal = Calendar.getInstance();

        int[] time = MainPreferenceActivity.getDailyReminderTime(context);

        firingCal.set(Calendar.HOUR_OF_DAY, time[0]);
        firingCal.set(Calendar.MINUTE, time[1]);
        long intendedTime = firingCal.getTimeInMillis();
        long currentTime = currentCal.getTimeInMillis();

        if (intendedTime <= currentTime) {
            // we missed this one!
            firingCal.add(Calendar.DAY_OF_MONTH, 1);
        }
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(
                AlarmManager.RTC, firingCal.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent
        );

    }

    public static boolean isNextAlarmSet(Context context) {
        return (PendingIntent.getBroadcast(
                context, REQUEST_CODE,
                new Intent(EventReceiver.ACTION_START_DAILYREMINDER),
                PendingIntent.FLAG_NO_CREATE
        ) != null);
    }

    public static void cancelAlarm(Context context) {
        context = context.getApplicationContext();
        Intent myIntent = new Intent(context, EventReceiver.class);
        myIntent.setAction(EventReceiver.ACTION_START_DAILYREMINDER);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, REQUEST_CODE, myIntent, 0
        );
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    public static void setup(Context context) {
        if (MainPreferenceActivity.isDailyReminderSet(context) && !isNextAlarmSet(context)) {
            rescheduleAlarm(context);
        }
    }
}
