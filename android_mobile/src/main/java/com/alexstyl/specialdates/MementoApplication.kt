package com.alexstyl.specialdates

import android.annotation.SuppressLint
import android.content.Context
import android.support.multidex.MultiDexApplication
import com.alexstyl.resources.ResourcesModule
import com.alexstyl.specialdates.dailyreminder.AndroidDailyReminderScheduler
import com.alexstyl.specialdates.dailyreminder.DailyReminderUserSettings
import com.alexstyl.specialdates.events.namedays.activity.NamedaysInADayModule
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsModule
import com.alexstyl.specialdates.events.peopleevents.UpcomingEventsSettings
import com.alexstyl.specialdates.facebook.FacebookModule
import com.alexstyl.specialdates.facebook.FacebookUserSettings
import com.alexstyl.specialdates.facebook.friendimport.FacebookFriendsUpdaterScheduler
import com.alexstyl.specialdates.images.AndroidContactsImageDownloader
import com.alexstyl.specialdates.images.ImageModule
import com.alexstyl.specialdates.images.NutraBaseImageDecoder
import com.alexstyl.specialdates.permissions.MementoPermissions
import com.alexstyl.specialdates.theming.ThemingModule
import com.alexstyl.specialdates.ui.widget.ViewModule
import com.alexstyl.specialdates.upcoming.PeopleEventsRefreshJob
import com.evernote.android.job.DailyJob
import com.evernote.android.job.JobManager
import com.evernote.android.job.JobRequest
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.assist.QueueProcessingType
import com.nostra13.universalimageloader.utils.L
import com.novoda.notils.logger.simple.Log
import net.danlew.android.joda.JodaTimeAndroid
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@SuppressLint("Registered")
open class MementoApplication : MultiDexApplication() {

    @Inject lateinit var tracker: CrashAndErrorTracker
    @Inject lateinit var facebookSettings: FacebookUserSettings
    @Inject lateinit var jobCreator: JobsCreator
    @Inject lateinit var permissions: MementoPermissions
    @Inject lateinit var upcomingEventsSettings: UpcomingEventsSettings
    @Inject lateinit var dailyReminderUserSettings: DailyReminderUserSettings

    lateinit var applicationModule: AppComponent


    override fun onCreate() {
        super.onCreate()

        initialiseDependencies()

        applicationModule = DaggerAppComponent.builder()
                .androidApplicationModule(AndroidApplicationModule(this))
                .resourcesModule(ResourcesModule(this, resources))
                .imageModule(ImageModule(resources))
                .peopleEventsModule(PeopleEventsModule(this))
                .themingModule(ThemingModule())
                .viewModule(ViewModule(resources))
                .facebookModule(FacebookModule(this))
                .namedaysInADayModule(NamedaysInADayModule())
                .build()

        applicationModule.inject(this)

        tracker.startTracking()

        JobManager.create(this).addJobCreator(jobCreator)

        if (dailyReminderUserSettings.isEnabled()) {
            AndroidDailyReminderScheduler()
                    .scheduleReminderFor(dailyReminderUserSettings.getTimeSet())
        }

        if (facebookSettings.isLoggedIn) {
            FacebookFriendsUpdaterScheduler().scheduleNext()
        }

        scheduleContactsJob()
    }

    private fun scheduleContactsJob() {
        if (permissions.canReadAndWriteContacts()
                && !upcomingEventsSettings.hasBeenInitialised()) {
            JobRequest.Builder(PeopleEventsRefreshJob.TAG)
                    .startNow()
                    .build()
                    .schedule()
        }
        DailyJob.schedule(
                JobRequest.Builder(PeopleEventsRefreshJob.TAG),
                1.oClock(), 3.oClock())
    }

    protected open fun initialiseDependencies() {
        Log.setShowLogs(BuildConfig.DEBUG)
        JodaTimeAndroid.init(this)
        initImageLoader(this)
    }

    companion object {
        fun initImageLoader(context: Context) {
            val config = ImageLoaderConfiguration.Builder(context)
                    .threadPriority(Thread.MIN_PRIORITY)
                    .threadPoolSize(10)
                    .tasksProcessingOrder(QueueProcessingType.LIFO)
                    .imageDecoder(NutraBaseImageDecoder(BuildConfig.DEBUG))
                    .imageDownloader(AndroidContactsImageDownloader(context))
            L.writeLogs(BuildConfig.DEBUG)

            com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(config.build())
        }
    }

    private fun Int.oClock(): Long = TimeUnit.HOURS.toMillis(this.toLong())
}

