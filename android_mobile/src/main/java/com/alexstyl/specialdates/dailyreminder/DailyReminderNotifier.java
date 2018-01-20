package com.alexstyl.specialdates.dailyreminder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.v4.app.NotificationCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.StyleSpan;

import com.alexstyl.android.Version;
import com.alexstyl.resources.ColorResources;
import com.alexstyl.resources.DimensionResources;
import com.alexstyl.specialdates.Strings;
import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.events.bankholidays.BankHoliday;
import com.alexstyl.specialdates.events.namedays.activity.NamedayActivity;
import com.alexstyl.specialdates.images.ImageLoader;
import com.alexstyl.specialdates.upcoming.UpcomingEventsActivity;
import com.alexstyl.specialdates.util.NaturalLanguageUtils;

import java.util.ArrayList;
import java.util.List;

public final class DailyReminderNotifier {

    private static final int NOTIFICATION_ID_DAILY_REMINDER_CONTACTS = 0;
    private static final int NOTIFICATION_ID_DAILY_REMINDER_NAMEDAYS = 1;
    private static final int NOTIFICATION_ID_DAILY_REMINDER_BANKHOLIDAYS = 2;
    private static final int MAX_CONTACTS = 3;

    private final Context context;
    private final NotificationManager notificationManager;
    private final ImageLoader imageLoader;
    private final Strings strings;
    private final DimensionResources dimensions;
    private final ColorResources colorResources;
    private final DailyReminderPreferences preferences;

    DailyReminderNotifier(Context context,
                          NotificationManager notificationManager,
                          ImageLoader imageLoader,
                          Strings strings,
                          ColorResources colorResources,
                          DimensionResources dimensions,
                          DailyReminderPreferences preferences) {
        this.notificationManager = notificationManager;
        this.strings = strings;
        this.imageLoader = imageLoader;
        this.context = context.getApplicationContext();
        this.dimensions = dimensions;
        this.colorResources = colorResources;
        this.preferences = preferences;
    }

    void forDailyReminder(Date date, List<ContactEvent> events) {
        Bitmap largeIcon = null;
        int contactCount = events.size();

        if (shouldDisplayContactImage(contactCount)) {
            Contact displayingContact = events.get(0).getContact();
            int size = dimensions.getPixelSize(android.R.dimen.notification_large_icon_width);
            Optional<Bitmap> loadedIcon =
                    imageLoader.load(displayingContact.getImagePath())
                            .withSize(size, size)
                            .synchronously();
            if (Version.hasLollipop() && loadedIcon.isPresent()) {
                // in Lollipop the notifications is the default to use Rounded Images
                largeIcon = getCircleBitmap(loadedIcon.get());
            }
        }

        Intent startIntent = UpcomingEventsActivity.getStartIntent(context);
        PendingIntent intent =
                PendingIntent.getActivity(
                        context, NOTIFICATION_ID_DAILY_REMINDER_CONTACTS,
                        startIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        List<Contact> contacts = new ArrayList<>();
        for (ContactEvent event : events) {
            contacts.add(event.getContact());
        }
        String title = NaturalLanguageUtils.joinContacts(strings, contacts, MAX_CONTACTS);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_stat_memento)
                .setContentTitle(title)
                .setLargeIcon(largeIcon)
                .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                .setAutoCancel(true)
                .setContentIntent(intent)
                .setNumber(events.size())
                .setColor(colorResources.getColor(R.color.main_red))
                .setSound(preferences.getRingtoneSelected());

        if (events.size() == 1) {
            ContactEvent event = events.get(0);
            String msg = event.getLabel(date, strings);

            NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle().bigText(msg);
            bigTextStyle.setBigContentTitle(title);
            builder.setContentText(msg);

            builder.setStyle(bigTextStyle);

        } else if (contacts.size() > 1) {
            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
            inboxStyle.setBigContentTitle(title);

            for (int i = 0; i < events.size(); ++i) {

                ContactEvent event = events.get(i);
                Contact contact = event.getContact();
                String name = contact.getDisplayName().toString();

                String lineFormatted = name + "\t\t" + event.getLabel(date, strings);

                Spannable sb = new SpannableString(lineFormatted);
                sb.setSpan(new StyleSpan(Typeface.BOLD), 0, name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                inboxStyle.addLine(sb);
            }

            builder.setStyle(inboxStyle);
            builder.setContentText(TextUtils.join(", ", contacts));
        }

        if (supportsPublicNotifications()) {
            String publicTitle = context.getString(R.string.contact_celebration_count, contactCount);
            NotificationCompat.Builder publicNotification = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.ic_stat_memento)
                    .setAutoCancel(true)
                    .setContentIntent(intent)
                    .setContentTitle(publicTitle)
                    .setColor(colorResources.getColor(R.color.main_red));

            builder.setPublicVersion(publicNotification.build());
        }

        if (preferences.getVibrationSet()) {
            builder.setDefaults(Notification.DEFAULT_VIBRATE);
        }

        Notification notification = builder.build();

        notificationManager.notify(NOTIFICATION_ID_DAILY_REMINDER_CONTACTS, notification);

    }

    private static Bitmap getCircleBitmap(Bitmap bitmap) {
        final Bitmap output = Bitmap.createBitmap(
                bitmap.getWidth(),
                bitmap.getHeight(),
                Bitmap.Config.ARGB_8888
        );
        final Canvas canvas = new Canvas(output);

        final int color = Color.RED;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    private boolean supportsPublicNotifications() {
        return Version.hasLollipop();
    }

    private boolean shouldDisplayContactImage(int contactCount) {
        return contactCount == 1;
    }

    void forNamedays(List<String> names, Date date) {
        if (names == null || names.isEmpty()) {
            return;
        }
        PendingIntent intent = PendingIntent.getActivity(
                context, NOTIFICATION_ID_DAILY_REMINDER_NAMEDAYS,
                NamedayActivity.Companion.getStartIntent(context, date),
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        String subtitle = NaturalLanguageUtils.join(strings, names, 1);
        String fullsubtitle = TextUtils.join(", ", names);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_stat_namedays)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(fullsubtitle))
                .setContentTitle(strings.todaysNamedays(names.size()))
                .setContentText(subtitle)
                .setAutoCancel(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setColor(colorResources.getColor(R.color.nameday_blue))
                .setContentIntent(intent);
        if (names.size() > 1) {
            mBuilder.setNumber(names.size());
        }
        notificationManager.notify(NOTIFICATION_ID_DAILY_REMINDER_NAMEDAYS, mBuilder.build());
    }

    public void cancelAllEvents() {
        notificationManager.cancel(NOTIFICATION_ID_DAILY_REMINDER_CONTACTS);
        notificationManager.cancel(NOTIFICATION_ID_DAILY_REMINDER_NAMEDAYS);
        notificationManager.cancel(NOTIFICATION_ID_DAILY_REMINDER_BANKHOLIDAYS);

    }

    void forBankholiday(BankHoliday bankHoliday) {
        PendingIntent intent = PendingIntent.getActivity(
                context, NOTIFICATION_ID_DAILY_REMINDER_BANKHOLIDAYS,
                UpcomingEventsActivity.getStartIntent(context),
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        String subtitle = bankHoliday.getHolidayName();
        CharSequence title = context.getString(R.string.dailyreminder_title_bankholiday);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_stat_bankholidays)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(subtitle))
                .setContentTitle(title)
                .setContentText(subtitle)
                .setAutoCancel(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setColor(colorResources.getColor(R.color.bankholiday_green))
                .setContentIntent(intent);
        notificationManager.notify(NOTIFICATION_ID_DAILY_REMINDER_BANKHOLIDAYS, builder.build());
    }
}
