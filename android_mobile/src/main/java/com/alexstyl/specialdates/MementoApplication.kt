package com.alexstyl.specialdates

import android.annotation.SuppressLint
import android.content.Context
import android.support.multidex.MultiDexApplication
import com.alexstyl.resources.ResourcesModule
import com.alexstyl.specialdates.dailyreminder.DailyReminderOreoChannelCreator
import com.alexstyl.specialdates.dailyreminder.DailyReminderScheduler
import com.alexstyl.specialdates.dailyreminder.DailyReminderUserSettings
import com.alexstyl.specialdates.events.namedays.activity.NamedaysInADayModule
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsModule
import com.alexstyl.specialdates.facebook.FacebookModule
import com.alexstyl.specialdates.images.AndroidContactsImageDownloader
import com.alexstyl.specialdates.images.ImageModule
import com.alexstyl.specialdates.images.NutraBaseImageDecoder
import com.alexstyl.specialdates.permissions.MementoPermissions
import com.alexstyl.specialdates.theming.ThemingModule
import com.alexstyl.specialdates.ui.widget.ViewModule
import com.evernote.android.job.JobManager
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.assist.QueueProcessingType
import com.nostra13.universalimageloader.utils.L
import com.novoda.notils.logger.simple.Log
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import net.danlew.android.joda.JodaTimeAndroid
import javax.inject.Inject

@SuppressLint("Registered")
open class MementoApplication : MultiDexApplication() {

    @Inject lateinit var tracker: CrashAndErrorTracker
    @Inject lateinit var jobCreator: JobsCreator
    @Inject lateinit var permissions: MementoPermissions

    @Inject lateinit var dailyReminderOreoChannelCreator: DailyReminderOreoChannelCreator
    @Inject lateinit var dailyReminderScheduler: DailyReminderScheduler
    @Inject lateinit var dailyReminderUserSettings: DailyReminderUserSettings
    @Inject lateinit var mementoUserSettings: MementoUserSettings

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


        if (mementoUserSettings.isFirstTimeBooting()) {
            initialiseMementoAsync()
        }
    }

    private fun initialiseMementoAsync() {
        Observable.fromCallable {
            dailyReminderOreoChannelCreator.createDailyReminderChannel()
            val timeSet = dailyReminderUserSettings.getTimeSet()
            dailyReminderScheduler.scheduleReminderFor(timeSet)
            mementoUserSettings.setFirstTimeBoot(false)
            Log.d("Memento initialised")
        }
                .subscribeOn(Schedulers.io())
                .subscribe()
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
}

