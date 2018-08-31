package com.alexstyl.specialdates.contact

import android.content.ContentResolver
import com.alexstyl.specialdates.CrashAndErrorTracker
import com.alexstyl.specialdates.contact.ContactSource.SOURCE_DEVICE
import com.alexstyl.specialdates.contact.ContactSource.SOURCE_FACEBOOK
import com.alexstyl.specialdates.events.database.EventSQLiteOpenHelper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ContactsModule {

    @Provides
    @Singleton
    internal fun provider(contentResolver: ContentResolver,
                          eventSQLiteOpenHelper: EventSQLiteOpenHelper,
                          tracker: CrashAndErrorTracker): ContactsProvider {
        return ContactsProvider(mapOf(
                Pair(SOURCE_DEVICE, buildAndroidSource(tracker, contentResolver)),
                Pair(SOURCE_FACEBOOK, buildFacebookSource(eventSQLiteOpenHelper))
        ))
    }

    companion object {

        private const val CACHE_SIZE = 1024

        private fun buildAndroidSource(tracker: CrashAndErrorTracker, contentResolver: ContentResolver): ContactsProviderSource {
            val factory = AndroidContactFactory(contentResolver)
            val contactCache = ContactCache(CACHE_SIZE)
            return AndroidContactsProviderSource(contactCache, factory)
        }

        private fun buildFacebookSource(eventSQLHelper: EventSQLiteOpenHelper): ContactsProviderSource {
            val contactCache = ContactCache(CACHE_SIZE)
            return FacebookContactsSource(eventSQLHelper, contactCache)
        }
    }
}
