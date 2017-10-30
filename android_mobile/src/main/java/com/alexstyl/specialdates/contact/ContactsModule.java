package com.alexstyl.specialdates.contact;

import android.content.ContentResolver;
import android.content.Context;

import com.alexstyl.specialdates.events.database.EventSQLiteOpenHelper;
import com.alexstyl.specialdates.permissions.PermissionChecker;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

import dagger.Module;
import dagger.Provides;

import static com.alexstyl.specialdates.contact.ContactSource.SOURCE_DEVICE;
import static com.alexstyl.specialdates.contact.ContactSource.SOURCE_FACEBOOK;

@Module
@Singleton
public class ContactsModule {

    private static final int CACHE_SIZE = 1024;

    @Provides
    @Singleton
    ContactsProvider provider(Context context) {
        Map<Integer, ContactsProviderSource> sources = new HashMap<>();
        sources.put(SOURCE_DEVICE, buildAndroidSource(context));
        sources.put(SOURCE_FACEBOOK, buildFacebookSource(context));
        return new ContactsProvider(sources);
    }

    private static ContactsProviderSource buildAndroidSource(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        AndroidContactFactory factory = new AndroidContactFactory(contentResolver);
        ContactCache contactCache = new ContactCache(CACHE_SIZE);
        return new AndroidContactsProviderSource(contactCache, factory);
    }

    private static ContactsProviderSource buildFacebookSource(Context context) {
        ContactCache contactCache = new ContactCache(CACHE_SIZE);
        return new FacebookContactsSource(new EventSQLiteOpenHelper(context), contactCache);
    }
}
