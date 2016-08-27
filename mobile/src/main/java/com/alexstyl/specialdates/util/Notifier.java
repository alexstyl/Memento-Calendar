package com.alexstyl.specialdates.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.datedetails.DateDetailsActivity;
import com.alexstyl.specialdates.events.ContactEvents;
import com.alexstyl.specialdates.events.bankholidays.BankHoliday;
import com.alexstyl.specialdates.events.namedays.NamedayPreferences;
import com.alexstyl.specialdates.images.ImageLoader;
import com.alexstyl.specialdates.settings.MainPreferenceActivity;
import com.novoda.notils.logger.simple.Log;

import java.util.List;

public class Notifier {

    private static final int NOTIFICATION_ID_DAILY_REMINDER_CONTACTS = 0;
    private static final int NOTIFICATION_ID_DAILY_REMINDER_NAMEDAYS = 1;
    private static final int NOTIFICATION_ID_DAILY_REMINDER_BANKHOLIDAYS = 2;

    private static final String TAG = "Notifier";

    private final Context context;
    private final ImageLoader imageLoader;
    private final NamedayPreferences namedayPreferences;

    public static Notifier newInstance(Context context) {
        Resources resources = context.getResources();
        ImageLoader imageLoader = ImageLoader.createSquareThumbnailLoader(resources);
        NamedayPreferences namedayPreferences = NamedayPreferences.newInstance(context);
        return new Notifier(context, imageLoader, namedayPreferences);
    }

    public Notifier(Context context, ImageLoader imageLoader, NamedayPreferences namedayPreferences) {
        this.imageLoader = imageLoader;
        this.namedayPreferences = namedayPreferences;
        this.context = context.getApplicationContext();
    }

    /**
     * Notifies the user about the special dates contained in the given daycard
     *
     * @param events The celebration date to display
     */
    public void forDailyReminder(ContactEvents events) {
        Bitmap largeIcon = null;
        Resources res = context.getResources();
        Date date = events.getDate();
        int contactCount = events.size();

        if (shouldDisplayContactImage(contactCount)) {
            // Large Icons were introduced in Honeycomb
            // and we are only displaying one if it is one contact
            int size = res.getDimensionPixelSize(android.R.dimen.notification_large_icon_width);
            Contact displayingContact = events.getContacts().iterator().next();
            largeIcon = loadImageAsync(displayingContact, size, size);
            if (Utils.hasLollipop() && largeIcon != null) {
                // in Lollipop the notifications is the default to use Rounded Images
                largeIcon = getCircleBitmap(largeIcon);
            }

        }

        Intent startIntent = DateDetailsActivity.getStartIntentFromExternal(context, date.getDayOfMonth(), date.getMonth(), date.getYear());
        PendingIntent intent =
                PendingIntent.getActivity(
                        context, NOTIFICATION_ID_DAILY_REMINDER_CONTACTS,
                        startIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        String title = NaturalLanguageUtils.joinContacts(context, events.getContacts(), 3);
        String fullText = TextUtils.join(", ", events.getContacts());

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_stat_contact_event)
                .setContentTitle(title)
                .setLargeIcon(largeIcon)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(fullText))
                .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                .setAutoCancel(true)
                .setContentText(fullText)
                .setContentIntent(intent)
                .setColor(context.getResources().getColor(R.color.main_red));

        if (supportsPublicNotifications()) {
            String publicTitle = context.getString(R.string.contact_celebration_count, contactCount);
            NotificationCompat.Builder publicNotification = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.ic_stat_contact_event)
                    .setAutoCancel(true)
                    .setContentIntent(intent)
                    .setContentTitle(publicTitle)
                    .setColor(context.getResources().getColor(R.color.main_red));

            builder.setPublicVersion(publicNotification.build());
        }

        builder.setNumber(contactCount);

        for (Contact contact : events.getContacts()) {
            Uri uri = contact.getLookupUri();
            if (uri != null) {
                builder.addPerson(uri.toString());
            }
        }

        String uri = MainPreferenceActivity.getDailyReminderRingtone(context);
        builder.setSound(Uri.parse(uri));

        if (MainPreferenceActivity.getDailyReminderVibrationSet(context)) {
            builder.setDefaults(Notification.DEFAULT_VIBRATE);
        }

        Notification notification = builder.build();

        NotificationManager mngr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mngr.notify(NOTIFICATION_ID_DAILY_REMINDER_CONTACTS, notification);

    }

    public static Bitmap getCircleBitmap(Bitmap bitmap) {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                                                  bitmap.getHeight(), Bitmap.Config.ARGB_8888
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

    private Bitmap loadImageAsync(Contact displayingContact, int width, int height) {
        return imageLoader.loadBitmap(displayingContact.getImagePath(), width, height);
    }

    private boolean supportsPublicNotifications() {
        return Utils.hasLollipop();
    }

    private boolean shouldDisplayContactImage(int contactCount) {
        return Utils.hasHoneycomb() && contactCount == 1;
    }

    public void forNamedays(List<String> names, Date date) {
        if (names == null || names.isEmpty()) {
            Log.w(TAG, "Tried to notify for empty name list");
            return;
        }
        PendingIntent intent = PendingIntent.getActivity(
                context, NOTIFICATION_ID_DAILY_REMINDER_NAMEDAYS,
                DateDetailsActivity.getStartIntentFromExternal(context, date.getDayOfMonth(), date.getMonth(), date.getYear()),
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        String subtitle = NaturalLanguageUtils.join(context, names, 1);
        String fullsubtitle = TextUtils.join(", ", names);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_stat_namedays)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(fullsubtitle))
                .setContentTitle(context.getResources().getQuantityString(R.plurals.todays_nameday, names.size()))
                .setContentText(subtitle)
                .setAutoCancel(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setColor(context.getResources().getColor(R.color.nameday_blue))
                .setContentIntent(intent);
        if (names.size() > 1) {
            mBuilder.setNumber(names.size());
        }
        NotificationManager mngr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mngr.notify(NOTIFICATION_ID_DAILY_REMINDER_NAMEDAYS, mBuilder.build());
    }

    public void cancelAllEvents() {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(NOTIFICATION_ID_DAILY_REMINDER_CONTACTS);
        manager.cancel(NOTIFICATION_ID_DAILY_REMINDER_NAMEDAYS);
        manager.cancel(NOTIFICATION_ID_DAILY_REMINDER_BANKHOLIDAYS);

    }

    public void forBankholiday(Date date, BankHoliday bankHoliday) {
        PendingIntent intent = PendingIntent.getActivity(
                context, NOTIFICATION_ID_DAILY_REMINDER_BANKHOLIDAYS,
                DateDetailsActivity.getStartIntentFromExternal(context, date.getDayOfMonth(), date.getMonth(), date.getYear()),
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
                .setColor(context.getResources().getColor(R.color.bankholiday_green))
                .setContentIntent(intent);
        NotificationManager mngr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mngr.notify(NOTIFICATION_ID_DAILY_REMINDER_BANKHOLIDAYS, builder.build());
    }
}
